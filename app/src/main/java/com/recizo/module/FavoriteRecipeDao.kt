package com.recizo.module

import android.content.Context
import com.recizo.model.database.FavoriteRecipeDatabaseHelper
import com.recizo.model.entity.CookpadRecipe

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
    favoriteRecipeDatabaseHelper!!.addRecipe(recipe)
  }

  fun remove(recipeTitle: String) {
    favoriteRecipeDatabaseHelper!!.removeRecipe(recipeTitle)
  }

  fun getAll(): List<CookpadRecipe>? {
    return favoriteRecipeDatabaseHelper?.getRecipeAll()
  }

  fun getRecipe(recipeTitle: String): CookpadRecipe? {
    try {
      val recipe = favoriteRecipeDatabaseHelper?.getRecipe(recipeTitle)
      return recipe
    }catch (e: Exception) {
      return null
    }
  }
}