package com.recizo.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.recizo.R
import com.recizo.module.AppContextHolder
import com.recizo.module.XMLSeasonParser
import com.recizo.presenter.SeasonAdapter
import kotlinx.android.synthetic.main.fragment_season_item.*
import kotlinx.android.synthetic.main.searched_recipe_list.*

class SeasonsItemFragment(val position: Int = 0): Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    val ret = inflater!!.inflate(R.layout.fragment_season_item, container, false)
    return ret
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // TODO 旬の食材
    val parse = XMLSeasonParser(resources)
    val seasonsList = parse.parseSeason()
    val monthList = seasonsList[position]


    val recycleView: RecyclerView? = view?.findViewById(R.id.season_list_view)

    season_list_view.layoutManager = LinearLayoutManager(activity)
    season_list_view.addItemDecoration(DividerItemDecoration(
        season_list_view.context,
        LinearLayoutManager(activity).orientation)
    )
    recycleView!!.adapter = SeasonAdapter(AppContextHolder.context!!, monthList)
  }
}