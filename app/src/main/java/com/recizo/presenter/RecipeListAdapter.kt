package com.recizo.presenter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.model.entity.RecizoRecipe
import com.recizo.model.viewholder.RecipeViewHolder
import com.recizo.module.FavoriteRecipeDao
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class RecipeListAdapter(private val recyclerView: RecyclerView, private val view: View): RecyclerView.Adapter<RecipeViewHolder>(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
  private var onItemClickListener: OnItemClickListener? = null
  private var onRefreshPullListener: RecipeListAdapter.OnRefreshPullListner? = null
  val recipeList = mutableListOf<RecizoRecipe>()

  fun setOnItemClickListener(listener: OnItemClickListener){ onItemClickListener = listener }

  fun clearRecipe() {
    val size = recipeList.size
    recipeList.clear()
    notifyItemRangeRemoved(0, size)
  }

  fun addRecipe(recipe: RecizoRecipe) {
    recipeList.add(recipe)
    notifyItemInserted(recipeList.size)
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

  override fun getItemCount(): Int { return recipeList.size }

  override fun onBindViewHolder(holder: RecipeViewHolder?, position: Int) {
    launch(UI) {
      try {
        val image = getImageStream(recipeList[position].imgUrl).await()
        holder!!.imageUrl.setImageBitmap(image)
      }catch (e: Exception) {
        holder!!.imageUrl.setImageResource(R.drawable.ic_imagenotfound)
        e.printStackTrace()
      }
    }
    holder!!.title.text = recipeList[position].title
    holder.author.text = recipeList[position].author
    holder.description.text = recipeList[position].description
    holder.linkUrl = recipeList[position].cookpadLink
    holder.starButton.isChecked = false
    if(FavoriteRecipeDao.getRecipe(holder.title.text.toString() ) != null ) holder.starButton.isChecked = true
    holder.starButton.setOnClickListener {
      if(holder.starButton.isChecked) {
        FavoriteRecipeDao.add(
            RecizoRecipe(
                id = null,
                title = holder.title.text.toString(),
                author = holder.author.text.toString(),
                description = holder.description.text.toString(),
                imgUrl = recipeList[position].imgUrl,
                cookpadLink = recipeList[position].cookpadLink)
        )
      } else FavoriteRecipeDao.remove(holder.title.text.toString() )
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecipeViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.searched_list_item, parent, false)
    v.setOnClickListener(this)
    return RecipeViewHolder(v)
  }

  override fun onClick(view: View?) {
    val position = recyclerView.getChildAdapterPosition(view)
    onItemClickListener?.onItemClick(this.recipeList[position])
  }

  override fun onRefresh() { onRefreshPullListener?.onRefreshPull() }

  interface OnItemClickListener { fun onItemClick(recipe: RecizoRecipe) }
  interface OnRefreshPullListner { fun onRefreshPull() }
}