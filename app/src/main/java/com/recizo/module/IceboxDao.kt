package com.recizo.module

import android.content.Context
import com.recizo.model.database.IceboxDatabaseHelper
import com.recizo.model.entity.IceboxItem

object IceboxDao {
  var context: Context? = null
    set(value) {
      if(field != null) return
      else field = value
    }
  private var iceboxDatabaseHelper: IceboxDatabaseHelper? = null

  fun update(item: IceboxItem) {
    access()
    iceboxDatabaseHelper?.updateItem(item)
    close()
  }

  fun getAll(): List<IceboxItem> {
    access()
    val ret = iceboxDatabaseHelper?.getAllItem()
    close()
    return ret ?: listOf<IceboxItem>()
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

  fun getAllFromRemote(context: Context): List<IceboxItem> {
    val db = IceboxDatabaseHelper(context)
    val ret = db.getAllItem()
    return ret
  }

  private fun access() { iceboxDatabaseHelper = IceboxDatabaseHelper(AppContextHolder.context!!) }
  private fun close() { iceboxDatabaseHelper = null }
}