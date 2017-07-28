package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*

class IceboxFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    IceboxAdapter.setContext(activity)
    IceboxAdapter.setView(view)
    setUpViews()
  }


  fun setUpViews() {
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = IceboxAdapter
    IceboxAdapter.initItem()
  }


}
