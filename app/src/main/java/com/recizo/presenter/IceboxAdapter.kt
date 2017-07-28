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
  var itemList = IceboxDao.getAll().toMutableList()
  var searchList = mutableSetOf<Long>()
  var garbageList = mutableSetOf<Long>()
  var recyclerView: RecyclerView? = null
  var selectedItemIdList = mutableSetOf<Long>()
  init {
    setHasStableIds(true)
    itemList.add(IceboxItem(id = -1, memo = "", name = "empty", date = ""))
  }

  fun removeItem(id: Long) {
    IceboxDao.delete(id.toInt())
    itemList = itemList.filter { it.id.toLong() != id }.toMutableList()
    notifyDataSetChanged()
  }


  fun updateDataSet() {
    itemList = IceboxDao.getAll().toMutableList()
    itemList.add(IceboxItem(-1, "empty", "", ""))
    this.notifyDataSetChanged()
  }


  fun onDeleteClicked() {
    garbageList.sorted().reversed().map {
      removeItem(it)
    }
    garbageList.clear()
    fragment.changeBtnVisibility(add = true)
  }
  fun getSearchItemList(): Set<String> {
    return searchList.map { id -> itemList.first { it.id.toLong() == id }.name }.toSet()
  }

  fun onUndoClicked() {
    selectedItemIdList.map {
      val item = recyclerView?.findViewHolderForItemId(it)
      println(item)
      if(item != null) (item as IceboxViewHolder).swipeLayout.close()
    }
    Log.d("来てる？","わかんね")
    fragment.changeBtnVisibility(add = true)
  }

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

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    val item = itemList[position]
    if(item.id == -1) {
      holder!!.itemView.visibility = View.INVISIBLE
      return
    }
    holder!!.bindView(name = item.name, memo = item.memo, date = item.date)

    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener() {
      override fun onOpen(layout: SwipeLayout?) {
        super.onOpen(layout)
        when(layout!!.dragEdge) {
          SwipeLayout.DragEdge.Left -> {
            searchList.add(holder.itemId)
            fragment.changeBtnVisibility(search = true, undo = true)
          }
          SwipeLayout.DragEdge.Right -> {
            garbageList.add(holder.itemId)
            fragment.changeBtnVisibility(delete = true, add = true)
          }
          else -> return
        }
      }
      override fun onClose(layout: SwipeLayout?) {
        super.onClose(layout)
        when(layout!!.dragEdge) {
          SwipeLayout.DragEdge.Left -> {
            garbageList.remove(holder.itemId)
            if(garbageList.size == 0) fragment.changeBtnVisibility(add = true)
          }
          SwipeLayout.DragEdge.Right -> {
            searchList.remove(holder.itemId)
            if(searchList.size == 0) fragment.changeBtnVisibility(add = true)
          }
          else -> return
        }
      }
    })

    holder.cardView.setOnClickListener {
      fragment.toChangeActivity(itemList.first { it.id.toLong() == holder.itemId })
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

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean = false, undo: Boolean = false, search: Boolean = false, delete: Boolean = false)
    fun toChangeActivity(item: IceboxItem)
  }

}