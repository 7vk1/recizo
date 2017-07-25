package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.entity.CookpadRecipe
import jp.gr.java_conf.item.recizo.model.ErrorCode
import jp.gr.java_conf.item.recizo.presenter.RecipeListAdapter
import jp.gr.java_conf.item.recizo.presenter.ScrapingAdapter
import kotlinx.android.synthetic.main.searched_recipe_list.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.experimental.buildSequence

class SearchedRecipeFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.searched_recipe_list, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onStart() {
    super.onStart()

    searched_recyclerView.layoutManager = LinearLayoutManager(activity)
    val recipeListAdapter = RecipeListAdapter()
    val dividerItemDecoration = DividerItemDecoration(searched_recyclerView.context,
        LinearLayoutManager(activity).orientation)
    searched_recyclerView.addItemDecoration(dividerItemDecoration)
    searched_recyclerView.adapter = recipeListAdapter

    val test = ScrapingAdapter(listOf("りんご") )

    for(i in 0..2) {
      test.pageToNext()

      test.cookpadScraping(object: ProgressBarCallBack {
        override fun progressBarStart() {
          searched_recipe_progressBar.visibility = View.VISIBLE
        }

        override fun progressBarStop() {
          searched_recipe_progressBar.visibility = View.GONE
        }
      }, object : CookpadCallBack {
        private val cookpadUrlBase = "https://cookpad.com"
        override fun succeed(html: Document?) {
          val recipesHtml = buildSequence {
            for (i in 0..9) yield(html?.getElementById("recipe_$i")?.html())
          }

          recipesHtml.filterNotNull().forEach { recipe ->
            val recipeDoc = Jsoup.parse(recipe)
            val imgUrl = Jsoup.parse(recipeDoc.getElementsByClass("recipe-image").html() ).getElementsByTag("img").attr("src")
            val title = recipeDoc.getElementsByClass("recipe-title").text()
            val description = recipeDoc.getElementsByClass("recipe_description").text()
            val cookpadLinkUrl = cookpadUrlBase + recipeDoc.getElementsByClass("recipe-title").attr("href")
            val author = Jsoup.parse(recipeDoc.getElementsByClass("recipe_author_name").html() ).getElementsByTag("a").text()

            recipeListAdapter.addRecipe(CookpadRecipe(title, description, imgUrl, cookpadLinkUrl, author))
          }

        }

        override fun failed(errorCode: ErrorCode) {
          Log.d("TEST ERROR CODE", errorCode.toString())
        }
      })
    }
  }
}