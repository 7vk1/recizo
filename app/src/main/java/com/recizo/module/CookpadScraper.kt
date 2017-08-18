package com.recizo.module

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CookpadScraper(private var searchCategory: String) {
  fun get(callback: CookpadScraper.Callback) {
    val url = "$BASE_URL/recipe?category=$searchCategory"
    val http = Http(url, API_KEY)
    val cb = object : Http.Callback {
      override fun onSuccess(body: String) {
        val typeToken = object : TypeToken<Collection<Collection<Recipe>>>() {}
        val res: ArrayList<ArrayList<Recipe>> = Gson().fromJson(body, typeToken.type)
        callback.onSuccess(res)
      }
      override fun onError(code: Http.ErrorCode) { callback.onError(code) }
    }
    http.setCallback(cb)
    http.execute()
  }

  class Recipe(
      val foodImageUrl: String,
      val nickname: String,
      val recipeDescription: String,
      val recipeTitle: String,
      val recipeUrl: String
      ) {}

  interface Callback {
    fun onSuccess(response: List<List<Recipe>>)
    fun onError(errCode: Http.ErrorCode)
  }

  companion object {
    val BASE_URL = "http://recizo.com/api/"
    val API_KEY = "Token 7500ae50c452593029a999f067cd6e699745c6a2a6702b9ba888d59ffbf02e37f84db6312e139ed684db36a7764d73610f1db63ecedd9fe0bc766b70e69d4f13"
  }
}