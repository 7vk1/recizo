package com.recizo.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.module.AppContextHolder
import com.recizo.module.XMLSeasonParser
import com.recizo.presenter.SeasonAdapter
import kotlinx.android.synthetic.main.fragment_season_item.*

class SeasonsItemFragment(val position: Int = 0): Fragment() {
  var recycleView: RecyclerView? = null

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_season_item, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recycleView = view?.findViewById(R.id.season_list_view)
  }

  override fun onResume() {
    super.onResume()
    val parse = XMLSeasonParser(resources)
    val seasonsList = parse.parseSeason()
    val monthList = seasonsList[position]

    season_list_view.layoutManager = LinearLayoutManager(activity)
    season_list_view.addItemDecoration(DividerItemDecoration(
        season_list_view.context,
        LinearLayoutManager(activity).orientation)
    )
    recycleView!!.adapter = SeasonAdapter(AppContextHolder.context!!, monthList)
  }
}