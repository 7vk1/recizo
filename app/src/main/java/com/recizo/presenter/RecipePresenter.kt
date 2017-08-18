package com.recizo.presenter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.recizo.R
import com.recizo.model.database.CategoryDatabaseHelper
import com.recizo.model.entity.RecizoRecipe
import com.recizo.module.RecizoRecipeApi
import com.recizo.module.Http
import com.recizo.view.RecipeFragment
import com.recizo.view.SearchItemView

class RecipePresenter (val context: Activity,val view: View, val keywords: Set<String>){
  private val recipeListView: RecyclerView = view.findViewById(R.id.searched_recyclerView)
  private var recizoRecipe: RecizoRecipeApi? = null
  private var loadEventListener: LoadEventListener? = null
  private val recipeListAdapter = RecipeListAdapter(recipeListView)
  private var recipeListMaster = mutableListOf<RecizoRecipe>()
  init {
    recipeListView.adapter = recipeListAdapter
    recipeListAdapter.setOnItemClickListener(object: RecipeListAdapter.OnItemClickListener {
      override fun onItemClick(recipe: RecizoRecipe) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.cookpadLink)))
      }
    })

    val categoryList = mutableListOf<String>()
    val categoryDbHelper = CategoryDatabaseHelper(context)
    keywords.map {
      val list = categoryDbHelper.getItem(it)
      list.map { categoryList.add(it) }
    }
    recizoRecipe = RecizoRecipeApi(categoryList)
  }

  fun setLoadEventListener(listener: LoadEventListener) { loadEventListener = listener }

  fun startRecipeListCreate() {
    val errorMes = view.findViewById<LinearLayout>(R.id.error_mes_box)
    errorMes.visibility = View.INVISIBLE
    loadEventListener?.onLoadStart()
    recizoRecipe!!.get(object: RecizoRecipeApi.Callback {
      override fun onError(errCode: Http.ErrorCode) {
        if(errCode == Http.ErrorCode.CONNECTION_ERROR) {
          setErrorMesText(R.string.network_error_title, "Wifiまたはデータ通信がオフになっていませんか？\nオンになっている場合は", createSpannableStringToReload(" リロード "), "を試してください")
          view.findViewById<TextView>(R.id.error_mes_detail).movementMethod = LinkMovementMethod.getInstance()
        } else if(errCode == Http.ErrorCode.EMPTY_BODY) {
          setErrorMesText(R.string.searched_notfound_title, R.string.searched_notfound_detail)
          errorMes.visibility = View.VISIBLE
        } else setErrorMesText(R.string.other_error_title, R.string.other_error_detail)
        recipeListAdapter.clearRecipe()
        errorMes.visibility = View.VISIBLE
        loadEventListener?.onLoadEnd()
      }

      override fun onSuccess(response: Map<String, List<RecizoRecipeApi.Recipe>>) {
        val SIZE_FORMAT = "?thum=51"
        val recipeList: MutableList<RecizoRecipe> = mutableListOf()

        if(recizoRecipe!!.isFinished() && response.get("result")!!.isEmpty()) {
          setErrorMesText(R.string.searched_notfound_title, R.string.searched_notfound_detail)
          recipeListAdapter.clearRecipe()
          errorMes.visibility = View.VISIBLE
          loadEventListener?.onLoadEnd()
        }

        response.get("result")?.map {
          val recipe = RecizoRecipe(
              title = it.recipeTitle,
              imgUrl = it.foodImageUrl + SIZE_FORMAT,
              description = it.recipeDescription,
              author = it.nickname,
              cookpadLink = it.recipeUrl
          )
          var isDuplicate = false
          recipeListMaster.forEach { if(it.title == recipe.title) isDuplicate = true }
          if(!isDuplicate) recipeList.add(recipe)

        }
        recipeListMaster = recipeListMaster.plus(recipeList).toMutableList()
        recipeListMaster.map { println(it.title) }
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

  fun addRecipeList(recyclerView: RecyclerView?, dy: Int){
    if (dy == 0 || recizoRecipe!!.isFinished()) return
    val layoutManager = recyclerView!!.layoutManager as android.support.v7.widget.LinearLayoutManager
    val totalItemCount = layoutManager.itemCount
    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
    if (totalItemCount < lastVisibleItem + 5) {
      startRecipeListCreate()
    }
  }

  private fun eraseKeyword(v: String) {
    // TODO keywordをすべて削除された時のメッセージを出す
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

