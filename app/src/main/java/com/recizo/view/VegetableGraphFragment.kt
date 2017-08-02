package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.recizo.R
import com.recizo.module.RecizoApi
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
    val lineDataAdapter = VegetableGraphPresenter(chart)
    val adapter = VegetableAdapter(activity)
    spinner.adapter = adapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        lineDataAdapter.onItemChange(adapter.getItem(p2).name_jp)
      }
      override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
  }
}