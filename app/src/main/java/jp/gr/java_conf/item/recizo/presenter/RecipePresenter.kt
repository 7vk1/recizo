package jp.gr.java_conf.item.recizo.presenter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import jp.gr.java_conf.item.recizo.model.entity.CookpadRecipe
import jp.gr.java_conf.item.recizo.model.entity.ShufooFlyer
import jp.gr.java_conf.item.recizo.module.CookpadScraper
import jp.gr.java_conf.item.recizo.module.ShufooScraper
import org.jsoup.nodes.Document


class RecipePresenter (val scraper: CookpadScraper){
  interface IRecipeFragment{
    fun showProgress()
    fun dismissProgress()
    fun setResultToList(recipe: CookpadRecipe)
  }

  private var recipeView:IRecipeFragment? = null

  fun setView(view: IRecipeFragment){
    recipeView = view
  }

  fun startFlyerListCreate(){
    recipeView!!.showProgress()
    addFlyerListToAdaptor()
  }

  fun addFlyerList(recyclerView: RecyclerView?, dy: Int){
    if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
    val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
    val totalItemCount = layoutManager.itemCount
    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
    if (totalItemCount < lastVisibleItem + 5) {
      scraper.isLoading = true
      startFlyerListCreate()
    }
  }

  private fun addFlyerListToAdaptor(){
    scraper.scrapingHTML(object : CookpadCallBack {
      override fun succeed(html: Document?) {
        val recipes = scraper.requestGetRecipeItem(html)
        recipes.forEach {
          recipeView!!.setResultToList(it)
        }
        recipeView!!.dismissProgress()
      }
      override fun failed(errorCode: ErrorCode) {
        Log.d("TEST ERROR CODE", errorCode.toString())
        recipeView!!.dismissProgress()
      }
    })
  }
}

