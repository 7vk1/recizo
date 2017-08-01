package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class IceboxPresenter(val fragment: IceboxButtons) {
  private var searchList = mutableSetOf<Long>()
  private var garbageList = mutableSetOf<Long>()
  private var recyclerView: RecyclerView? = null
  private var sort = Sort.CREATED
  private val iceboxAdapter = IceboxAdapter()
  init {
    iceboxAdapter.setEventListener(object : IceboxAdapter.EventListener {
      override fun onViewAttached(holder: IceboxAdapter.IceboxViewHolder) {
        checkHolder(holder)
      }
      override fun onBindViewHolder(holder: IceboxAdapter.IceboxViewHolder, position: Int) {
      }

      override fun onItemOpen(dragEdge: SwipeLayout.DragEdge, itemId: Long) {
        when(dragEdge) {
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
      override fun onDeleteClicked(itemId: Long) {
        removeItem(itemId)
      }
      override fun onSearchClicked(item: IceboxItem) {
        fragment.toSearchActivity(getSearchItemList()) // todo アイテム１つで検索？
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
    sortItems(sort)
    fragment.changeBtnVisibility(add = true)
    checkHolders()
  }

  fun sortItems(type: Sort) {
    sort = type
    iceboxAdapter.setItemList(sortList(iceboxAdapter.getItemList(), sort))
    fragment.onSortMethodChange(sort)
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
    if(searchList.size != 0) fragment.changeBtnVisibility(search = true)
    else fragment.changeBtnVisibility(add = true)
  }

  fun getSearchItemList(): Set<String> {
    return searchList.map { id -> iceboxAdapter.getItemList().first { it.id.toLong() == id }.name }.toSet()
  }

  private fun checkHolder(holder: IceboxAdapter.IceboxViewHolder) = launch(UI) {
    delay(100)
    if(garbageList.contains(holder.itemId)) holder.swipeLayout.open(false, SwipeLayout.DragEdge.Right)
    else if(searchList.contains(holder.itemId)) holder.swipeLayout.open(false, SwipeLayout.DragEdge.Left)
    else holder.swipeLayout.close(false)
  }

  private fun checkHolders() = launch(UI) {
    delay(100)
    garbageList.map {
      val item = recyclerView?.findViewHolderForItemId(it)
      if(item != null) (item as IceboxAdapter.IceboxViewHolder).swipeLayout.open(false, SwipeLayout.DragEdge.Right)
    }
    searchList.map {
      val item = recyclerView?.findViewHolderForItemId(it)
      if(item != null) (item as IceboxAdapter.IceboxViewHolder).swipeLayout.open(false, SwipeLayout.DragEdge.Left)
    }
  }

  private fun sortList(list: MutableList<IceboxItem>, type: Sort): MutableList<IceboxItem> {
    return when(type) {
      Sort.NAME -> list.sortedBy { it.name }
      Sort.DATE -> list.sortedBy { it.date }
      Sort.CATEGORY -> list.sortedBy { it.category }
      Sort.CREATED -> list.sortedBy { it.id }
    }.toMutableList()
  }

  private fun removeItem(id: Long) {
    IceboxDao.delete(id.toInt())
    val index: Int = iceboxAdapter.getItemList().indexOfFirst { it.id.toLong() == id }
    iceboxAdapter.removeItem(index)
  }

  enum class Sort {
    NAME, DATE, CATEGORY, CREATED
  }

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean = false, undo: Boolean = false, search: Boolean = false, delete: Boolean = false)
    fun toIceboxItemSetActivity(item: IceboxItem)
    fun toSearchActivity(set: Set<String>)
    fun onSortMethodChange(type: Sort)
  }
}