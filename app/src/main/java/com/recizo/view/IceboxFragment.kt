package com.recizo.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.recizo.R
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
      IceboxAdapter.notifyDataSetChanged()
    }
    IceboxAdapter.setContext(activity)
    setUpViews()
    swiped()
  }

  fun setUpViews() {
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = IceboxAdapter
    IceboxAdapter.initItem()
  }

  fun swiped() {
    val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
      // どのような動きを許可するか
      // ViewHolder ごとに分ける等の場合はここで制御する

      override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        // TODO 左スワイプの時は検索に使えるようにする
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
      }

      // 動いた場合
      override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
      }

      //要素
      override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
          Log.d("とりあえず","来た来た"+actionState.toString())
        }
      }

      // スワイプされた場合
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 項目を消去
        // TODO 左右スワイプで処理分けする時はここのdirection使ってやるっぽい。左は4、右は8。
        if(direction == 4){

          searchTargetList.add(IceboxAdapter.getOneItem(viewHolder.adapterPosition))
          //addIfNeedChange()
          Log.d("左右確認 : "," 左来たよ")
        }else if(direction == 8) {
          IceboxAdapter.removeItem(viewHolder.adapterPosition)
          Log.d("左右確認 ; "," 右来たよ")
        }
      }
    })
    helper.attachToRecyclerView(recyclerView)
  }

  fun addIfNeedChange(){
    if(searchTargetList.size > 0) {
      add.setImageResource(R.drawable.ic_recipe_search)
      undo.visibility = View.VISIBLE
    }

  }
}
