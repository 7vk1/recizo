package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.MainActivity
import com.recizo.R
import com.recizo.presenter.FavoriteRecipePresenter
import kotlinx.android.synthetic.main.fragment_favorite_recipe.*

class FavoriteRecipeFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_favorite_recipe, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fragment_favorite_recipe_frame.layoutManager = LinearLayoutManager(activity)
    val presenter =  FavoriteRecipePresenter(activity, view!!)
    presenter.setUpView()
  }

  override fun onResume() {
    super.onResume()
    (activity as MainActivity).changeSelectedNavItem(MainActivity.NavMenuItems.favorite_recipe)
  }
}