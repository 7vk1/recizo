package com.recizo.presenter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.recizo.model.ErrorCode
import com.recizo.model.entity.ShufooFlyer
import com.recizo.module.Scraper
import com.recizo.module.ShufooScraper
import org.jsoup.nodes.Document
import android.content.Intent
import com.recizo.module.AppContextHolder


class FlyerPresenter (context: Context, flyerView: RecyclerView, keywords: String){
  private val scraper: ShufooScraper = ShufooScraper(keywords)
  private var flyerListAdapter = FlyerListAdapter(flyerView)
  private var progressBarCallback: IProgressBar? = null

  init {
    flyerView.adapter = flyerListAdapter
    flyerListAdapter.setOnItemClickListener(object: FlyerListAdapter.OnItemClickListener{
      override fun onItemClick(flyer: ShufooFlyer) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(flyer.shufooLink)))
      }
    })
  }

  fun setProgressBar(progressBar: IProgressBar) {
    progressBarCallback = progressBar
  }

  fun startFlyerListCreate(){
    progressBarCallback?.showProgressBar()
    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
      override fun succeed(html: Document?) {
        val flyers = scraper.requestGetShufooItem(html)
        flyers.forEach {flyerListAdapter.addFlyer(it)}
        progressBarCallback?.hideProgressBar()
      }
      override fun failed(errorCode: ErrorCode) {
        Log.d("TEST ERROR CODE", errorCode.toString())
        progressBarCallback?.hideProgressBar()
      }
    })
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

  interface IProgressBar{
    fun showProgressBar()
    fun hideProgressBar()
  }
}

