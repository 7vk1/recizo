package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.MainActivity
import com.recizo.R
import com.recizo.presenter.TabAdapter
import kotlinx.android.synthetic.main.fragment_seasons.*
import java.util.*

class SeasonsFragment : Fragment(){
  override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_seasons, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fa = activity as FragmentActivity
    val month = Calendar.getInstance().get(Calendar.MONTH)
    season_tab.tabMode = TabLayout.MODE_SCROLLABLE
    seasons_viewpager.adapter = TabAdapter(fa.supportFragmentManager)
    seasons_viewpager.setCurrentItem(month, true)
    season_tab.setupWithViewPager(seasons_viewpager)
  }

  override fun onResume() {
    super.onResume()
    (activity as MainActivity).changeSelectedNavItem(MainActivity.NavMenuItems.season)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    seasons_viewpager.adapter = null
  }
}