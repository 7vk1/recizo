package com.recizo.view


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.contract.CookpadCallBack
import com.recizo.contract.ProgressBarCallBack
import com.recizo.model.ErrorCode
import com.recizo.model.entity.ShufooFlyer
import com.recizo.module.ShufooScraper
import com.recizo.presenter.FlyerListAdapter
import kotlinx.android.synthetic.main.searched_recipe_list.*
import org.jsoup.nodes.Document
import com.recizo.module.ShufooScraper.Flyer.*



class FlyerFragment : Fragment(){
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.searched_recipe_list, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }
  override fun onStart() {
    super.onStart()
    searched_recyclerView.layoutManager = LinearLayoutManager(activity)
    val flyerListAdapter = FlyerListAdapter()
    val dividerItemDecoration = DividerItemDecoration(searched_recyclerView.context,
            LinearLayoutManager(activity).orientation)
    searched_recyclerView.addItemDecoration(dividerItemDecoration)
    searched_recyclerView.adapter = flyerListAdapter


    val test = ShufooScraper("1690074")

    test.scraping(object: ProgressBarCallBack {
      override fun progressBarStart() {
        searched_recipe_progressBar.visibility = View.VISIBLE
      }

      override fun progressBarStop() {
        searched_recipe_progressBar.visibility = View.GONE
      }
    }, object : CookpadCallBack {
      override fun succeed(html: Document?) {
        val flyers = test.requestGetShufooItem(html)
        flyers.forEach {
          flyerListAdapter.addFlyer(it)
        }
      }
      override fun failed(errorCode: ErrorCode) {
        Log.d("TEST ERROR CODE", errorCode.toString())
      }
    })
  }
}
