package com.recizo.module


import com.recizo.model.entity.ShufooFlyer
import org.jsoup.nodes.Document

class ShufooScraper(postCode: String): Scraper(){

  companion object {
    val BASE_URL = "http://www.shufoo.net/pntweb/chirashiList.php"
  }

  init{
    this.queryString = "?keyword=$postCode&sort=distance&categoryId=101&page="
  }

  override fun parseNumberOfItem(html: Document): Int {
    return html.select(".result_count")[0].text().replace("枚","").replace(""".+/""".toRegex(), "").toInt()
  }

  override fun getSearchUrl(): String {
    return BASE_URL + this.queryString + nowPage
  }

  override fun getTotalPage(): Int{
    return Math.ceil((numberOfItem.toDouble() / 32)).toInt()
  }

  fun requestGetShufooItem(html: Document?): List<ShufooFlyer> {

    val flyers = html!!.select(".chirashi_list_item")

    return flyers.map {
      ShufooFlyer(
              storeName = it.select(".chirashi_list_item_name_str")[0].text(),
              description = it.select(".chirashi_list_item_title")[0].text(),
              shufooLink = it.select(".hover_opacity")[0].attr("href")
      )
    }
  }
}
