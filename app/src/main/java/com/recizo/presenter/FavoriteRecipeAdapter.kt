package com.recizo.presenter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.recizo.R
import com.recizo.model.entity.CookpadRecipe
import com.recizo.model.viewholder.FavoriteRecipeViewHolder
import com.recizo.module.AppContextHolder
import com.recizo.module.FavoriteRecipeDao
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FavoriteRecipeAdapter(private val favoriteRecipeListView: RecyclerView, private val removeBtn: FloatingActionButton, private val undoBtn: FloatingActionButton): RecyclerView.Adapter<FavoriteRecipeViewHolder>() {
  init {
    this.setHasStableIds(true)
  }
  val favoriteRecipeList = mutableListOf<CookpadRecipe>()
  val garbageList = mutableSetOf<Long>()
  var recycleView: RecyclerView? = null
  val undoList: MutableList<Long> = mutableListOf()

  override fun getItemCount(): Int {
    return favoriteRecipeList.size
  }

  override fun onBindViewHolder(holder: FavoriteRecipeViewHolder?, position: Int) {
    holder!!.title.text = favoriteRecipeList[position].title
    holder.author.text = favoriteRecipeList[position].author
    holder.description.text = favoriteRecipeList[position].description
    launch(UI) {
      val image = getImageStream(favoriteRecipeList[position].imgUrl).await()
      holder.imageUrl.setImageBitmap(image)
    }
    holder.cardFrame.setOnClickListener{
      AppContextHolder.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(favoriteRecipeList[position].cookpadLink)))
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

  fun onDeleteClicked() {
    garbageList.sortedBy { it }.reversed().map {
      FavoriteRecipeDao.remove(favoriteRecipeList[it.toInt()].title)
      favoriteRecipeList.removeAt(it.toInt())
    }
    garbageList.clear()
    this.notifyDataSetChanged()
    removeBtn.visibility = View.INVISIBLE
    undoBtn.visibility = View.INVISIBLE
  }

  fun onUndoClicked() {
    garbageList.map {
      val item = recycleView?.findViewHolderForItemId(it)
      if(item != null) (item as FavoriteRecipeViewHolder).swipeLayout.close()
    }
    garbageList.clear()
    undoBtn.visibility = View.INVISIBLE
    removeBtn.visibility = View.INVISIBLE

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
    // image scale
    val scale = 3
    val bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
    val width = bitmapImage.width * scale
    val height = bitmapImage.height * scale
    return@async Bitmap.createScaledBitmap(bitmapImage, width, height, false)
  }
}