package com.recizo.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.recizo.R
import com.recizo.module.AppContextHolder
import com.recizo.presenter.SeasonAdapter
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
    val listView: ListView? = view?.findViewById(R.id.season_list_view)

    val mocklist = listOf(" 野菜","せり","なずな","ゴギョウ","魚介類","ハコベラ","ホトケノザ")
    // TODO XMLparserの結果をListで受け取る
    val seasonsList = listOf(
        listOf("せり"), // 1月
        listOf("ナズナ"), // 2月
        listOf("ゴギョウ"), // 3月
        listOf("ハコベラ"), // 4月
        listOf("ホトケノザ") // 5月
    )

    val monthList = seasonsList[position]

    val adapter = SeasonAdapter(AppContextHolder.context!!, R.layout.season_list_item, monthList)
    listView!!.adapter = adapter
    Log.d("_TEST", "SEASON")
  }
}