package com.recizo.presenter

import android.content.Intent
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.AppContextHolder

class FavoriteRecipePresenter(favoriteRecipeListView: RecyclerView, removeBtn: FloatingActionButton, undoBtn: FloatingActionButton) {
  private val favoriteRecipeAdapter = FavoriteRecipeAdapter(favoriteRecipeListView, removeBtn, undoBtn)
  init{
    favoriteRecipeListView.adapter = favoriteRecipeAdapter
    favoriteRecipeAdapter.viewFavoriteList()
  }

  fun onRemoveClicked() {
    favoriteRecipeAdapter.onDeleteClicked()
  }

  fun onUndoClicked() {
    favoriteRecipeAdapter.onUndoClicked()
  }
}