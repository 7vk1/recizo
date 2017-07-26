package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.content.ClipData
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.Vegetable
import jp.gr.java_conf.item.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*
import kotlinx.android.synthetic.main.icebox_item.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

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
    isSwiped()
  }

  fun setUpViews() {
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = IceboxAdapter
    IceboxAdapter.initItem()
  }

  fun isSwiped(){
      searchTargetList = IceboxAdapter.getTargetList()
      if(searchTargetList.size > 0) {
        undo.visibility = View.VISIBLE
        add.setImageResource(R.drawable.ic_recipe_search)
      }
      else {
        undo.visibility = View.GONE
      }
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

      override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
          Log.d("左右確認 : "," 右来たよ")
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
