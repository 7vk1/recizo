package com.recizo.presenter

import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao

object IceboxDataStore {
  var iceboxAdapter: IceboxAdapter? = null
  var itemList = IceboxDao.getAll().toMutableList()

  fun addItem(item: IceboxItem) {
    itemList.add(item)
    iceboxAdapter?.onItemAdded()
  }
  fun removeItem(itemId: Int) {
    itemList = itemList.filterIndexed { index, iceboxItem ->
      if(iceboxItem.id == itemId) {
        iceboxAdapter?.onItemremoved(index)
        false
      } else true
    }.toMutableList()
  }
  fun updateItem(item: IceboxItem) {
    itemList = itemList.mapIndexed { index, iceboxItem ->
      if(iceboxItem.id == item.id) {
        iceboxAdapter?.onItemUpdated(index)
        item
      } else iceboxItem
    }.toMutableList()
  }

}