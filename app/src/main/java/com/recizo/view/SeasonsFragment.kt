package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.presenter.TabAdapter
import kotlinx.android.synthetic.main.fragment_seasons.*

class SeasonsFragment : Fragment(){
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_seasons, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fa = activity as FragmentActivity
    _tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    seasons_viewpager.adapter = TabAdapter(fa.supportFragmentManager)
    _tabLayout.setupWithViewPager(seasons_viewpager)
  }

}