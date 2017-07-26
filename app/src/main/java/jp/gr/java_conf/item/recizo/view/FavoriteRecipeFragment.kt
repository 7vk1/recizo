package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.presenter.FavoriteRecipeAdapter
import kotlinx.android.synthetic.main.fragment_favorite_recipe.*

class FavoriteRecipeFragment: Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    Log.d("TEST CREATE FRAG", "FAVORITE RECIPE")
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_favorite_recipe, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    fragment_favorite_recipe_frame.layoutManager = LinearLayoutManager(activity)
    val favoriteRecipeAdapter = FavoriteRecipeAdapter()
    fragment_favorite_recipe_frame.adapter = favoriteRecipeAdapter
    favoriteRecipeAdapter.viewFavoriteList()
  }
}