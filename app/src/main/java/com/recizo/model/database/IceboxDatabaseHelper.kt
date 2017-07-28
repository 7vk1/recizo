package com.recizo.model.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.recizo.model.entity.IceboxItem
import com.recizo.model.entity.Vegetable
import kotlin.coroutines.experimental.buildSequence

class IceboxDatabaseHelper(context: Context) {
  private val dbHelper: DatabaseHelper
  lateinit var db: SQLiteDatabase

  init {
    dbHelper = DatabaseHelper(context)
  }

  private fun writableOpen() {
    db = dbHelper.writableDatabase
  }

  private fun readableOpen() {
    db = dbHelper.readableDatabase
  }

  fun updateItem(item: IceboxItem) {
    writableOpen()
    val values = ContentValues()
    values.put("name", item.name)
    values.put("memo", item.memo)
    val dates = item.date.split("/".toRegex() )
    values.put("year", dates[0])
    values.put("month", dates[1])
    values.put("day", dates[2])
    val id = item.id
    db.update(TABLE_NAME, values, "_id=$id", null)
    db.close()
  }

  fun getAllItem(): MutableList<IceboxItem> {
    readableOpen()
    val query = "SELECT _id, name, memo, year, month, day FROM $TABLE_NAME"
    var list = mutableListOf<IceboxItem>()
    db.rawQuery(query, null).use {
      list = buildSequence {
        while(it.moveToNext()) {
          yield(
              IceboxItem(
                  it.getInt(it.getColumnIndex("_id")),
                  it.getString(it.getColumnIndex("name")),
                  it.getString(it.getColumnIndex("memo")),
                  it.getString(it.getColumnIndex("year")),
                  it.getString(it.getColumnIndex("month")),
                  it.getString(it.getColumnIndex("day"))
              )
          )
        }
      }.toMutableList()
    }
    db.close()
    return list
  }

  fun getLastItem(): IceboxItem {
    readableOpen()
    val query = "SELECT _id, name, memo ,year, month, day FROM $TABLE_NAME"
    db.rawQuery(query, null).use {
      it.moveToLast()
      val ret = IceboxItem(
          it.getInt(it.getColumnIndex("_id")),
          it.getString(it.getColumnIndex("name")),
          it.getString(it.getColumnIndex("memo")),
          it.getString(it.getColumnIndex("year")),
          it.getString(it.getColumnIndex("month")),
          it.getString(it.getColumnIndex("day"))
      )
      db.close()
      return ret
    }
  }

  fun addItem(item: IceboxItem) {
    writableOpen()
    val values: ContentValues = ContentValues()
    values.put("name", item.name)
    values.put("memo", item.memo)

    val date = item.date.split("/".toRegex())
    values.put("year", date[0])
    values.put("month", date[1])
    values.put("day", date[2])
    db.insertOrThrow(TABLE_NAME, null, values)
    db.close()
  }

  fun deleteItem(itemId: Int) {
    writableOpen()
    Log.d("TEST DELETE ID", itemId.toString() )
    val result = db.delete(TABLE_NAME, "_id=$itemId", null)
    Log.d("TEST DELETE RESULT", result.toString() )
    db.close()
  }

//  fun deleteVegetable(vegetable: Vegetable) {
//    writableOpen()
//    val id = vegetable.id
//    db.delete(TABLE_NAME, "_id=$id", null)
//    db.close()
//  }

  companion object {
    val TABLE_NAME = "icebox_table"
    val DB_NAME = "Icebox"
    val DB_VERSION = 1
  }

  inner class DatabaseHelper(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
      val query = "CREATE TABLE icebox_table (" +
          "_id integer primary key autoincrement, " +
          "name char(256), " +
          "memo char(256), " +
          "year int, " +
          "month int, " +
          "day int)"

      db!!.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }
}