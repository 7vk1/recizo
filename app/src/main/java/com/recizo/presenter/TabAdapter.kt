package com.recizo.presenter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.recizo.view.SeasonsItemFragment

class TabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
  override fun getCount(): Int {
    return 12
  }

  override fun getItem(position: Int): Fragment {
    return SeasonsItemFragment(position)
  }

  override fun getPageTitle(position: Int): CharSequence {
    return "${position + 1}月"
  }
}