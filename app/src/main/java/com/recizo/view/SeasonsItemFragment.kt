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
    recycleView = view!!.findViewById(R.id.season_list_view)
  }

  override fun onResume() {
    super.onResume()
    fun isCategory(name: String): Boolean {
      when(name) {
        "野菜" -> return true
        "魚貝" -> return true
        "キノコ" -> return true
        "果物" -> return true
        "その他" -> return true
        else -> return false
      }
    }

    val parse = XMLSeasonParser(resources)
    val seasonsList = parse.parseSeason()
    val seasonMonthList: MutableList<SeasonItem> = mutableListOf()
    var categoryCount: Int = -1
    seasonsList[position].map {
      if(isCategory(it)) {
        seasonMonthList.add(SeasonItem(category = it, item = mutableListOf()))
        categoryCount++
      } else seasonMonthList[categoryCount].item.add(it)
    }

    season_list_view.layoutManager = LinearLayoutManager(activity)
//    season_list_view.addItemDecoration(DividerItemDecoration(
//        season_list_view.context,
//        LinearLayoutManager(activity).orientation)
//    )
    recycleView!!.adapter = SeasonAdapter(AppContextHolder.context!!, seasonMonthList)
  }

  class SeasonItem(val category: String, val item: MutableList<String>)
}