package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.model.entity.ShufooFlyer
import com.recizo.module.ShufooScraper
import com.recizo.presenter.FlyerListAdapter
import com.recizo.presenter.FlyerPresenter
import kotlinx.android.synthetic.main.searched_recipe_list.*
import com.recizo.R

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
    searched_recyclerView.layoutManager = LinearLayoutManager(activity)
    searched_recyclerView.addItemDecoration(DividerItemDecoration(
            searched_recyclerView.context,
            LinearLayoutManager(activity).orientation)
    )

    val flyerPresenter = FlyerPresenter(this, searched_recyclerView, "1690074")
    flyerPresenter.setProgressBar(object:FlyerPresenter.IProgressBar{
      override fun showProgressBar() {
        searched_recipe_progressBar.visibility = View.VISIBLE
      }
      override fun hideProgressBar() {
        searched_recipe_progressBar.visibility = View.GONE
      }
    })
    flyerPresenter.startFlyerListCreate()

    searched_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        flyerPresenter.addFlyerList(recyclerView, dy)
      }
    })
  }
}
