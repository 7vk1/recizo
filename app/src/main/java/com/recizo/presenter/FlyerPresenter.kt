package com.recizo.presenter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.recizo.model.ErrorCode
import com.recizo.model.entity.ShufooFlyer
import com.recizo.module.Scraper
import com.recizo.module.ShufooScraper
import org.jsoup.nodes.Document


class FlyerPresenter (val scraper: ShufooScraper){
  interface IFlyerFragment{
    fun showProgress()
    fun dismissProgress()
    fun setResultToList(flyer: ShufooFlyer)
  }

  private var flyerView: IFlyerFragment? = null

  fun setView(view: IFlyerFragment){
    flyerView = view
  }

  fun startFlyerListCreate(){
    flyerView!!.showProgress()
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
    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
      override fun succeed(html: Document?) {
        val flyers = scraper.requestGetShufooItem(html)
        flyers.forEach {
          flyerView!!.setResultToList(it)
        }
        flyerView!!.dismissProgress()
      }
      override fun failed(errorCode: ErrorCode) {
        Log.d("TEST ERROR CODE", errorCode.toString())
        flyerView!!.dismissProgress()
      }
    })
  }
}

