package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.recizo.MainActivity
import com.recizo.R
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
    graphPresenter.listener = object : VegetableGraphPresenter.EventListener {
      override fun onStart() {
        error_text?.visibility = View.INVISIBLE
        progressBar?.visibility = View.VISIBLE }
      override fun onEnd() { progressBar?.visibility = View.INVISIBLE }
      override fun onError(code: Http.ErrorCode) {
        fun fromHtml(title: String, message: String): Spanned {
          val text = "<font color=#ff0000><big>$title</big></font><br><br><small>$message</small>"
          return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY)
          } else Html.fromHtml(text)
        }
        error_text?.text = when(code) {
          Http.ErrorCode.CONNECTION_ERROR -> fromHtml("ネット接続エラー", "ネットワークに接続できませんでした")
          else -> fromHtml("サーバーエラー", "しばらく時間をおいてからもう一度アクセスしてください")
        }
        error_text.visibility = View.VISIBLE
      }
    }
    val adapter = VegetableAdapter(activity)
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