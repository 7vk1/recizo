package com.recizo.model.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.recizo.model.entity.RecizoRecipe

// TODO Operation check
class FavoriteRecipeDatabaseHelper(context: Context) {
  private val dbHelper: DatabaseHelper
  lateinit var db: SQLiteDatabase
  init { this.dbHelper = DatabaseHelper(context) }

  fun getRecipeAll(): MutableList<RecizoRecipe> {
    val query = "SELECT title, description, author, imgurl, link FROM ${TABLE_NAME}"
    val list = mutableListOf<RecizoRecipe>()
    this.readableOpen()
    db.rawQuery(query, null).use {
      while (it.moveToNext() ) {
        list.add(RecizoRecipe(
            title = it.getString(it.getColumnIndex("title")),
            description = it.getString(it.getColumnIndex("description")),
            author = it.getString(it.getColumnIndex("author")),
            imgUrl = it.getString(it.getColumnIndex("imgurl")),
            cookpadLink = it.getString(it.getColumnIndex("link")))
        )
      }
    }
    db.close()
    return list
  }

  fun removeRecipe(recipeTitle: String) {
    this.writableOpen()
    val query = "DELETE FROM $TABLE_NAME WHERE title=?"
    db.execSQL(query, arrayOf(recipeTitle) )
    db.close()
  }

  fun addRecipe(recipe: RecizoRecipe) {
    val values = ContentValues()
    values.put("title", recipe.title)
    values.put("description", recipe.description)
    values.put("author", recipe.author)
    values.put("imgurl", recipe.imgUrl)
    values.put("link", recipe.cookpadLink)
    this.writableOpen()
    db.insertOrThrow(TABLE_NAME, null, values)
    db.close()
  }

  fun getRecipe(recipeTitle: String): RecizoRecipe? {
    this.readableOpen()
    val query = "SELECT title, description, author, imgurl, link FROM $TABLE_NAME WHERE title=?"
    var recipe: RecizoRecipe? = null
    db.rawQuery(query, arrayOf(recipeTitle) ).use {
      while(it.moveToNext() ) {
        recipe = RecizoRecipe(
            title = it.getString(it.getColumnIndex("title")),
            description = it.getString(it.getColumnIndex("description")),
            author = it.getString(it.getColumnIndex("author")),
            imgUrl = it.getString(it.getColumnIndex("imgurl")),
            cookpadLink = it.getString(it.getColumnIndex("link") ) )
      }
    }
    db.close()
    return recipe
  }

  private fun writableOpen() { db = dbHelper.writableDatabase }
  private fun readableOpen() { db = dbHelper.readableDatabase }

  companion object {
    val TABLE_NAME = "favorite_recipe_table"
    val DB_NAME = "FavoriteRecipe"
    val DB_VERSION = 1
  }

  inner class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
      val query = "CREATE TABLE ${TABLE_NAME} (" +
          "_id integer primary key autoincrement, " +
          "title char(256) not null, " +
          "description text, " +
          "author char(256), " +
          "imgurl text, " +
          "link text" +
          ")"
      db!!.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }
}