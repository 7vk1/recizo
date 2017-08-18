package com.recizo.presenter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.recizo.R
import com.recizo.model.ErrorCode
import com.recizo.model.entity.CookpadRecipe
import com.recizo.module.CookpadScraper
import com.recizo.module.Http
import com.recizo.module.Scraper
import com.recizo.view.RecipeFragment
import com.recizo.view.SearchItemView

class RecipePresenter (val context: Activity,val view: View, val keywords: Set<String>){
  private val recipeListView: RecyclerView = view.findViewById(R.id.searched_recyclerView)
  // TODO 食材選択方法が決まるまで借り置き
  private var scraper = CookpadScraper("なす")
  private var loadEventListener: LoadEventListener? = null
  private val recipeListAdapter = RecipeListAdapter(recipeListView)
  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(object: RecipeListAdapter.OnItemClickListener {
      override fun onItemClick(recipe: CookpadRecipe) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.cookpadLink)))
      }
    })
  }

  fun setLoadEventListener(listener: LoadEventListener) { loadEventListener = listener }

  fun startRecipeListCreate() {
    val errorMes = view.findViewById<LinearLayout>(R.id.error_mes_box)
    errorMes.visibility = View.INVISIBLE
    loadEventListener?.onLoadStart()

    scraper.get(object: CookpadScraper.Callback {
      override fun onError(errCode: Http.ErrorCode) {
        println("$errCode")
      }

      override fun onSuccess(response: Map<String, List<CookpadScraper.Recipe>>) {
        println("SUCCESS")
        val recipeList: MutableList<CookpadRecipe> = mutableListOf()

        response.get("result")?.map {
          recipeList.add(CookpadRecipe(
              title = it.recipeTitle,
              imgUrl = it.foodImageUrl,
              description = it.recipeDescription,
              author = it.nickname,
              cookpadLink = it.recipeUrl
          ))
        }
        recipeList.forEach { recipeListAdapter.addRecipe(it) }
        loadEventListener?.onLoadEnd()
      }
    })




//    scraper.scrapingHTML(object : Scraper.ScraperCallBack {
//      override fun succeed(html: org.jsoup.nodes.Document?) {
//        val recipes = scraper.requestGetRecipeItem(html)
//        if(recipes.isEmpty()) {
//          setErrorMesText(R.string.searched_notfound_title, R.string.searched_notfound_detail)
//          errorMes.visibility = View.VISIBLE
//        }
//        recipes.forEach {recipeListAdapter.addRecipe(it)}
//        loadEventListener?.onLoadEnd()
//      }
//      override fun failed(errorCode: ErrorCode) {
//        if(errorCode.name == ErrorCode.IO_ERROR.name) {
//          setErrorMesText(R.string.network_error_title, "Wifiまたはデータ通信がオフになっていませんか？\nオンになっている場合は", createSpannableStringToReload(" リロード "), "を試してください")
//          view.findViewById<TextView>(R.id.error_mes_detail).movementMethod = LinkMovementMethod.getInstance()
//        }
//        // 検索食材を全部削除した際に起きる
//        else if(errorCode.name == ErrorCode.UNSUPPORTED_OPERATION_ERROR.name) setErrorMesText(R.string.searched_notfound_title, R.string.searched_notfound_detail)
//        else setErrorMesText(R.string.other_error_title, R.string.other_error_detail)
//        recipeListAdapter.clearRecipe()
//        errorMes.visibility = View.VISIBLE
//        loadEventListener?.onLoadEnd()
//      }
//    })
  }

  private fun createSpannableStringToReload(text: String): SpannableString {
    val link = SpannableString(text)

    link.setSpan(object : ClickableSpan() {
      override fun onClick(textView: View) {
        startRecipeListCreate()
      }
    }, 1, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return link
  }

  private fun setErrorMesText(title: Int, detail: Int) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    view.findViewById<TextView>(R.id.error_mes_detail).text = context.resources.getString(detail)
  }

  private fun setErrorMesText(title: Int, detailBefore: String, link: SpannableString, detailAfter: String) {
    view.findViewById<TextView>(R.id.error_mes_title).text = context.resources.getString(title)
    val errorDetail = view.findViewById<TextView>(R.id.error_mes_detail)
    errorDetail.text = detailBefore
    errorDetail.append(link)
    errorDetail.append(detailAfter)
  }

  fun displaySearchedText(parent: LinearLayout) {
    keywords.map {
      val view = SearchItemView(context ,it)
      view.setOnCloseClickListener(object : SearchItemView.OnCloseClickListener{
        override fun onClick(item: String) { eraseKeyword(item) }
      })
      parent.addView(view)
    }
  }

//  fun addRecipeList(recyclerView: RecyclerView?, dy: Int){
//    if (dy == 0 || scraper.isLoading || scraper.idFinished()) return
//    val layoutManager = recyclerView!!.layoutManager as android.support.v7.widget.LinearLayoutManager
//    val totalItemCount = layoutManager.itemCount
//    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
//    if (totalItemCount < lastVisibleItem + 5) {
//      scraper.isLoading = true
//      startRecipeListCreate()
//    }
//  }

  private fun eraseKeyword(v: String) {
    val transaction = context.fragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    transaction.replace(R.id.fragment_frame, RecipeFragment(keywords.filter { it != v }.toSet()))
    transaction.commit()
  }

  interface LoadEventListener{
    fun onLoadStart()
    fun onLoadEnd()
  }
}

