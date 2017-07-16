package jp.gr.java_conf.item.recizo.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.coroutines.experimental.buildSequence

class IceboxDatabaseHelper(context: Context) {
  private val dbHelper: DatabaseHelper
  lateinit var db: SQLiteDatabase

  init {
    dbHelper = DatabaseHelper(context)
  }

  fun writebleOpen() {
    db = dbHelper.writableDatabase
  }

  fun readableOpen() {
    db = dbHelper.readableDatabase
  }

  fun getVegetableAll(): MutableList<Vegetable> {
    val query = "SELECT _id, name, memo, year, month, day FROM $TABLE_NAME"
    var list = mutableListOf<Vegetable>()
    db.rawQuery(query, null).use {
      list = buildSequence {
        while(it.moveToNext()) {
          yield(
              Vegetable(
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
    return list
  }

  fun getVegetableLast(): Vegetable {
    val query = "SELECT _id, name, memo ,year, month, day FROM $TABLE_NAME"
    db.rawQuery(query, null).use {
      it.moveToLast()
      return Vegetable(
          it.getInt(it.getColumnIndex("_id")),
          it.getString(it.getColumnIndex("name")),
          it.getString(it.getColumnIndex("memo")),
          it.getString(it.getColumnIndex("year")),
          it.getString(it.getColumnIndex("month")),
          it.getString(it.getColumnIndex("day"))
      )
    }
  }

  fun addVegetable(vegetable: Vegetable) {
    val values: ContentValues = ContentValues()
    values.put("name", vegetable.name)
    values.put("memo", vegetable.memo)

    val date = vegetable.date.split("/".toRegex())
    values.put("year", date[0])
    values.put("month", date[1])
    values.put("day", date[2])
    db.insertOrThrow(TABLE_NAME, null, values)
  }

  fun deleteVegetable(vegetableId: Int) {
    Log.d("TEST DELETE ID", vegetableId.toString() )
    val result = db.delete(TABLE_NAME, "_id=$vegetableId", null)
    Log.d("TEST DELETE RESULT", result.toString() )
  }

  fun deleteVegetable(vegetable: Vegetable) {
    val id = vegetable.id
    db.delete(TABLE_NAME, "_id=$id", null)
  }

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