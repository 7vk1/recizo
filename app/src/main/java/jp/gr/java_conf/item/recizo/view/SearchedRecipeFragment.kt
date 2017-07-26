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
import jp.gr.java_conf.item.recizo.module.CookpadScraper
import jp.gr.java_conf.item.recizo.presenter.RecipeListAdapter
import kotlinx.android.synthetic.main.searched_recipe_list.*
import org.jsoup.nodes.Document
import jp.gr.java_conf.item.recizo.module.CookpadScraper.Recipe.*


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

    val test = CookpadScraper(listOf("りんご") )

    test.pageToNext()
    for(i in 0..2) {

      test.scraping(object: ProgressBarCallBack {
        override fun progressBarStart() {
          searched_recipe_progressBar.visibility = View.VISIBLE
        }

        override fun progressBarStop() {
          searched_recipe_progressBar.visibility = View.GONE
        }
      }, object : CookpadCallBack {
        override fun succeed(html: Document?) {
          val recipes = test.requestGetRecipeItem(html)

          Log.d("TEST5",recipes.size.toString())
          recipes.forEach {
            recipeListAdapter.addRecipe(it)
          }

        }
        override fun failed(errorCode: ErrorCode) {
          Log.d("TEST ERROR CODE", errorCode.toString())
        }
      })
    }
  }
}