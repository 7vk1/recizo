package com.recizo.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.recizo.R
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.AppContextHolder
import java.net.URI
import java.net.URL
import java.util.concurrent.BlockingDeque

class FavoriteRecipePresenter(val context: Context, val view: View) {
  val removeBtn: FloatingActionButton = view.findViewById(R.id.favorite_recipe_remove_btn)
  val undoBtn: FloatingActionButton = view.findViewById(R.id.favorite_recipe_undo_btn)
  private val favoriteRecipeAdapter = FavoriteRecipeAdapter(
      object :FavoriteRecipePresenter.ChangeVisibility{
        override fun changeTextVisibility(visible: Boolean) {
          if(visible) {
            setErrorMesText(R.string.favorite_not_register_error_title, R.string.favorite_not_register_error_detail)
            view.findViewById<LinearLayout>(R.id.favorite_error_mes_box).visibility = View.VISIBLE
          }
          else view.findViewById<LinearLayout>(R.id.favorite_error_mes_box).visibility = View.INVISIBLE
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

  private fun setErrorMesText(title: Int, detail: Int) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    view.findViewById<TextView>(R.id.error_mes_detail).text = context.resources.getString(detail)
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