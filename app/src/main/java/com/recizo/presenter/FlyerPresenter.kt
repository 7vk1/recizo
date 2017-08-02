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
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.recizo.R

class FlyerPresenter (val context: Context,val view: View,val keywords: String){
  private var flyerListAdapter = FlyerListAdapter(view.findViewById(R.id.flyer_recyclerView))
  private var progressBarCallback: IProgressBar? = null
  private val scraper: ShufooScraper = ShufooScraper(keywords)
  init {
    view.findViewById<RecyclerView>(R.id.flyer_recyclerView).adapter = flyerListAdapter
    flyerListAdapter.setOnItemClickListener(object: FlyerListAdapter.OnItemClickListener{
      override fun onItemClick(flyer: ShufooFlyer) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(flyer.shufooLink)))
      }
    })
  }

  fun setProgressBar(progressBar: IProgressBar) {
    progressBarCallback = progressBar
  }

  fun startFlyerListCreate() {
    if (keywords != "" && keywords.isNotEmpty() && keywords != "0") {
      view.findViewById<LinearLayout>(R.id.flyer_empty_text_box).visibility = View.INVISIBLE
      progressBarCallback?.showProgressBar()
      scraper.scrapingHTML(object : Scraper.ScraperCallBack {
        override fun succeed(html: Document?) {
          val flyers = scraper.requestGetShufooItem(html)
          flyers.forEach { flyerListAdapter.addFlyer(it) }
          progressBarCallback?.hideProgressBar()
        }

        override fun failed(errorCode: ErrorCode) {
          if(errorCode.name == ErrorCode.IO_ERROR.name) Toast.makeText(context, "ネットワークエラー", Toast.LENGTH_SHORT).show()
          else if(errorCode.name == ErrorCode.INDEX_OUT_OF_BOUNDS_ERROR.name) Toast.makeText(context, "郵便番号を発見できませんでした", Toast.LENGTH_SHORT).show()
          else Toast.makeText(context, "予期せぬエラー", Toast.LENGTH_SHORT).show()
          progressBarCallback?.hideProgressBar()
        }
      })
    }
  }

  fun addFlyerList(recyclerView: RecyclerView?, dy: Int) {
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

