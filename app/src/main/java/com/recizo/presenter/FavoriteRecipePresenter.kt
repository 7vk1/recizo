package com.recizo.presenter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.AppContextHolder

class FavoriteRecipePresenter(favoriteRecipeListView: RecyclerView) {
  private val favoriteRecipeAdapter = FavoriteRecipeAdapter(favoriteRecipeListView)
  init{
    favoriteRecipeListView.adapter = favoriteRecipeAdapter
    favoriteRecipeAdapter.viewFavoriteList()
    favoriteRecipeAdapter.setOnItemClickListener(object: FavoriteRecipeAdapter.OnItemClickListener {
      override fun onItemClick(recipe: CookpadRecipe) {
        Log.d("TEST", "IS CLICKED")
        AppContextHolder.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.cookpadLink)))
      }
    })
  }
}