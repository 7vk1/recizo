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
import android.graphics.Color
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.recizo.R
import kotlinx.android.synthetic.main.fragment_flyer.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class FlyerPresenter (val context: Context,val view: View,val keywords: String){
  private var flyerListAdapter = FlyerListAdapter(view.findViewById(R.id.flyer_recyclerView))
  private var progressBarCallback: IProgressBar? = null
  private val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.flyer_swipe_refresh_layout)
  private val scraper: ShufooScraper = ShufooScraper(keywords.replace("-",""))
  init {
    view.findViewById<RecyclerView>(R.id.flyer_recyclerView).adapter = flyerListAdapter
    swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9137"))
    swipeRefreshLayout.setOnRefreshListener(flyerListAdapter)
    flyerListAdapter.setOnItemClickListener(object: FlyerListAdapter.OnItemClickListener{
      override fun onItemClick(flyer: ShufooFlyer) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(flyer.shufooLink)))
      }
    })
    flyerListAdapter.setOnRefreshPullListener(object: FlyerListAdapter.OnRefreshPullListner {
      override fun onRefreshPull() {
        fun prepareRefreshProgressBar() {
          setProgressBar(object: IProgressBar {
            override fun hideProgressBar() {
              fun restoreProgressBar() {
                setProgressBar(object: IProgressBar {
                  override fun hideProgressBar() { view.findViewById<ProgressBar>(R.id.searched_shufoo_progressBar).visibility = View.GONE }
                  override fun showProgressBar() { view.findViewById<ProgressBar>(R.id.searched_shufoo_progressBar).visibility = View.VISIBLE }
                })
              }

              swipeRefreshLayout.isRefreshing = false
              restoreProgressBar()
            }
            override fun showProgressBar() {}
          })
        }
        prepareRefreshProgressBar()
        scraper.pageInit()
        startFlyerListCreate()
      }
    })
  }

  fun setProgressBar(progressBar: IProgressBar) { progressBarCallback = progressBar }

  fun startFlyerListCreate() {
    if (keywords != "" && keywords.isNotEmpty() && keywords != "0") {
      val errorMes = view.findViewById<LinearLayout>(R.id.error_mes_box)
      errorMes.visibility = View.INVISIBLE
      progressBarCallback?.showProgressBar()
      scraper.scrapingHTML(object : Scraper.ScraperCallBack {
        override fun succeed(html: Document?) {
          val flyers = scraper.requestGetShufooItem(html)
          flyers.forEach { flyerListAdapter.addFlyer(it) }
          progressBarCallback?.hideProgressBar()
        }

        override fun failed(errorCode: ErrorCode) {
          if(errorCode.name == ErrorCode.IO_ERROR.name) setErrorMesText(R.string.network_error_title, R.string.network_error_detail)
          else if(errorCode.name == ErrorCode.INDEX_OUT_OF_BOUNDS_ERROR.name) setErrorMesText(R.string.flyer_notfound_title, R.string.flyer_notfound_detail)
          else setErrorMesText(R.string.other_error_title, R.string.other_error_detail)
          clearFlyerList()
          errorMes.visibility = View.VISIBLE
          progressBarCallback?.hideProgressBar()
        }
      })
    } else setErrorMesText(R.string.flyer_empty_text, R.string.flyer_empty_detail)
  }

  private fun setErrorMesText(title: Int, detail: Int) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    view.findViewById<TextView>(R.id.error_mes_detail).text = context.resources.getString(detail)
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

  private fun clearFlyerList() {
    flyerListAdapter.clearFlyer()
  }

  interface IProgressBar{
    fun showProgressBar()
    fun hideProgressBar()
  }
}

