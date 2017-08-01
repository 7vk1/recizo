package com.recizo.module

import android.content.res.Resources
import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser

class XMLSeasonParser(resources: Resources) {
  val parser: XmlPullParser = Xml.newPullParser()
  init {
    val maneger = resources.assets
    val stream = maneger.open("foods.xml")
    parser.setInput(stream , "UTF-8")
  }

  fun parseSeason(): List<List<String>> {
    var eventType = parser.eventType
    val seasonList = mutableListOf<List<String>>()
    while(eventType != XmlPullParser.END_DOCUMENT) {
      if(isSeasonStartTag(eventType)) {
        val monthItems = mutableListOf<String>()
        while (!isSeasonEndTag(eventType)) {
          // 旬が無い分類の読み飛ばし
          if(isCategoryStartTag(eventType) && !isCategoryEmpty(eventType)) monthItems.add(getCategoryName())
          if(isFoodStartTag(eventType)) monthItems.add(getFoodName())
          eventType = parser.next()
        }
        seasonList.add(monthItems)
      }
      eventType = parser.next()
    }
    return seasonList
  }

  private fun isSeasonStartTag(eventType: Int): Boolean {
    if(parser.name == "season" && eventType == XmlPullParser.START_TAG) {
      return true
    }
    return false
  }

  private fun isSeasonEndTag(eventType: Int): Boolean {
    if(parser.name == "season" && eventType == XmlPullParser.END_TAG) {
      return true
    }
    return false
  }

  private fun isCategoryEmpty(eventType: Int): Boolean {
    if(parser.getAttributeValue(null, "empty") == "true") return true
    return false
  }

  private fun isCategoryStartTag(eventType: Int): Boolean {
    if(Category.values().map { it.name }.contains(parser.name) && eventType == XmlPullParser.START_TAG) return true
    return false
  }

  private fun getCategoryName(): String {
    return Category.valueOf(parser.name).name_jp
  }

  private fun isCategoryEndTag(eventType: Int): Boolean {
    if(Category.values().map { it.name }.contains(parser.name) && eventType == XmlPullParser.END_TAG) return true
    return false
  }

  private fun isFoodStartTag(eventType: Int): Boolean {
    if(parser.name == "food" && eventType == XmlPullParser.START_TAG) return true
    return false
  }

  private fun getFoodName(): String {
    if(parser.attributeCount == 1) {
      return parser.getAttributeValue(null, "name")
    }
    return "-"
  }

  private enum class Category(val name_jp: String) {
    vegetable("野菜"),
    mushroom("キノコ"),
    fruit("果物"),
    seafood("魚貝"),
    other("その他")
  }
}