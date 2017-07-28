package com.recizo.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import kotlinx.android.synthetic.main.fragment_season_item.*

class SeasonsItemFragment(val position: Int = 0): Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    val ret = inflater!!.inflate(R.layout.fragment_season_item, container, false)
    return ret
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // TODO 旬の食材
    seasons_item_text.text = position.toString()
  }


}