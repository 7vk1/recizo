package com.recizo.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.model.entity.CookpadRecipe
import com.recizo.model.viewholder.FavoriteRecipeViewHolder
import com.recizo.module.FavoriteRecipeDao
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FavoriteRecipeAdapter(private val favoriteRecipeListView: RecyclerView): RecyclerView.Adapter<FavoriteRecipeViewHolder>(), View.OnClickListener {
  val favoriteRecipeList = mutableListOf<CookpadRecipe>()
  private var onItemClickListener: FavoriteRecipeAdapter.OnItemClickListener? = null
  fun setOnItemClickListener(listener: FavoriteRecipeAdapter.OnItemClickListener){
    Log.d("_TEST", listener.toString())
    onItemClickListener = listener
  }

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
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FavoriteRecipeViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.favorite_recipe_item, parent, false)
    v.setOnClickListener(this)
    Log.d("_TEST", "VIEW HOLDER SET ON CLICK LISTENER")
    return FavoriteRecipeViewHolder(v)
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

  override fun onClick(view: View?) {
    Log.d("_TEST", "ON CLICK")
    val position = favoriteRecipeListView.getChildAdapterPosition(view)
    onItemClickListener?.onItemClick(this.favoriteRecipeList[position])
  }

  interface OnItemClickListener {
    fun onItemClick(recipe: CookpadRecipe)
  }
}