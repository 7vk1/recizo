package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.model.viewholder.IceboxViewHolder
import com.daimajia.swipe.*
import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao


class IceboxAdapter(val fragment: IceboxButtons) : RecyclerView.Adapter<IceboxViewHolder>() {
  init {
    setHasStableIds(true)
  }
  var itemList = IceboxDao.getAll().toMutableList()
  var searchList = mutableSetOf<String>()
  var garbageList = mutableSetOf<Int>()
  var recyclerView: RecyclerView? = null
  var selectedItemIdList = mutableSetOf<Long>()

  override fun getItemCount(): Int {
    return itemList.size
  }

  override fun getItemId(position: Int): Long {
    return itemList[position].id.toLong()
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean = false, undo: Boolean = false, search: Boolean = false, delete: Boolean = false)
    fun toChangeActivity(item: IceboxItem, position: Int)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    val item = itemList[position]
    holder!!.title.text = item.name
    holder.memo.text = item.memo
    holder.date.text = item.date
    holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,holder.swipeLayout.findViewById(R.id.icebox_item_del))
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,holder.swipeLayout.findViewById(R.id.icebox_item_search))
    var search = false
    var garbage = false
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener() {
      override fun onHandRelease(layout: SwipeLayout?, x: Float, y: Float) {
        //こいつでどっち側のViewを引っ張ってるか検知出来る
        //ただ実際に俺らが見ている動きとは逆方向なので注意
        val direction =  holder.swipeLayout.dragEdge
        val openStatus = holder.swipeLayout.openStatus
        if(direction == SwipeLayout.DragEdge.Left){
          //検索ビュー展開時に検索ワード追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !search){
            searchList.add(itemList[position].name)
            selectedItemIdList.add(itemList[position].id.toLong())
            fragment.changeBtnVisibility(search = true, undo = true)
            Log.d("取ったやつ : 空いたで", searchList.toString())
          }
          //検索ビュー縮小時に検索ワード削除
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && search){
            searchList.remove(itemList[position].name)
            if(searchList.size == 0) {
              fragment.changeBtnVisibility(add = true)
            }
            Log.d("検索リストのサイズ : 閉じたで", searchList.size.toString())
          }
        }
        else if(direction == SwipeLayout.DragEdge.Right){
          //削除ビュー展開時に削除候補追加
          if(openStatus == SwipeLayout.Status.Open || openStatus == SwipeLayout.Status.Middle && !garbage){
            Log.d("ポジション",getIceboxItemId(position).toString())
            garbageList.add(itemList[position].id)
            println(holder.itemId)
            selectedItemIdList.add(itemList[position].id.toLong())
            fragment.changeBtnVisibility(delete = true, undo = true)
            Log.d("ゴミ箱行きリストのサイズ", garbageList.size.toString())
          }
          else if(openStatus == SwipeLayout.Status.Close || openStatus == SwipeLayout.Status.Middle && garbage){
            garbageList.remove(getIceboxItemId(position))
            if(garbageList.size == 0) {
              fragment.changeBtnVisibility(add = true)
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
      if(position == itemList.size){ //TODO これいるの？
        fragment.toChangeActivity(item = itemList[position -1], position = position -1)
      }else {
        fragment.toChangeActivity(item = itemList[position], position = position)
      }
    }
  }
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
    super.onAttachedToRecyclerView(recyclerView)
    this.recyclerView = recyclerView
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
    super.onDetachedFromRecyclerView(recyclerView)
    this.recyclerView = null
  }


  fun onItemUpdated(position: Int) {
    notifyItemChanged(position)
  }

  fun onItemAdded() {
    notifyItemInserted(itemList.size)
  }

  fun removeItem(id: Int) {
    IceboxDao.delete(id)
    itemList = itemList.filter { it.id != id }.toMutableList()
    notifyDataSetChanged()
  }

  private fun getIceboxItemId(position: Int): Int{
    return itemList[position].id
  }



//  fun getItem(): MutableList<Vegetable> {
//    this.vegetableList = IceboxDao.getAll().toMutableList()
//    return vegetableList
//  }
//
//  fun initItem() {
//    // TODO RecyclerViewのItemが移動(Move)されていた場合反映されない問題
//    val list = getItem()
//    notifyItemRangeInserted(0, list.size)
//  }

  fun updateDataSet() {
    itemList = IceboxDao.getAll().toMutableList()
    this.notifyDataSetChanged()
  }


  fun onDeleteClicked() {
    garbageList.sorted().reversed().map {
      removeItem(it)
    }
    garbageList.clear()
    fragment.changeBtnVisibility(add = true)
  }
  fun getSearchItemList(): Set<String> { return searchList }

  fun onUndoClicked() {
    selectedItemIdList.map {
      val item = recyclerView?.findViewHolderForItemId(it)
      println(item)
      if(item != null) (item as IceboxViewHolder).swipeLayout.close()
    }
    Log.d("来てる？","わかんね")
    fragment.changeBtnVisibility(add = true)
  }

}