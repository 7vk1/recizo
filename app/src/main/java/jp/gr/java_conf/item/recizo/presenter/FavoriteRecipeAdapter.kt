package jp.gr.java_conf.item.recizo.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.entity.CookpadRecipe
import jp.gr.java_conf.item.recizo.model.viewholder.RecipeViewHolder
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FavoriteRecipeAdapter: RecyclerView.Adapter<RecipeViewHolder>() {
  val favoriteRecipeList = mutableListOf<CookpadRecipe>()
  override fun getItemCount(): Int {
    return favoriteRecipeList.size
  }

  override fun onBindViewHolder(holder: RecipeViewHolder?, position: Int) {
    holder!!.title.text = favoriteRecipeList[position].title
    holder.author.text = favoriteRecipeList[position].author
    holder.description.text = favoriteRecipeList[position].description
    launch(UI) {
      val image = getImageStream(favoriteRecipeList[position].imgUrl).await()
      holder.imageUrl.setImageBitmap(image)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecipeViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.favorite_recipe_item, parent, false)
    return RecipeViewHolder(v)
  }

  private fun getImageStream(imageUrl: String) = async(CommonPool) {
    val url = URL(imageUrl)
    // image scale
    val scale = 3
    val bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
    val width = bitmapImage.width * scale
    val height = bitmapImage.height * scale
    // TODO 画像取得失敗時のエラーハンドリング
    return@async Bitmap.createScaledBitmap(bitmapImage, width, height, false)
  }
}