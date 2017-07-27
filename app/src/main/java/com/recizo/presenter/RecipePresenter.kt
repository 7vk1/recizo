package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.recizo.model.ErrorCode
import com.recizo.model.viewholder.RecipeViewHolder
import com.recizo.module.CookpadScraper
import com.recizo.module.Scraper

class RecipePresenter (recipeListView: RecyclerView, keywords: List<String>){
  private val scraper = CookpadScraper(keywords)
  private val recipeListAdapter = RecipeListAdapter()
  private var progressBarCallback: IProgressBar? = null

  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(View.OnClickListener {
      Log.d("hoeghoge",(it as RecipeViewHolder).linkUrl)
      println("aaaaaa")
    })
  }

  fun setProgressBar(progressBar: IProgressBar) {
    progressBarCallback = progressBar
  }

  fun startRecipeListCreate(){

    progressBarCallback?.showProgressBar()
    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
      override fun succeed(html: org.jsoup.nodes.Document?) {
        val recipes = scraper.requestGetRecipeItem(html)
        recipes.forEach {recipeListAdapter.addRecipe(it)}
        progressBarCallback?.hideProgressBar()
      }
      override fun failed(errorCode: ErrorCode) {
        android.util.Log.d("TEST ERROR CODE", errorCode.toString())
        progressBarCallback?.hideProgressBar()
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

  interface IProgressBar{
    fun showProgressBar()
    fun hideProgressBar()
  }
}

