package jp.gr.java_conf.item.recizo.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
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

class RecipeListAdapter: RecyclerView.Adapter<RecipeViewHolder>() {
  val recipeList = mutableListOf<CookpadRecipe>()

  override fun getItemCount(): Int {
    return recipeList.size
  }

  override fun onBindViewHolder(holder: RecipeViewHolder?, position: Int) {
    holder!!.title.text = recipeList[position].title
    holder.author.text = recipeList[position].author
    holder.description.text = recipeList[position].description
    launch(UI) {
      val image = getImageStream(recipeList[position].imgUrl).await()
      holder.imageUrl.setImageBitmap(image)
    }

    Log.d("TEST RECIPE TITLE", holder.title.text.toString() )

    holder.starButton.isChecked = false
    if(FavoriteRecipeDao.getRecipe(holder.title.text.toString() ) != null ) {
      Log.d("TEST", "FAV DB HIT")
      holder.starButton.isChecked = true
    }

    holder.starButton.setOnClickListener {
      Log.d("TEST STAR BUTTON", "CLICK")
      FavoriteRecipeDao.add(
          CookpadRecipe(title = holder.title.text.toString(),
              author = holder.author.text.toString(),
              description = holder.description.text.toString(),
              imgUrl = holder.imageUrl.toString(),
              cookpadLink = recipeList[position].cookpadLink)
      )
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecipeViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.searched_list_item, parent, false)
    return RecipeViewHolder(v)
  }

  fun addRecipe(recipe: CookpadRecipe) {
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
    // TODO 画像取得失敗時のエラーハンドリング
    return@async Bitmap.createScaledBitmap(bitmapImage, width, height, false)
  }

}