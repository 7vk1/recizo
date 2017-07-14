package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.Vegetable
import jp.gr.java_conf.item.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*

class IceboxFragment() : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fab.setOnClickListener {
      startActivity(Intent(activity, RegisterActivity::class.java) )
    }
    setUpViews()
    swiped()
  }

  fun setUpViews() {
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = IceboxAdapter
//    recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager(activity).orientation) )
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
        return false
      }

      // スワイプされた場合
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 項目を消去
          IceboxAdapter.removeItem(viewHolder.adapterPosition)
      }
    })

    helper.attachToRecyclerView(recyclerView)
  }
}
