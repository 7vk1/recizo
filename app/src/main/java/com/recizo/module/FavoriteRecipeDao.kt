package com.recizo.module

import android.content.Context
import com.recizo.model.database.FavoriteRecipeDatabaseHelper
import com.recizo.model.entity.RecizoRecipe

object FavoriteRecipeDao {
  var context: Context? = null
    set(value) {
      if(field != null) return
      else field = value
    }
  private var favoriteRecipeDatabaseHelper: FavoriteRecipeDatabaseHelper? = null

  fun add(recipe: RecizoRecipe) {
    this.access()
    favoriteRecipeDatabaseHelper!!.addRecipe(recipe)
    this.close()
  }

  fun remove(recipeTitle: String) {
    access()
    favoriteRecipeDatabaseHelper!!.removeRecipe(recipeTitle)
    close()
  }

  fun remove(recipeId: Int) {
    access()
    favoriteRecipeDatabaseHelper!!.removeRecipe(recipeId)
    close()
  }

  fun getAll(): List<RecizoRecipe>? {
    access()
    val ret = favoriteRecipeDatabaseHelper?.getRecipeAll()
    close()
    return ret
  }

  fun getRecipe(recipeTitle: String): RecizoRecipe? {
    access()
    try { return favoriteRecipeDatabaseHelper?.getRecipe(recipeTitle) }
    catch (e: Exception) { return null }
    finally { close() }
  }

  private fun access() { favoriteRecipeDatabaseHelper = FavoriteRecipeDatabaseHelper(AppContextHolder.context!!) }
  private fun close() { favoriteRecipeDatabaseHelper = null }
}