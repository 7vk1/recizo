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
    access()
    iceboxDatabaseHelper?.addVegetable(vegetable)
    close()
  }

  fun getAll(): List<Vegetable> {
    access()
    val ret = iceboxDatabaseHelper?.getVegetableAll()
    println(ret?.size)
    close()
    return if(ret != null)ret else listOf<Vegetable>()
  }

  fun getLast(): Vegetable? {
    access()
    val ret = iceboxDatabaseHelper?.getVegetableLast()
    close()
    return ret
  }

  fun add(vegetable: Vegetable) {
    access()
    iceboxDatabaseHelper?.addVegetable(vegetable)
    close()
  }

  fun delete(vegetableId: Int) {
    access()
    iceboxDatabaseHelper?.deleteVegetable(vegetableId)
    close()
  }
}