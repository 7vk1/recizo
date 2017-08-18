package com.recizo.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.recizo.R
import com.recizo.model.entity.RecizoRecipe
import com.recizo.module.FavoriteRecipeDao
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FavoriteRecipeAdapter(val changeVisibility: FavoriteRecipePresenter.ChangeVisibility,
                            val error: FavoriteRecipePresenter.Error,
                            val intent: FavoriteRecipePresenter.Intent)
  : RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeViewHolder>() {

  var recycleView: RecyclerView? = null
  val favoriteRecipeList = mutableListOf<RecizoRecipe>()
  val garbageList = mutableSetOf<Long>()
  val undoList: MutableList<Long> = mutableListOf()
  init {
    this.setHasStableIds(true)
  }

  fun onDeleteClicked() {
    garbageList.sortedBy { it }.reversed().map {
      FavoriteRecipeDao.remove(favoriteRecipeList[it.toInt()].title)
      favoriteRecipeList.removeAt(it.toInt())
    }
    notifyDataSetChanged()
    garbageList.clear()
    changeVisibility.changeButtonVisibility(remove = false, undo = false)
    if(isFavoriteEmpty()) changeVisibility.changeTextVisibility(true)
    else changeVisibility.changeTextVisibility(false)
  }

  fun onUndoClicked() {
    garbageList.map {
      val item = recycleView?.findViewHolderForItemId(it)
      if(item != null) (item as FavoriteRecipeViewHolder).swipeLayout.close()
    }
    garbageList.clear()
    changeVisibility.changeButtonVisibility(remove = false, undo = false)
  }

  fun viewFavoriteList() {
    FavoriteRecipeDao.getAll()!!.forEach {
      favoriteRecipeList.add(
          RecizoRecipe(title = it.title,
              cookpadLink = it.cookpadLink,
              author = it.author,
              description = it.description,
              imgUrl = it.imgUrl)
      )
      notifyItemInserted(favoriteRecipeList.size)
    }
    if(isFavoriteEmpty()) changeVisibility.changeTextVisibility(true)
    else changeVisibility.changeTextVisibility(false)
  }

  private fun isFavoriteEmpty(): Boolean {
    if(favoriteRecipeList.size == 0) return true
    return false
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

  override fun getItemCount(): Int { return favoriteRecipeList.size }

  override fun onBindViewHolder(holder: FavoriteRecipeViewHolder?, position: Int) {
    holder!!.title.text = favoriteRecipeList[position].title
    holder.author.text = favoriteRecipeList[position].author
    holder.description.text = favoriteRecipeList[position].description
    launch(UI) {
      try{
        holder.imageUrl.setImageBitmap(getBitmapImage(favoriteRecipeList[position].imgUrl).await())
      }catch (e: Exception){
        // TODO ここでToast出すとお気に入りの数だけToastが出続けるからToastを出すなら他の場所にする。Toastから読み込み失敗画像に変える予定
        error.failedGetImage()
      }
    }
    holder.cardFrame.setOnClickListener{ intent.onRecipe(Uri.parse(favoriteRecipeList[position].cookpadLink)) }
    holder.swipeLayout.addSwipeListener(object :SimpleSwipeListener(){
      override fun onOpen(layout: SwipeLayout?) {
        super.onOpen(layout)
        if(layout!!.dragEdge == SwipeLayout.DragEdge.Right) {
          garbageList.add(holder.itemId)
          undoList.add(holder.itemId)
          changeVisibility.changeButtonVisibility(remove = true, undo = true)
        }
      }

      override fun onClose(layout: SwipeLayout?) {
        super.onClose(layout)
        garbageList.remove(holder.itemId)
        undoList.remove(holder.itemId)
        if(garbageList.size == 0) {
          changeVisibility.changeButtonVisibility(remove = false, undo = false)
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

  override fun onViewAttachedToWindow(holder: FavoriteRecipeViewHolder?) {
    super.onViewAttachedToWindow(holder)
    launch(UI) {
      delay(100)
      if (garbageList.contains(holder?.itemId)) holder?.swipeLayout?.open(true, false, SwipeLayout.DragEdge.Right)
      else if (undoList.contains(holder?.itemId)) holder?.swipeLayout?.open(true, false, SwipeLayout.DragEdge.Left)
      else holder?.swipeLayout?.close(false)
    }
  }

  override fun getItemId(position: Int): Long { return position.toLong() }

  class FavoriteRecipeViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById(R.id.recipe_title)
    val author: TextView = v.findViewById(R.id.recipe_author)
    val description: TextView = v.findViewById(R.id.recipe_description)
    val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
    val cardFrame: CardView = v.findViewById(R.id.favorite_recipe_surface_cardview)
    val swipeLayout: SwipeLayout = v.findViewById(R.id.swipelayout)
  }
}