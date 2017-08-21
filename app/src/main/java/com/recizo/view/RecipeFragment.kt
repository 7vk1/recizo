package com.recizo.view

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.MainActivity
import com.recizo.R
import com.recizo.presenter.RecipePresenter
import kotlinx.android.synthetic.main.searched_recipe_list.*

class RecipeFragment(val items: Set<String> = setOf()) : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.searched_recipe_list, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val recipePresenter = RecipePresenter(activity, view!!, items)

    (activity as MainActivity).onBackPressedListener = BaseBackPressedListener(activity as FragmentActivity)

    searched_recyclerView.layoutManager = LinearLayoutManager(activity)


    searched_recipe_progressBar.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY)
    recipePresenter.setLoadEventListener(object : RecipePresenter.LoadEventListener {
      override fun onLoadStart() { searched_recipe_progressBar?.visibility = View.VISIBLE }
      override fun onLoadEnd() { searched_recipe_progressBar?.visibility = View.INVISIBLE }
    })
    recipePresenter.startRecipeListCreate()
    recipePresenter.displaySearchedText(searche_result_keyword_flame)

    searched_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        recipePresenter.addRecipeList(recyclerView, dy)
      }
    })
  }

  override fun onResume() {
    super.onResume()
    (activity as MainActivity).changeSelectedNavItem(MainActivity.NavMenuItems.icebox) // todo is ok
  }

  override fun onDestroyView() {
    super.onDestroyView()
    (activity as MainActivity).onBackPressedListener = null
  }


  interface OnBackPressedListener { fun doBack() }

  class BaseBackPressedListener(private val activity: FragmentActivity): OnBackPressedListener {
    override fun doBack() { activity.fragmentManager.popBackStack("icebox", 0) }
  }
}