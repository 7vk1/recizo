package com.recizo.module

import android.content.Context
import com.recizo.model.database.IceboxDatabaseHelper
import com.recizo.model.entity.IceboxItem
import com.recizo.model.entity.Vegetable

object IceboxDao {
  var context: Context? = null
    get
    set(value) {
      if(field != null) return
      else field = value
    }

  private var iceboxDatabaseHelper: IceboxDatabaseHelper? = null

  private fun access() {
    iceboxDatabaseHelper = IceboxDatabaseHelper(context!!)
  }

  private fun close() {
    iceboxDatabaseHelper = null
  }

  fun update(item: IceboxItem) {
    access()
    iceboxDatabaseHelper?.updateItem(item)
    close()
  }

  fun getAll(): List<IceboxItem> {
    access()
    val ret = iceboxDatabaseHelper?.getAllItem()
    println(ret?.size)
    close()
    return if(ret != null)ret else listOf<IceboxItem>()
  }

  fun getLast(): IceboxItem? {
    access()
    val ret = iceboxDatabaseHelper?.getLastItem()
    close()
    return ret
  }

  fun add(item: IceboxItem) {
    access()
    iceboxDatabaseHelper?.addItem(item)
    close()
  }

  fun delete(itemId: Int) {
    access()
    iceboxDatabaseHelper?.deleteItem(itemId)
    close()
  }
}