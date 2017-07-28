package com.recizo.presenter

import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao

object IceboxDataStore {
  var iceboxAdapter: IceboxAdapter? = null
  var itemList = IceboxDao.getAll().toMutableList()

  fun addItem(item: IceboxItem) {
    IceboxDao.add(item)
    itemList.add(item)
    iceboxAdapter?.onItemAdded()
  }
  fun removeItem(itemId: Int) {
    IceboxDao.delete(itemId)
    itemList = itemList.filterIndexed { index, iceboxItem ->
      if(iceboxItem.id == itemId) {
        iceboxAdapter?.onItemremoved(index)
        false
      } else true
    }.toMutableList()
  }
  fun updateItem(item: IceboxItem) {
    println("update!")
    println(iceboxAdapter)
    IceboxDao.update(item)
    itemList = itemList.mapIndexed { index, iceboxItem ->
      if(iceboxItem.id == item.id) {
        println("C!!")
        iceboxAdapter?.onItemUpdated(index)
        item
      } else iceboxItem
    }.toMutableList()
  }

}