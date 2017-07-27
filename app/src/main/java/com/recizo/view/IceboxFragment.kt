package com.recizo.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.RegisterActivity
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*

class IceboxFragment() : Fragment() {
  var searchTargetList = mutableListOf<String>()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    add.setOnClickListener {
      startActivity(Intent(activity, RegisterActivity::class.java) )
    }
    undo.setOnClickListener {
      searchTargetList.clear()
      undo.visibility = View.GONE
      add.setImageResource(R.drawable.ic_plus)
    }
    IceboxAdapter.setContext(activity)
    setUpViews()
    //isSwiped()
  }

  fun setUpViews() {
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = IceboxAdapter
    IceboxAdapter.initItem()
  }

  //fun isSwiped(){
  //    searchTargetList = IceboxAdapter.getTargetList()
  //    if(searchTargetList.size > 0) {
  //      undo.visibility = View.VISIBLE
  //      add.setImageResource(R.drawable.ic_recipe_search)
  //    }
  //    else {
  //      undo.visibility = View.GONE
  //    }
  //}

  fun addIfNeedChange(){
    if(searchTargetList.size > 0) {
      add.setImageResource(R.drawable.ic_recipe_search)
      undo.visibility = View.VISIBLE
    }

  }
}
