package jp.gr.java_conf.item.recizo.view

import android.util.Log
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.model.CookpadRecipe
import jp.gr.java_conf.item.recizo.model.ErrorCode
import jp.gr.java_conf.item.recizo.presenter.ScrapingAdapter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.experimental.buildSequence


// How to ScrapingAdapter
class CookpadRecipesTemp {

  fun Temp() {

    val test = ScrapingAdapter(listOf("りんご") )

    for(i in 0..2) {
      test.pageToNext()

      test.cookpadScraping(object : CookpadCallBack {
        private val cookpadUrlBase = "https://cookpad.com"
        override fun succeed(html: Document?) {
          val recipesHtml = buildSequence {
            for (i in 0..9) yield(html?.getElementById("recipe_$i")?.html())
          }

          val recipes = (buildSequence {
            recipesHtml.filterNotNull().forEach { recipe ->
              val recipeDoc = Jsoup.parse(recipe)
              val imgUrl = Jsoup.parse(recipeDoc.getElementsByClass("recipe_image").html()).getElementsByTag("img").attr("src")
              val title = recipeDoc.getElementsByClass("recipe-title").text()
              val description = recipeDoc.getElementsByClass("recipe_description").text()
              val cookpadLinkUrl = cookpadUrlBase + recipeDoc.getElementsByClass("recipe-title").attr("href")
              yield(CookpadRecipe(title, description, imgUrl, cookpadLinkUrl))
            }
          })

          recipes.forEach {
            Log.d("TEST RECIPE", it.title)
          }
        }

        override fun failed(errorCode: ErrorCode) {
          Log.d("TEST ERROR CODE", errorCode.toString())
        }
      })
    }


  }
}