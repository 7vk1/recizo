package com.recizo.view

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.presenter.FlyerPresenter
import com.recizo.R
import com.recizo.module.AppContextHolder
import kotlinx.android.synthetic.main.fragment_flyer.*

class FlyerFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_flyer, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    flyer_recyclerView.layoutManager = LinearLayoutManager(activity)
//    flyer_recyclerView.addItemDecoration(DividerItemDecoration(
//        flyer_recyclerView.context,
//            LinearLayoutManager(activity).orientation)
//    )
    val flyerPresenter = FlyerPresenter(
            activity,
            view!!,
            PreferenceManager.getDefaultSharedPreferences(AppContextHolder.context).getString("edit_postcode_key", "")
    )
    searched_shufoo_progressBar.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY)
    flyerPresenter.setProgressBar(object:FlyerPresenter.IProgressBar{
      override fun showProgressBar() {
        searched_shufoo_progressBar?.visibility = View.VISIBLE
      }
      override fun hideProgressBar() {
        searched_shufoo_progressBar?.visibility = View.GONE
      }
    })
    flyerPresenter.startFlyerListCreate()
    flyer_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        flyerPresenter.addFlyerList(recyclerView, dy)
      }
    })
  }
}
