package jp.gr.java_conf.item.recizo.module

import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import org.jsoup.nodes.Document

class ShufooScraper(val postCode: String): Scraper(){

  companion object {
    val BASE_URL = "http://www.shufoo.net/pntweb/chirashiList.php/"
  }

  init{
    this.queryString = "?keyword=$postCode&sort=&categoryId=101&page="
  }

  override fun parseNumberOfItem(html: Document): Int {
    val numberOfItemHtml = html.getElementsByClass("result_count").text()
    //return numberOfItemHtml.removeSuffix("品").replace(",".toRegex(), "").toInt()
    return 1
  }

  override fun getSearchUrl(): String {
    return BASE_URL
  }

  fun requestGetShufooItem(html: Document?): List<List<String>> {
    val elements = html!!.select(".chirashi_list_box")
    val storeName = elements.select(".chirashi_list_item_name_str").map { v -> v.text() }
    val description = elements.select(".chirashi_list_item_title").map { v -> v.text() }
    val shufooLinkUrl = elements.select(".hover_opacity").map { v -> v.attr("href") }
    return listOf<List<String>>(storeName,description,shufooLinkUrl)
  }


  fun scraping(progressCallback: ProgressBarCallBack, cookpadCallback: CookpadCallBack){
    this.scrapingHTML(object: ProgressBarCallBack {
      override fun progressBarStart() {
        progressCallback.progressBarStart()
      }

      override fun progressBarStop() {
        progressCallback.progressBarStop()
      }

    }, object: CookpadCallBack {
      override fun succeed(html: Document?) {
        cookpadCallback.succeed(html)
      }

      override fun failed(errorCode: ErrorCode) {
        cookpadCallback.failed(errorCode)
      }
    })
  }

  enum class Flyer(val num: Int) {
    STORE(0),
    DESCRIPTION(1),
    URL(2)
  }

}
