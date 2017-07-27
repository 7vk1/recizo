package com.recizo.presenter

import com.recizo.model.ErrorCode
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.CookpadScraper
import com.recizo.module.Scraper


class RecipePresenter (val scraper: CookpadScraper){
  interface IRecipeFragment{
    fun showProgress()
    fun dismissProgress()
    fun setResultToList(recipe: CookpadRecipe)
  }

  private var recipeView: RecipePresenter.IRecipeFragment? = null

  fun setView(view: RecipePresenter.IRecipeFragment){
    recipeView = view
  }

  fun startFlyerListCreate(){
    recipeView!!.showProgress()
    addFlyerListToAdaptor()
  }

  fun addFlyerList(recyclerView: android.support.v7.widget.RecyclerView?, dy: Int){
    if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
    val layoutManager = recyclerView!!.layoutManager as android.support.v7.widget.LinearLayoutManager
    val totalItemCount = layoutManager.itemCount
    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
    if (totalItemCount < lastVisibleItem + 5) {
      scraper.isLoading = true
      startFlyerListCreate()
    }
  }

  private fun addFlyerListToAdaptor(){
    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
      override fun succeed(html: org.jsoup.nodes.Document?) {
        val recipes = scraper.requestGetRecipeItem(html)
        recipes.forEach {
          recipeView!!.setResultToList(it)
        }
        recipeView!!.dismissProgress()
      }
      override fun failed(errorCode: ErrorCode) {
        android.util.Log.d("TEST ERROR CODE", errorCode.toString())
        recipeView!!.dismissProgress()
      }
    })
  }
}

