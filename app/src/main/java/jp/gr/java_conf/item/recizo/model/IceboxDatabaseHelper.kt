package jp.gr.java_conf.item.recizo.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class IceboxDatabaseHelper(context: Context): SQLiteOpenHelper(context, "Icebox", null, 1) {
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