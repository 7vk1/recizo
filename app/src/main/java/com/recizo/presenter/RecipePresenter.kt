package com.recizo.presenter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.recizo.R
import com.recizo.model.ErrorCode
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.CookpadScraper
import com.recizo.module.Scraper
import com.recizo.view.RecipeFragment
import com.recizo.view.SearchItemView

class RecipePresenter (val context: Activity, recipeListView: RecyclerView, val keywords: Set<String>){
  private var scraper = CookpadScraper(keywords)
  private var loadEventListener: LoadEventListener? = null
  private val recipeListAdapter = RecipeListAdapter(recipeListView)
  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(object: RecipeListAdapter.OnItemClickListener {
      override fun onItemClick(recipe: CookpadRecipe) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.cookpadLink)))
      }
    })
  }

  fun setLoadEventListener(listener: LoadEventListener) { loadEventListener = listener }

  fun startRecipeListCreate() {
    loadEventListener?.onLoadStart()
    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
      override fun succeed(html: org.jsoup.nodes.Document?) {
        val recipes = scraper.requestGetRecipeItem(html)
        recipes.forEach {recipeListAdapter.addRecipe(it)}
        loadEventListener?.onLoadEnd()
      }
      override fun failed(errorCode: ErrorCode) {
        //todo impl
        android.util.Log.d("ERROR CODE", errorCode.toString())
        loadEventListener?.onLoadEnd()
      }
    })
  }

  fun displaySearchedText(parent: LinearLayout) {
    keywords.map {
      val view = SearchItemView(context ,it)
      view.setOnCloseClickListener(object : SearchItemView.OnCloseClickListener{
        override fun onClick(item: String) { eraseKeyword(item) }
      })
      parent.addView(view)
    }
  }

  fun addRecipeList(recyclerView: RecyclerView?, dy: Int){
    if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
    val layoutManager = recyclerView!!.layoutManager as android.support.v7.widget.LinearLayoutManager
    val totalItemCount = layoutManager.itemCount
    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
    if (totalItemCount < lastVisibleItem + 5) {
      scraper.isLoading = true
      startRecipeListCreate()
    }
  }

  private fun eraseKeyword(v: String) {
    val transaction = context.fragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    transaction.replace(R.id.fragment_frame, RecipeFragment(keywords.filter { it != v }.toSet()))
    transaction.commit()
  }

  interface LoadEventListener{
    fun onLoadStart()
    fun onLoadEnd()
  }
}

