package com.recizo.module

import android.content.Context
import com.recizo.model.database.IceboxDatabaseHelper
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

  fun update(vegetable: Vegetable) {
    iceboxDatabaseHelper?.addVegetable(vegetable)
  }

  fun getAll(): List<Vegetable> {
    return iceboxDatabaseHelper?.getVegetableAll()!!.toList()
  }

  fun getLast(): Vegetable? {
    return iceboxDatabaseHelper?.getVegetableLast()
  }

  fun add(vegetable: Vegetable) {
    iceboxDatabaseHelper?.addVegetable(vegetable)
  }

  fun delete(vegetableId: Int) {
    iceboxDatabaseHelper?.deleteVegetable(vegetableId)
  }
}