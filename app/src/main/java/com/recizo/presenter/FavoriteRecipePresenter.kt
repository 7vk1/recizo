package com.recizo.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.recizo.R
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.AppContextHolder
import java.net.URI
import java.net.URL
import java.util.concurrent.BlockingDeque

class FavoriteRecipePresenter(context: Context, val view: View) {
  val removeBtn: FloatingActionButton = view.findViewById(R.id.favorite_recipe_remove_btn)
  val undoBtn: FloatingActionButton = view.findViewById(R.id.favorite_recipe_undo_btn)
  val emptyText: TextView = view.findViewById(R.id.empty_text)
  private val favoriteRecipeAdapter = FavoriteRecipeAdapter(
      object :FavoriteRecipePresenter.ChangeVisibility{
        override fun changeTextVisibility(visible: Boolean) {
          if(visible) emptyText.visibility = View.VISIBLE else emptyText.visibility = View.INVISIBLE
        }

        override fun changeButtonVisibility(remove: Boolean, undo: Boolean) {
          if(remove) removeBtn.visibility = View.VISIBLE else removeBtn.visibility = View.INVISIBLE
          if(undo) undoBtn.visibility = View.VISIBLE else undoBtn.visibility = View.INVISIBLE
        }
      },
      object: Error {
        override fun failedGetImage() {
          Toast.makeText(context, "画像の取得に失敗しました", Toast.LENGTH_SHORT).show()
        }
      },
      object: Intent {
        override fun onRecipe(url: Uri) {
          context.startActivity(Intent(android.content.Intent.ACTION_VIEW, url))
        }
      })
  init{
    favoriteRecipeAdapter.viewFavoriteList()
    view.findViewById<RecyclerView>(R.id.fragment_favorite_recipe_frame).adapter = favoriteRecipeAdapter
  }

  fun setUpView() {
    removeBtn.setOnClickListener { favoriteRecipeAdapter.onDeleteClicked() }
    undoBtn.setOnClickListener { favoriteRecipeAdapter.onUndoClicked() }
  }

  interface ChangeVisibility {
    fun changeTextVisibility(visible: Boolean)
    fun changeButtonVisibility(remove: Boolean, undo: Boolean)
  }

  interface Error{
    fun failedGetImage()
  }

  interface Intent {
    fun onRecipe(url: Uri)
  }
}