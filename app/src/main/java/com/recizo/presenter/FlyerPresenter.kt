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
import android.widget.TextView
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
      val errorMes = view.findViewById<LinearLayout>(R.id.flyer_error_mes_box)
      errorMes.visibility = View.INVISIBLE
      progressBarCallback?.showProgressBar()
      scraper.scrapingHTML(object : Scraper.ScraperCallBack {
        override fun succeed(html: Document?) {
          val flyers = scraper.requestGetShufooItem(html)
          flyers.forEach { flyerListAdapter.addFlyer(it) }
          progressBarCallback?.hideProgressBar()
        }

        override fun failed(errorCode: ErrorCode) {
          if(errorCode.name == ErrorCode.IO_ERROR.name) Toast.makeText(context, "ネットワークエラー", Toast.LENGTH_SHORT).show()
          else if(errorCode.name == ErrorCode.INDEX_OUT_OF_BOUNDS_ERROR.name) {
            setErrorMesText(R.string.flyer_notfound_title, R.string.flyer_notfound_detail)
            errorMes.visibility = View.VISIBLE
          }
          else Toast.makeText(context, "予期せぬエラー", Toast.LENGTH_SHORT).show()
          progressBarCallback?.hideProgressBar()
        }
      })
    } else setErrorMesText(R.string.flyer_empty_text, R.string.flyer_empty_detail)
  }

  fun setErrorMesText(title: String, detail: String) {
    view.findViewById<TextView>(R.id.flyer_error_mes_title).text = title
    view.findViewById<TextView>(R.id.flyer_error_mes_detail).text = detail
  }

  fun setErrorMesText(title: Int, detail: Int) {
    view.findViewById<TextView>(R.id.flyer_error_mes_title).text = context.resources.getString(title)
    view.findViewById<TextView>(R.id.flyer_error_mes_detail).text = context.resources.getString(detail)
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

