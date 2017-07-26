package jp.gr.java_conf.item.recizo.presenter

import android.content.Context
import jp.gr.java_conf.item.recizo.model.database.FavoriteRecipeDatabaseHelper
import jp.gr.java_conf.item.recizo.model.entity.CookpadRecipe

object FavoriteRecipeDao {
  var context: Context? = null
    get
    set(value) {
      if(field != null) return
      else field = value
    }

  private var favoriteRecipeDatabaseHelper: FavoriteRecipeDatabaseHelper? = null

  fun access() {
    favoriteRecipeDatabaseHelper = FavoriteRecipeDatabaseHelper(context!!)
  }
  fun close() {
    favoriteRecipeDatabaseHelper = null
  }

  fun add(recipe: CookpadRecipe) {
    favoriteRecipeDatabaseHelper?.addRecipe(recipe)
  }

  fun remove(recipeTitle: String) {
    favoriteRecipeDatabaseHelper?.removeRecipe(recipeTitle)
  }

  fun getAll(): List<CookpadRecipe>? {
    return favoriteRecipeDatabaseHelper?.getRecipeAll()
  }

  fun getRecipe(recipeTitle: String): CookpadRecipe? {
    var recipe: CookpadRecipe? = null
    try {
      recipe = favoriteRecipeDatabaseHelper?.getRecipe(recipeTitle)
    }catch (e: Exception) {}
    return recipe
  }
}