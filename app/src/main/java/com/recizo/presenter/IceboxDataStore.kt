package com.recizo.presenter

import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao

object IceboxDataStore {
  var iceboxAdapter: IceboxAdapter? = null
  var itemList = IceboxDao.getAll().toMutableList()

  fun addItem(item: IceboxItem) {
    itemList.add(item)


  }
  fun removeItems() {

  }
  fun changeItem() {

  }

}