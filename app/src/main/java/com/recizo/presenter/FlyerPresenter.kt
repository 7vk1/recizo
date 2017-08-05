package com.recizo.presenter

import android.app.Activity
import android.app.Fragment
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
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.recizo.R
import android.text.Spanned
import android.text.method.LinkMovementMethod
import com.recizo.view.SettingFragment


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
    System.out.println("POSTCODE CREATE")
    val errorMes = view.findViewById<LinearLayout>(R.id.error_mes_box)
    if (keywords != "" && keywords.isNotEmpty() && keywords != "0") {
      System.out.println("POSTCODE INPUT OK")
      errorMes.visibility = View.INVISIBLE
      progressBarCallback?.showProgressBar()
      scraper.scrapingHTML(object : Scraper.ScraperCallBack {
        override fun succeed(html: Document?) {
          System.out.println("POSTCODE SUCCEED")
          val flyers = scraper.requestGetShufooItem(html)
          flyers.forEach { flyerListAdapter.addFlyer(it) }
          progressBarCallback?.hideProgressBar()
        }

        override fun failed(errorCode: ErrorCode) {
          System.out.println("POSTCODE FAILED")
          if(errorCode.name == ErrorCode.IO_ERROR.name) setErrorMesText(R.string.network_error_title, R.string.network_error_detail)
          else if(errorCode.name == ErrorCode.INDEX_OUT_OF_BOUNDS_ERROR.name) {
            setErrorMesText(R.string.flyer_notfound_title, "郵便番号の設定は", createSpannableString("「設定」"), "から行えます")
            view.findViewById<TextView>(R.id.error_mes_detail).movementMethod = LinkMovementMethod.getInstance()
          }
          else setErrorMesText(R.string.other_error_title, R.string.other_error_detail)
          clearFlyerList()
          errorMes.visibility = View.VISIBLE
          progressBarCallback?.hideProgressBar()
        }
      })
    } else {
      setErrorMesText(R.string.flyer_empty_text, "郵便番号の設定は", createSpannableString("「設定」"), "から行えます")
      view.findViewById<TextView>(R.id.error_mes_detail).movementMethod = LinkMovementMethod.getInstance()
      errorMes.visibility = View.VISIBLE
    }
  }

  private fun changeFragment(fragment: Fragment) {
    context as Activity
    val transaction = context.fragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    transaction.replace(R.id.fragment_frame, fragment)
    transaction.commit()
  }

  private fun createSpannableString(text: String): SpannableString {
    val link = SpannableString(text)

    link.setSpan(object : ClickableSpan() {
      override fun onClick(textView: View) {
        changeFragment(SettingFragment())
      }
    }, 1, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return link
  }

  private fun setErrorMesText(title: Int, detail: Int) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    view.findViewById<TextView>(R.id.error_mes_detail).text = context.resources.getString(detail)
  }

  private fun setErrorMesText(title: Int, detailBefore: String, link: SpannableString, detailAfter: String) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    val errorDetail = view.findViewById<TextView>(R.id.error_mes_detail)
    errorDetail.text = detailBefore
    errorDetail.append(link)
    errorDetail.append(detailAfter)
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

