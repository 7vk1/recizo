package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.recizo.R
import com.recizo.contract.CookpadCallBack
import com.recizo.model.ErrorCode
import com.recizo.model.viewholder.RecipeViewHolder
import com.recizo.module.CookpadScraper
import com.recizo.presenter.RecipeListAdapter
import kotlinx.android.synthetic.main.searched_recipe_list.*
import org.jsoup.nodes.Document


class SearchedRecipeFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.searched_recipe_list, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onStart() {
    super.onStart()

    searched_recyclerView.layoutManager = LinearLayoutManager(activity)
    val recipeListAdapter = RecipeListAdapter()
    val dividerItemDecoration = DividerItemDecoration(searched_recyclerView.context,
        LinearLayoutManager(activity).orientation)
    searched_recyclerView.addItemDecoration(dividerItemDecoration)
    searched_recyclerView.adapter = recipeListAdapter

    val scraper = CookpadScraper(listOf("鹿","トマト"))

    addRecipeListToAdaptor(scraper, recipeListAdapter)

    searched_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
        if (totalItemCount < lastVisibleItem + 5) {
          scraper.isLoading = true
          searched_recipe_progressBar.visibility = View.VISIBLE
          addRecipeListToAdaptor(scraper, recipeListAdapter)
        }
      }
    })
  }

  private fun addRecipeListToAdaptor(scraper: CookpadScraper, recipeListAdapter: RecipeListAdapter){
    scraper.scraping(object : CookpadCallBack {
      override fun succeed(html: Document?) {
        val recipes = scraper.requestGetRecipeItem(html)
        recipes.forEach {
          recipeListAdapter.addRecipe(it)
        }
        searched_recipe_progressBar.visibility = View.GONE
      }
      override fun failed(errorCode: ErrorCode) {
        Log.d("TEST ERROR CODE", errorCode.toString())
        searched_recipe_progressBar.visibility = View.GONE
      }
    })
  }
}