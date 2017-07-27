package jp.gr.java_conf.item.recizo.module

import android.util.Log
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import jp.gr.java_conf.item.recizo.model.entity.CookpadRecipe
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


  fun requestGetRecipeItem(html: Document?): List<CookpadRecipe> {

    val recipes = html!!.select(".recipe-preview").filter{ !it.parent().hasClass("recommended_pro_recipe")}


    return recipes.map {
      CookpadRecipe(
              title = it.select(".recipe-title")[0].text(),
              description = it.select(".recipe_description")[0].text(),
              cookpadLink = CookpadScraper.BASE_URL + it.select(".recipe-image a")[0].attr("href"),
              author = it.select(".recipe_author_name a")[0].text(),
              imgUrl = it.select(".recipe-image img")[0].attr("src")
      )
    }
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