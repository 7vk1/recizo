package jp.gr.java_conf.item.recizo.module

import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CookpadScraper(val searchKeyWords: List<String>): Scraper() {

  companion object {
    val BASE_URL = "https://cookpad.com"
  }

  init{
    this.queryString = "?order=date&page="
  }

  override fun parseNumberOfItem(html: Document): Int {
    val numberOfItemHtml = html.getElementsByClass("count").html()
    val numberOfItemText:String = Jsoup.parse(numberOfItemHtml).getElementsByTag("em").text()
    return numberOfItemText.removeSuffix("品").replace(",".toRegex(), "").toInt()
  }

  override fun getSearchUrl(): String {
    var url = BASE_URL + "/search/"

    url += searchKeyWords.map { keyword -> "材料：$keyword "}.reduce { acc, s -> "$acc $s" }

    return url
  }


  fun requestGetRecipeItem(html: Document?): List<List<String>> {

    val imgUrl = html!!.select(".recipe-image img").map { v -> v.attr("src") }
    val title = html.select(".recipe-title").map { v -> v.text() }
    val description = html.select(".recipe_description").map { v -> v.text() }
    val cookpadLinkUrl = html.select(".recipe-title").map { v -> CookpadScraper.BASE_URL + v.attr("href") }
    val author = html.select(".recipe_author_name a").map { v -> v.text() }
    return listOf<List<String>>(title, description, imgUrl, cookpadLinkUrl, author)
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



  enum class Recipe(val num: Int) {
    TITLE(0),
    DESCRIPTION(1),
    IMG_URL(2),
    LINK_URL(3),
    AUTHOR(4)
  }
}