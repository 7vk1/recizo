package jp.gr.java_conf.item.recizo.model

import jp.gr.java_conf.item.recizo.module.Search
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CookpadSearch(val searchKeyWords: List<String>): Search() {

  init{
    this.urlBase = "https://cookpad.com/search/"
    this.queryString = "?order=date&page="
  }

  override fun parseNumberOfItem(html: Document): Int {
    val numberOfItemHtml = html.getElementsByClass("count").html()
    val numberOfItemText:String = Jsoup.parse(numberOfItemHtml).getElementsByTag("em").text()
    return numberOfItemText.removeSuffix("品").replace(",".toRegex(), "").toInt()
  }

  override fun getSearchUrl(): String {
    var url = urlBase

    url += searchKeyWords.map { keyword -> "材料：$keyword "}.reduce { acc, s -> "$acc $s" }

    return url
  }
}