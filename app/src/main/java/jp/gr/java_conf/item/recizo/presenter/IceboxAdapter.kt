package jp.gr.java_conf.item.recizo.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.database.IceboxDatabaseHelper
import jp.gr.java_conf.item.recizo.model.viewholder.IceboxViewHolder
import jp.gr.java_conf.item.recizo.model.entity.Vegetable
import jp.gr.java_conf.item.recizo.view.ChangeActivity

object IceboxAdapter: RecyclerView.Adapter<IceboxViewHolder>() {
  var vegetableList = mutableListOf<Vegetable>()
  var searchList = mutableSetOf<String>()
  var garbageList = mutableSetOf<Vegetable>()
  lateinit var useContext: Context

  override fun getItemCount(): Int {
    return vegetableList.size
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent!!.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    holder!!.title.text = vegetableList[position].name
    holder.memo.text = vegetableList[position].memo
    holder.date.text = vegetableList[position].date
    holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,holder.swipeLayout.findViewById(R.id.icebox_item_search))
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,holder.swipeLayout.findViewById(R.id.icebox_item_del))
    var search = false
    var garbage = false
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener(){
      override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
        //こいつでどっち側のViewを引っ張ってるか検知出来る
        //ただ実際に俺らが見ている動きとは逆方向なので注意
        var direction =  holder.swipeLayout.dragEdge
        var openStatus = holder.swipeLayout.openStatus
        if(direction == SwipeLayout.DragEdge.Right){
          //検索ビュー展開時に検索ワード追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !search){
            searchList.add(vegetableList[position].name)
            Log.d("取ったやつ : 空いたで", searchList.toString())
            Log.d("なんかよくわかんないの",search.toString())
          }
          //検索ビュー縮小時に検索ワード削除
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && search){
            searchList.remove(vegetableList[position].name)
            Log.d("検索リストのサイズ : 閉じたで", searchList.size.toString())
            Log.d("なんかよくわかんないの",search.toString())
          }
        }
        else if(direction == SwipeLayout.DragEdge.Left){
          //削除ビュー展開時に削除候補追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !garbage){
            garbageList.add(getOneItem(position))
            Log.d("ゴミ箱行きリストのサイズ", garbageList.size.toString())
          }
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && garbage){
            garbageList.remove(getOneItem(position))
            Log.d("ゴミ箱行きリストのサイズ", garbageList.size.toString())
          }
        }
      }

      override fun onOpen(layout: SwipeLayout?) {
        var direction =  holder.swipeLayout.dragEdge
        if(direction == SwipeLayout.DragEdge.Right){
          search = true
        }
        else if(direction == SwipeLayout.DragEdge.Left){
          garbage = false
        }
      }

      override fun onClose(layout: SwipeLayout?) {
        var direction =  holder.swipeLayout.dragEdge
        if(direction == SwipeLayout.DragEdge.Left){
          search = false
        }
        else if(direction == SwipeLayout.DragEdge.Left){
          garbage = true
        }
      }
    })
    val intent = Intent(useContext as Activity , ChangeActivity::class.java)
    holder.cardView.setOnClickListener {
      if(position == vegetableList.size){
        intent.putExtra("vegetable", vegetableList[position - 1])
        intent.putExtra("position", position -1)
      }else {
      intent.putExtra("vegetable", vegetableList[position])
      intent.putExtra("position", position)
      }

      (useContext as Activity).startActivity(intent)
    }
  }

  fun setContext(context: Context) {
    this.useContext = context
  }

  fun updateItem(vegetable: Vegetable, position: Int) {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.writebleOpen()
    idh.updateVegetable(vegetable)
    // vegetableListの更新
    for(i in vegetableList.indices) {
      if(vegetable.id == vegetableList[i].id) {
        vegetableList[i] = vegetable
        break
      }
    }
    notifyItemChanged(position)
  }

  fun addItem(vegetable: Vegetable) {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.writebleOpen()
    idh.addVegetable(vegetable)
    vegetableList.add(idh.getVegetableLast() )
    notifyItemInserted(vegetableList.size)
  }

  fun removeItem(position: Int) {
    if(position < vegetableList.size) {
      val idh = IceboxDatabaseHelper(this.useContext)
      idh.writebleOpen()
      idh.deleteVegetable(vegetableList[position].id)
      vegetableList.removeAt(position)
      notifyItemRemoved(position)
    }
  }

  fun getOneItem(position: Int): Vegetable{
    return vegetableList[position]
  }

  fun getItem(): MutableList<Vegetable> {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.readableOpen()
    this.vegetableList = idh.getVegetableAll()

    return vegetableList
  }

  fun getTargetList(): Set<String>{
    return searchList
  }

  fun initItem() {
    // TODO RecyclerViewのItemが移動(Move)されていた場合反映されない問題
    val list = getItem()
    notifyItemRangeInserted(0, list.size)
  }
}