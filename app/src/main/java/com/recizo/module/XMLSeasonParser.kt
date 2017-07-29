package com.recizo.module

import android.content.res.Resources
import android.util.Xml
import org.xmlpull.v1.XmlPullParser

class XMLSeasonParser(resouces: Resources) {
  val parser = Xml.newPullParser()
  init {
    val maneger = resouces.assets
    val stream = maneger.open("foods.xml")
    parser.setInput(stream , "UTF-8")
  }

  fun parseSeason(): List<List<String>> {
    var eventType = parser.eventType
    val seasonList = mutableListOf<List<String>>()
    while(eventType != XmlPullParser.END_DOCUMENT) {
      if(isSeasonStartTag(eventType)) {
        val list = mutableListOf<String>()
        while (!isSeasonEndTag(eventType)) {
          if(isCategoryStartTag(eventType)) list.add(Category.valueOf(parser.name).name_jp)
          if(isFoodStartTag(eventType)) list.add(getFoodName())
          eventType = parser.next()
        }
        seasonList.add(list)
      }
      eventType = parser.next()
    }
    return seasonList
  }

  fun isSeasonStartTag(eventType: Int): Boolean {
    if(parser.name == "season" && eventType == XmlPullParser.START_TAG) {
      return true
    }
    return false
  }

  fun isSeasonEndTag(eventType: Int): Boolean {
    if(parser.name == "season" && eventType == XmlPullParser.END_TAG) {
      return true
    }
    return false
  }

  fun isCategoryStartTag(eventType: Int): Boolean {
    if(Category.values().map { it.name }.contains(parser.name) && eventType == XmlPullParser.START_TAG) return true
    return false
  }

  fun isCategoryEndTag(eventType: Int): Boolean {
    if(Category.values().map { it.name }.contains(parser.name) && eventType == XmlPullParser.END_TAG) return true
    return false
  }

  fun isFoodStartTag(eventType: Int): Boolean {
    if(parser.name == "food" && eventType == XmlPullParser.START_TAG) return true
    return false
  }

  fun getFoodName(): String {
    if(parser.attributeCount == 1) {
      return parser.getAttributeValue(null, "name")
    }
    return "-"
  }

  enum class Category(val name_jp: String) {
    vegetable("野菜"),
    imo_mushroom("芋・キノコ"),
    fruit_nut("果物・きのみ"),
    fish_seafood("魚・貝")
  }
}