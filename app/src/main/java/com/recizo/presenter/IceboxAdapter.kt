package com.recizo.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.model.database.IceboxDatabaseHelper
import com.recizo.model.viewholder.IceboxViewHolder
import com.recizo.model.entity.Vegetable
import com.recizo.ChangeActivity
import com.daimajia.swipe.*


class IceboxAdapter(val fragment: IceboxButtons) : RecyclerView.Adapter<IceboxViewHolder>() {
  var vegetableList = mutableListOf<Vegetable>()
  var searchList = mutableSetOf<String>()
  var garbageList = mutableSetOf<Int>()
  var targetView: View? = null

  override fun getItemCount(): Int {
    return vegetableList.size
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent!!.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean, undo: Boolean, search: Boolean, delete: Boolean)
    fun toChangeActivity(vegetable: Vegetable, position: Int)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    holder!!.title.text = vegetableList[position].name
    holder.memo.text = vegetableList[position].memo
    holder.date.text = vegetableList[position].date
    holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,holder.swipeLayout.findViewById(R.id.icebox_item_del))
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,holder.swipeLayout.findViewById(R.id.icebox_item_search))
    var search = false
    var garbage = false
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener() {
      override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
        //こいつでどっち側のViewを引っ張ってるか検知出来る
        //ただ実際に俺らが見ている動きとは逆方向なので注意
        val direction =  holder.swipeLayout.dragEdge
        val openStatus = holder.swipeLayout.openStatus
        if(direction == SwipeLayout.DragEdge.Left){
          //検索ビュー展開時に検索ワード追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !search){
            searchList.add(vegetableList[position].name)
            setSearchVisibility(true)
            Log.d("取ったやつ : 空いたで", searchList.toString())
          }
          //検索ビュー縮小時に検索ワード削除
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && search){
            searchList.remove(vegetableList[position].name)
            if(searchList.size == 0) {
              setSearchVisibility(false)
            }
            Log.d("検索リストのサイズ : 閉じたで", searchList.size.toString())
          }
        }
        else if(direction == SwipeLayout.DragEdge.Right){
          //削除ビュー展開時に削除候補追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !garbage){
            Log.d("ポジション",getOneItemId(position).toString())
            garbageList.add(getOneItemId(position))
            setDeleteVisibility(true)
            Log.d("ゴミ箱行きリストのサイズ", garbageList.size.toString())
          }
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && garbage){
            garbageList.remove(getOneItemId(position))
            if(garbageList.size == 0) {
              setDeleteVisibility(false)
            }
            Log.d("ゴミ箱行きリストのサイズ", garbageList.size.toString())
          }
        }
      }

      override fun onOpen(layout: SwipeLayout?) {
        val direction =  holder.swipeLayout.dragEdge
        if(direction == SwipeLayout.DragEdge.Left){
          garbage = false
        }
        else if(direction == SwipeLayout.DragEdge.Right) {
          search = true
        }
      }

      override fun onClose(layout: SwipeLayout?) {
        val direction =  holder.swipeLayout.dragEdge
        if(direction == SwipeLayout.DragEdge.Left){
          search = false
        }
        else if(direction == SwipeLayout.DragEdge.Left){
          garbage = true
        }
      }
    })

    holder.cardView.setOnClickListener {
      if(position == vegetableList.size){
        fragment.toChangeActivity(vegetable = vegetableList[position -1], position = position -1)
      }else {
        fragment.toChangeActivity(vegetable = vegetableList[position], position = position)
      }
    }
  }



  fun updateItem(vegetable: Vegetable, position: Int) {
    val idh = IceboxDatabaseHelper(context)
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
    val idh = IceboxDatabaseHelper(context)
    idh.addVegetable(vegetable)
    vegetableList.add(idh.getVegetableLast() )
    notifyItemInserted(vegetableList.size)
  }

  fun removeItem(position: Int) {
    if(position < vegetableList.size) {
      val idh = IceboxDatabaseHelper(context)
      idh.deleteVegetable(vegetableList[position].id)
      vegetableList.removeAt(position)
      notifyItemRemoved(position)
    }
  }

  fun getOneItemId(position: Int): Int{
    return vegetableList[position].id
  }



  fun getItem(): MutableList<Vegetable> {
    val idh = IceboxDatabaseHelper(context)
    this.vegetableList = idh.getVegetableAll()

    return vegetableList
  }

  fun initItem() {
    // TODO RecyclerViewのItemが移動(Move)されていた場合反映されない問題
    val list = getItem()
    notifyItemRangeInserted(0, list.size)
  }

  fun setView(view: View?){
    targetView = view
  }
  fun onDeleteClicked() {
    for(item in garbageList.sorted()) {
      removeItem(item)
    }
    garbageList.clear()
    fragment.changeBtnVisibility(add = true, delete = false, undo = false, search = false)
  }
  fun onUndoClicked() {
    garbageList.clear()
    searchList.clear()
    holder.swipeLayout.close()
    Log.d("来てる？","わかんね")
//    fragment.changeBtnVisibility() todo
  }
  fun setSearchVisibility(check: Boolean){
    if(check)fragment.changeBtnVisibility(add = false, delete = false, undo = true, search = true)
    else fragment.changeBtnVisibility(search = false, undo = false, add = true, delete = false)
  }

  fun setDeleteVisibility(check: Boolean){
    if(check)fragment.changeBtnVisibility(delete = true, undo = true, add = false, search = true)
    else fragment.changeBtnVisibility(delete = false, undo = false, add = true, search = true)
    }
  }


}