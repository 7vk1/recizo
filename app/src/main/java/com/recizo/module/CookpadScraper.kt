package com.recizo.module

import com.recizo.model.entity.CookpadRecipe
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CookpadScraper(private var searchKeyWords: Set<String>): Scraper() {
  init{ this.queryString = "?order=date&page=" }

  fun pageInit() { this.nowPage = 1 }

  fun requestGetRecipeItem(html: Document?): List<CookpadRecipe> {
    val recipes = html!!.select(".recipe-preview").filter{ !it.parent().hasClass("recommended_pro_recipe")}
    return recipes.map {
      CookpadRecipe(
          title = it.select(".recipe-title")[0].text(),
          description = it.select(".recipe_description")[0].text(),
          cookpadLink = CookpadScraper.BASE_URL + it.select(".recipe-title")[0].attr("href"),
          author = it.select(".recipe_author_name a")[0].text(),
          imgUrl = it.select(".recipe-image img")[0].attr("src")
      )
    }
  }

  override fun parseNumberOfItem(html: Document): Int {
    val numberOfItemHtml = html.getElementsByClass("count").html()
    val numberOfItemText:String = Jsoup.parse(numberOfItemHtml).getElementsByTag("em").text()
    return numberOfItemText.removeSuffix("品").replace(",".toRegex(), "").toInt()
  }

  override fun getSearchUrl(): String {
    var url = BASE_URL + "/search/"
    //url += searchKeyWords.map { keyword -> "材料：$keyword "}.reduce { acc, s -> "$acc $s" }
    url += searchKeyWords.map { keyword -> "$keyword "}.reduce { acc, s -> "$acc $s" }
    url += this.queryString + nowPage
    //Log.d("url",url)
    return url
  }

  override fun getTotalPage(): Int{ return Math.ceil((numberOfItem.toDouble() / 10)).toInt() }

  companion object { val BASE_URL = "https://cookpad.com" }
}