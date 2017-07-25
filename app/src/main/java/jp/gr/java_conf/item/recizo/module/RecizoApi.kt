package jp.gr.java_conf.item.recizo.module

import android.util.Log
import com.google.gson.Gson


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
      override fun onSuccess(body: String) { callback.onSuccess(Gson().fromJson(body, Response::class.java)) }
      override fun onError(code: Http.ErrorCode) { callback.onError(code) }
    }
    http.setCallback(cb)
    http.execute()
  }

  companion object {
    val BASE_URL = "http://recizo.com/api/"
  }
  interface Callback {
    fun onSuccess(response: Response)
    fun onError(errCode: Http.ErrorCode)
  }
  data class DairyData(val date: String, val price: Int)
  class Response {
    val burokkori: Array<DairyData>? = null
    val daikon: Array<DairyData>? = null
    val hakusai: Array<DairyData>? = null
    val hourensou: Array<DairyData>? = null
    val jagaimo: Array<DairyData>? = null
    val kyabetsu: Array<DairyData>? = null
    val kyuri: Array<DairyData>? = null
    val nasu: Array<DairyData>? = null
    val negi: Array<DairyData>? = null
    val ninjin: Array<DairyData>? = null
    val piman: Array<DairyData>? = null
    val retasu: Array<DairyData>? = null
    val satoimo: Array<DairyData>? = null
    val tamanegi: Array<DairyData>? = null
    val tomato: Array<DairyData>? = null
  }
  enum class Vegetables(val name_jp: String) {
    all("All"),
    burokkori("ブロッコリー"),
    daikon("大根"),
    hakusai("白菜"),
    hourensou("ほうれん草"),
    jagaimo("ジャガイモ"),
    kyabetsu("キャベツ"),
    kyuri("きゅうり"),
    nasu("ナス"),
    negi("ネギ"),
    ninjin("にんじん"),
    piman("ピーマン"),
    retasu("レタス"),
    satoimo("里芋"),
    tamanegi("玉ねぎ"),
    tomato("トマト"),
  }
}