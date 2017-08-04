package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.recizo.MainActivity
import com.recizo.R
import com.recizo.module.ErrorMessageCreator
import com.recizo.module.Http
import com.recizo.presenter.VegetableAdapter
import kotlinx.android.synthetic.main.fragment_vegetable_graph.*
import com.recizo.presenter.VegetableGraphPresenter

class VegetableGraphFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_vegetable_graph, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val graphPresenter = VegetableGraphPresenter(chart)
    val adapter = VegetableAdapter(activity)
    async_wrap.retryMessage = "再接続"
    async_wrap.setOnClickListener {  }
    async_wrap.onRetryClickListener = object : AsyncWrapLayout.RetryClickListener {
      override fun onRetryClicked() {
        graphPresenter.onItemChange(adapter.getItem(spinner.selectedItemPosition).name_jp)
      }
    }
    graphPresenter.listener = object : VegetableGraphPresenter.LoadEventListener {
      override fun onLoadStart() {
        async_wrap?.hideError()
        async_wrap?.showProgressbar()
      }
      override fun onLoadEnd() { async_wrap?.hideProgressbar() }
      override fun onError(code: Http.ErrorCode) {
        async_wrap?.hideProgressbar()
        async_wrap?.onError(ErrorMessageCreator.create(code))
      }
    }
    spinner.adapter = adapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        graphPresenter.onItemChange(adapter.getItem(p2).name_jp)
      }
      override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
  }

  override fun onResume() {
    super.onResume()
    (activity as MainActivity).changeSelectedNavItem(MainActivity.NavMenuItems.market_price)
  }
}