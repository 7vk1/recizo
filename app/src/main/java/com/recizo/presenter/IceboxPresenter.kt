package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao

class IceboxPresenter(val fragment: IceboxButtons) {
  private var searchList = mutableSetOf<Long>()
  private var garbageList = mutableSetOf<Long>()
  private var recyclerView: RecyclerView? = null
  private var sort = Sort.CATEGORY
  private val iceboxAdapter = IceboxAdapter()
  init {
    iceboxAdapter.setEventListener(object : IceboxAdapter.EventListener {
      override fun onViewAttached(holder: IceboxAdapter.IceboxViewHolder) {
        if (!garbageList.contains(holder.itemId) && !searchList.contains(holder.itemId)) holder.swipeLayout.close()
      }
      override fun onBindViewHolder(holder: IceboxAdapter.IceboxViewHolder, position: Int) {
        if (!garbageList.contains(holder.itemId) && !searchList.contains(holder.itemId)) holder.swipeLayout.close()
      }
      override fun onItemOpen(dragEdge: SwipeLayout.DragEdge, itemId: Long) {
        when (dragEdge) {
          SwipeLayout.DragEdge.Left -> {
            searchList.add(itemId)
            fragment.changeBtnVisibility(search = true, undo = true)
          }
          SwipeLayout.DragEdge.Right -> {
            garbageList.add(itemId)
            fragment.changeBtnVisibility(delete = true, undo = true)
          }
          else -> return
        }
      }
      override fun onItemClosed(itemId: Long) {
        garbageList.remove(itemId)
        searchList.remove(itemId)
        if (garbageList.size == 0 && searchList.size == 0) fragment.changeBtnVisibility(add = true)
      }

      override fun onItemClicked(item: IceboxItem) {
        fragment.toIceboxItemSetActivity(item)
      }
    })
  }

  fun setRecyclerView(view: RecyclerView) {
    recyclerView = view
    view.adapter = iceboxAdapter
    dataUpdated()
  }

  fun dataUpdated() {
    val list = IceboxDao.getAll().toMutableList()
    iceboxAdapter.setItemList(list)
    fragment.changeBtnVisibility(add = true)
  }

  fun sortItems(type: Sort) {
    sort = type
    iceboxAdapter.setItemList(sortList(iceboxAdapter.getItemList(), sort))
  }

  fun onUndoClicked() {
    searchList.plus(garbageList).map {
      val item = recyclerView?.findViewHolderForItemId(it)
      if(item != null) (item as IceboxAdapter.IceboxViewHolder).swipeLayout.close()
    }
    searchList.clear()
    garbageList.clear()
    fragment.changeBtnVisibility(add = true)
  }

  fun onDeleteClicked() {
    garbageList.map { removeItem(it) }
    garbageList.clear()
    fragment.changeBtnVisibility(add = true)
  }

  fun getSearchItemList(): Set<String> {
    return searchList.map { id -> iceboxAdapter.getItemList().first { it.id.toLong() == id }.name }.toSet()
  }

  private fun sortList(list: MutableList<IceboxItem>, type: Sort): MutableList<IceboxItem> {
    return when(type) {
      Sort.NAME -> list.sortedBy { it.name }
      Sort.DATE -> list.sortedBy { it.date }
      Sort.CATEGORY -> list.sortedBy { it.category }
    }.toMutableList()
  }

  private fun removeItem(id: Long) {
    IceboxDao.delete(id.toInt())
    val index: Int = iceboxAdapter.getItemList().indexOfFirst { it.id.toLong() == id }
    iceboxAdapter.removeItem(index)
  }

  enum class Sort {
    NAME, DATE, CATEGORY
  }

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean = false, undo: Boolean = false, search: Boolean = false, delete: Boolean = false)
    fun toIceboxItemSetActivity(item: IceboxItem)
  }
}