package com.recizo.module

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.recizo.R

class RecizoApi {
  private var isRecent = true
  private var vegetable = Vegetables.all
  fun recent(): RecizoApi { this.isRecent = true; return this }
  fun past(): RecizoApi { this.isRecent = false; return this }
  fun vegetable(v: Vegetables): RecizoApi { this.vegetable = v; return this }
  fun all(): RecizoApi { this.vegetable = Vegetables.all; return this }
  fun burokkori(): RecizoApi { this.vegetable = Vegetables.burokkori; return this }
  fun daikon(): RecizoApi { this.vegetable = Vegetables.daikon; return this }
  fun hakusai(): RecizoApi { this.vegetable = Vegetables.hakusai; return this }
  fun hourensou(): RecizoApi { this.vegetable = Vegetables.hourensou; return this }
  fun jagaimo(): RecizoApi { this.vegetable = Vegetables.jagaimo; return this }
  fun kyabetsu(): RecizoApi { this.vegetable = Vegetables.kyabetsu; return this }
  fun kyuri(): RecizoApi { this.vegetable = Vegetables.kyuri; return this }
  fun nasu(): RecizoApi { this.vegetable = Vegetables.nasu; return this }
  fun negi(): RecizoApi { this.vegetable = Vegetables.negi; return this }
  fun ninjin(): RecizoApi { this.vegetable = Vegetables.ninjin; return this }
  fun piman(): RecizoApi { this.vegetable = Vegetables.piman; return this }
  fun retasu(): RecizoApi { this.vegetable = Vegetables.retasu; return this }
  fun satoimo(): RecizoApi { this.vegetable = Vegetables.satoimo; return this }
  fun tamanegi(): RecizoApi { this.vegetable = Vegetables.tamanegi; return this }
  fun tomato(): RecizoApi { this.vegetable = Vegetables.tomato; return this }

  fun get(callback: Callback) {
    val url = "$BASE_URL${if(this.isRecent) "recent" else "past"}/${if(this.vegetable == Vegetables.all) "" else this.vegetable.name}"
    val http = Http(url)
    val cb = object : Http.Callback {
      override fun onSuccess(body: String) {
        val typeToken = object : TypeToken<Map<String, Collection<DairyData>>>() {}
        val res: Map<String, ArrayList<DairyData>> = Gson().fromJson(body, typeToken.type)
        callback.onSuccess(res) }
      override fun onError(code: Http.ErrorCode) { callback.onError(code) }
    }
    http.setCallback(cb)
    http.execute()
  }

  companion object { val BASE_URL = "http://recizo.com/api/" }

  interface Callback {
    fun onSuccess(response: Map<String, List<DairyData>>)
    fun onError(errCode: Http.ErrorCode)
  }

  data class DairyData(val date: String, val price: Int)

  enum class Vegetables(val name_jp: String, val resource: Int) {
    kyabetsu("キャベツ", R.drawable.cat_vegetable),
    kyuri("きゅうり", R.drawable.vege_kyuuri),
    satoimo("里芋", R.drawable.vege_satoimo),
    jagaimo("ジャガイモ", R.drawable.vege_jagaimo),
    tamanegi("玉ねぎ", R.drawable.vege_tamanegi),
    daikon("大根", R.drawable.vege_daikon),
    tomato("トマト", R.drawable.ic_cancel),
    nasu("ナス", R.drawable.ic_cancel),
    ninjin("にんじん", R.drawable.ic_cancel),
    negi("ネギ", R.drawable.ic_cancel),
    hakusai("白菜", R.drawable.ic_cancel),
    piman("ピーマン", R.drawable.ic_cancel),
    burokkori("ブロッコリー", R.drawable.ic_cancel),
    hourensou("ほうれん草", R.drawable.ic_cancel),
    retasu("レタス", R.drawable.ic_cancel),
    all("全て", R.drawable.ic_cancel),
  }
}