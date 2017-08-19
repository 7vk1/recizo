package com.recizo.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.recizo.module.AppContextHolder
import com.recizo.module.XMLCategoryParser

class CategoryDatabaseHelper(context: Context) {
  private val dbHelper: CategoryDatabaseHelper.DatabaseHelper
  lateinit var db: SQLiteDatabase
  init { dbHelper = DatabaseHelper(context) }

  fun getItem(keyword: String): List<String> {
    readableOpen()
    println("GET ITEM START")
    val query = "SELECT category FROM $TABLE_NAME WHERE category LIKE '%$keyword%'"
    val list = mutableListOf<String>()
    db.rawQuery(query, null).use {
      println("SELECT QUERY")
      while (it.moveToNext()) {
        println("SELECT NUM: $it")
        list.add(it.getString(it.getColumnIndex("category")))
      }
    }
    db.close()
    return list.toList()
  }

  private fun readableOpen() { db = dbHelper.readableDatabase }

  companion object {
    val TABLE_NAME = "category_table"
    val DB_NAME = "RakutenRecipe"
    val DB_VERSION = 1
  }

  inner class DatabaseHelper(context: Context?): SQLiteOpenHelper(context, CategoryDatabaseHelper.DB_NAME, null, CategoryDatabaseHelper.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
      val query = "CREATE TABLE $TABLE_NAME (" +
          "_id integer primary key autoincrement, " +
          "category char(256))"
      db!!.execSQL(query)

      val categoryList = XMLCategoryParser(AppContextHolder.context!!.resources).parse()
      categoryList.forEach {
        val insertQuery = "INSERT INTO $TABLE_NAME(category) VALUES ('$it')"
        db.execSQL(insertQuery)
      }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }
}