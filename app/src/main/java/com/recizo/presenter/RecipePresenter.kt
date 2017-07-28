package com.recizo.presenter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.recizo.model.ErrorCode
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.AppContextHolder
import com.recizo.module.CookpadScraper
import com.recizo.module.Scraper
import com.recizo.view.RecipeFragment


class RecipePresenter (recipeListView: RecyclerView, keywords: List<String>){
  private val scraper = CookpadScraper(keywords)
  private val recipeListAdapter = RecipeListAdapter(recipeListView)
  private var loadEventListener: LoadEventListener? = null
  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(object: RecipeListAdapter.OnItemClickListener {
      override fun onItemClick(recipe: CookpadRecipe) {
        AppContextHolder.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.cookpadLink)))
      }
    })
  }

  fun setLoadEventListener(listener: LoadEventListener) { loadEventListener = listener }

  fun startRecipeListCreate(){

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

  fun addRecipeList(recyclerView: android.support.v7.widget.RecyclerView?, dy: Int){
    if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
    val layoutManager = recyclerView!!.layoutManager as android.support.v7.widget.LinearLayoutManager
    val totalItemCount = layoutManager.itemCount
    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
    if (totalItemCount < lastVisibleItem + 5) {
      scraper.isLoading = true
      startRecipeListCreate()
    }
  }

  interface LoadEventListener{
    fun onLoadStart()
    fun onLoadEnd()
  }
}

