package com.recizo.presenter

import android.support.v7.widget.RecyclerView

class FavoriteRecipePresenter(favoriteRecipeListView: RecyclerView) {
  private val favoriteRecipeAdapter = FavoriteRecipeAdapter()
  init{
    favoriteRecipeListView.adapter = favoriteRecipeAdapter
    favoriteRecipeAdapter.viewFavoriteList()
  }
}