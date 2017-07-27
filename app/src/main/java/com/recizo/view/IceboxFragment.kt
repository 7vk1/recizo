package com.recizo.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.ChangeActivity
import com.recizo.R
import com.recizo.RegisterActivity
import com.recizo.model.entity.IceboxItem
import com.recizo.model.entity.Vegetable
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*

class IceboxFragment : Fragment(), IceboxAdapter.IceboxButtons {
  override fun changeBtnVisibility(add: Boolean, undo: Boolean, search: Boolean, delete: Boolean) {
    add_btn.visibility = if(add) View.VISIBLE else View.INVISIBLE
    undo_btn.visibility = if(undo) View.VISIBLE else View.INVISIBLE
    delete_btn.visibility = if(search) View.VISIBLE else View.INVISIBLE
    recipe_search_btn.visibility = if(delete) View.VISIBLE else View.INVISIBLE
  }

  override fun toChangeActivity(item: IceboxItem, position: Int) {
    val intent = Intent(activity, ChangeActivity::class.java)
    intent.putExtra("item", item)
    intent.putExtra("position", position)
    activity.startActivity(intent)
  }



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onResume() {
    super.onResume()
    println("resume!!!\n\n\n")
  }

  override fun onStop() {
    super.onStop()
    println("STOP!!!!!!!\n\n\n")
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val iceboxAdapter = IceboxAdapter(this, recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = iceboxAdapter

    add_btn.setOnClickListener {
      activity.startActivity(Intent(activity, RegisterActivity::class.java))
    }
    delete_btn.setOnClickListener {
      iceboxAdapter.onDeleteClicked()
    }
    recipe_search_btn.setOnClickListener {
      //todo impl
    }
    undo_btn

  }

}
