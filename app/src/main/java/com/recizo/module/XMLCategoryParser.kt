package com.recizo.module

import android.content.res.Resources
import android.util.Xml
import org.xmlpull.v1.XmlPullParser

class XMLCategoryParser(resources: Resources) {
  val parser: XmlPullParser = Xml.newPullParser()
  init {
    val maneger = resources.assets
    val stream = maneger.open("category.xml")
    parser.setInput(stream , "UTF-8")
  }

  fun parse(): List<String> {
    var eventType = parser.eventType
    val categoryList = mutableListOf<String>()
    while(eventType != XmlPullParser.END_DOCUMENT) {
      if(parser.name == "category" && eventType == XmlPullParser.START_TAG) {
        categoryList.add(parser.getAttributeValue(null, "name"))
      }
      eventType = parser.next()
    }
    return categoryList.toList()
  }
}