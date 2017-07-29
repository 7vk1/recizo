package com.recizo.view


import android.content.Context
import android.content.Intent
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
  var recycleView: RecyclerView? = null

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    val ret = inflater!!.inflate(R.layout.fragment_season_item, container, false)
    Log.d("SEASON FRAGMENT", "ON CREATE VIEW")
    return ret
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recycleView = view?.findViewById(R.id.season_list_view)
    Log.d("SEASON FRAGMENT", "ON VIEW CREATED")
  }

  override fun onResume() {
    Log.d("SEASON FRAGMENT", "ON RESUME")
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

  override fun onAttach(context: Context?) {
    Log.d("SEASON FRAGMENT", "ON ATTACH")
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    Log.d("SEASON FRAGMENT", "ON ACTIVITY CREATED")
    super.onActivityCreated(savedInstanceState)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Log.d("SEASON FRAGMENT", "ON ACTIVITY RESULT")
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onAttachFragment(childFragment: Fragment?) {
    Log.d("SEASON FRAGMENT", "ON ATTACH FRAGMENT")
    super.onAttachFragment(childFragment)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    Log.d("SEASON FRAGMENT", "ON CREATE")
    super.onCreate(savedInstanceState)
  }

  override fun onDetach() {
    Log.d("SEASON FRAGMENT", "ON DETACH")
    super.onDetach()
  }

  override fun onDestroyView() {
    Log.d("SEASON FRAGMENT", "ON DESTROY VIEW")
    super.onDestroyView()
  }

  override fun onDestroy() {
    Log.d("SEASON FRAGMENT", "ON DESTROY")
    super.onDestroy()
  }
}