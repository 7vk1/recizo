package com.recizo.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.recizo.R
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.FavoriteRecipeDao
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FavoriteRecipeAdapter(private val context: Context, private val favoriteRecipeListView: RecyclerView, private val removeBtn: FloatingActionButton, private val undoBtn: FloatingActionButton): RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeViewHolder>() {
  init {this.setHasStableIds(true)}
  val favoriteRecipeList = mutableListOf<CookpadRecipe>()
  val garbageList = mutableSetOf<Long>()
  var recycleView: RecyclerView? = null
  val undoList: MutableList<Long> = mutableListOf()

  fun onDeleteClicked() {
    garbageList.sortedBy { it }.reversed().map {
      FavoriteRecipeDao.remove(favoriteRecipeList[it.toInt()].title)
      favoriteRecipeList.removeAt(it.toInt())
    }
    notifyDataSetChanged()
    garbageList.clear()
    changeBtnVisibility(remove = false, undo = false)
  }

  fun onUndoClicked() {
    garbageList.map {
      val item = recycleView?.findViewHolderForItemId(it)
      if(item != null) (item as FavoriteRecipeViewHolder).swipeLayout.close()
    }
    garbageList.clear()
    changeBtnVisibility(remove = false, undo = false)
  }

  private fun changeBtnVisibility(remove: Boolean, undo: Boolean) {
    undoBtn.visibility = if (undo) View.VISIBLE else View.INVISIBLE
    removeBtn.visibility = if (remove) View.VISIBLE else View.INVISIBLE
  }

  fun viewFavoriteList() {
    FavoriteRecipeDao.getAll()!!.forEach {
      favoriteRecipeList.add(
          CookpadRecipe(title = it.title,
              cookpadLink = it.cookpadLink,
              author = it.author,
              description = it.description,
              imgUrl = it.imgUrl)
      )
      notifyItemInserted(favoriteRecipeList.size)
    }
  }

  private fun getImageStream(imageUrl: String) = async(CommonPool) {
    val url = URL(imageUrl)
    return@async try{BitmapFactory.decodeStream(url.openConnection().getInputStream())}catch (e: Exception){throw e}
  }

  private fun getBitmapImage(imageUrl: String) = async(UI) {
    val SCALE = 3
    val bitmapImage: Bitmap
    try {bitmapImage = getImageStream(imageUrl).await()}catch (e: Exception) {throw e}
    val width = bitmapImage.width * SCALE
    val height = bitmapImage.height * SCALE
    return@async Bitmap.createScaledBitmap(bitmapImage, width, height, false)
  }

  override fun getItemCount(): Int {
    return favoriteRecipeList.size
  }

  override fun onBindViewHolder(holder: FavoriteRecipeViewHolder?, position: Int) {
    holder!!.title.text = favoriteRecipeList[position].title
    holder.author.text = favoriteRecipeList[position].author
    holder.description.text = favoriteRecipeList[position].description
    launch(UI) {
      try{
        holder.imageUrl.setImageBitmap(getBitmapImage(favoriteRecipeList[position].imgUrl).await())
      }catch (e: Exception){
        // TODO ここでToast出すとお気に入りの数だけToastが出続けるからToastを出すなら他の場所にする。Toastから読み込み失敗画像に変える予定
        Toast.makeText(favoriteRecipeListView.context, "画像の読み込みに失敗しました", Toast.LENGTH_SHORT).show()
      }
    }
    holder.cardFrame.setOnClickListener{
      context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(favoriteRecipeList[position].cookpadLink)))
    }
    holder.swipeLayout.addSwipeListener(object :SimpleSwipeListener(){
      override fun onOpen(layout: SwipeLayout?) {
        super.onOpen(layout)
        if(layout!!.dragEdge == SwipeLayout.DragEdge.Right) {
          println(holder.itemId)
          garbageList.add(holder.itemId)
          undoList.add(holder.itemId)
          removeBtn.visibility = View.VISIBLE
          undoBtn.visibility = View.VISIBLE
        }
      }

      override fun onClose(layout: SwipeLayout?) {
        super.onClose(layout)
        garbageList.remove(holder.itemId)
        undoList.remove(holder.itemId)
        if(garbageList.size == 0) {
          removeBtn.visibility = View.INVISIBLE
          undoBtn.visibility = View.INVISIBLE
        }
      }
    })
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
    super.onAttachedToRecyclerView(recyclerView)
    this.recycleView = recyclerView
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
    super.onDetachedFromRecyclerView(recyclerView)
    this.recycleView = null
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FavoriteRecipeViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.favorite_recipe_item, parent, false)
    return FavoriteRecipeViewHolder(v)
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  class FavoriteRecipeViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById(R.id.recipe_title)
    val author: TextView = v.findViewById(R.id.recipe_author)
    val description: TextView = v.findViewById(R.id.recipe_description)
    val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
    val cardFrame: CardView = v.findViewById(R.id.favorite_recipe_surface_cardview)
    val swipeLayout: SwipeLayout = v.findViewById(R.id.swipelayout)
  }
}