package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import com.recizo.model.ErrorCode
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.CookpadScraper
import com.recizo.module.Scraper

class RecipePresenter (recipeListView: RecyclerView, keywords: List<String>){
  private val scraper = CookpadScraper(keywords)
  private val recipeListAdapter = RecipeListAdapter(recipeListView)
  private var progressBarCallback: IProgressBar? = null

  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(object: RecipeListAdapter.OnItemClickListener {
      override fun onItemClick(recipe: CookpadRecipe) {
        println(recipe.cookpadLink)//todo impl
      }
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
        //todo impl
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

