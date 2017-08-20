package com.recizo.module

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RecizoRecipeApi(private var searchCategory: MutableList<String>) {
  init { Collections.shuffle(searchCategory) }

  fun get(callback: RecizoRecipeApi.Callback) {
    // リストが空の場合にEMPTY_BODYを起こすために空を入れる
    if(searchCategory.isEmpty()) {searchCategory.add("")}
    val url = "${BASE_URL}recipe?category=${searchCategory[0]}"
    println("URL:$url")
    searchCategory = searchCategory.drop(1).toMutableList()
    val http = Http(url, API_KEY)
    val cb = object : Http.Callback {
      override fun onSuccess(body: String) {
        val typeToken = object : TypeToken<Map<String, Collection<Recipe>>>() {}
        val res: Map<String, ArrayList<Recipe>> = Gson().fromJson(body, typeToken.type)
        callback.onSuccess(res)
      }
      override fun onError(code: Http.ErrorCode) { callback.onError(code) }
    }

    http.setCallback(cb)
    http.execute()
  }

  fun isFinished(): Boolean {
    if(searchCategory.isEmpty()) return true
    return false
  }

  class Recipe(
      val foodImageUrl: String,
      val nickname: String,
      val recipeDescription: String,
      val recipeTitle: String,
      val recipeUrl: String
      )

  interface Callback {
    fun onSuccess(response: Map<String, List<Recipe>>)
    fun onError(errCode: Http.ErrorCode)
  }

  companion object {
    val BASE_URL = "http://recizo.com/api/"
    val API_KEY = "Token 7500ae50c452593029a999f067cd6e699745c6a2a6702b9ba888d59ffbf02e37f84db6312e139ed684db36a7764d73610f1db63ecedd9fe0bc766b70e69d4f13"
  }
}