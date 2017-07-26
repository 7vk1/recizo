package jp.gr.java_conf.item.recizo.module

import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.entity.ShufooFlyer
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

  fun requestGetShufooItem(html: Document?): List<ShufooFlyer> {

    val flyers = html!!.select(".chirashi_list_item")

    return flyers.map {
      ShufooFlyer(
              storeName = it.select(".chirashi_list_item_name_str")[0].text(),
              description = it.select(".chirashi_list_item_title")[0].text(),
              shufooLink = CookpadScraper.BASE_URL + it.select(".hover_opacity")[0].attr("href")
      )
    }
  }


  fun scraping(progressCallback: ProgressBarCallBack, cookpadCallback: CookpadCallBack){
    this.scrapingHTML(cookpadCallback)
  }

  enum class Flyer(val num: Int) {
    STORE(0),
    DESCRIPTION(1),
    URL(2)
  }

}
