package jp.gr.java_conf.item.recizo.module

import com.google.gson.Gson


val BASE_URL = "http://recizo.com/api/"
class RecizoApi {
  interface Callback {
    fun onSuccess(v: Response)
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
  enum class Vegetables {
    all,
    burokkori,
    daikon,
    hakusai,
    hourensou,
    jagaimo,
    kyabetsu,
    kyuri,
    nasu,
    negi,
    ninjin,
    piman,
    retasu,
    satoimo,
    tamanegi,
    tomato,
  }
  private var isRecent = true
  private var vegetable = Vegetables.all
  fun recent() { this.isRecent = true }
  fun past() { this.isRecent = false }

  fun vegetable(v: Vegetables) { this.vegetable = v }
  fun all() { this.vegetable = Vegetables.all }
  fun burokkori() { this.vegetable = Vegetables.burokkori }
  fun daikon() { this.vegetable = Vegetables.daikon }
  fun hakusai() { this.vegetable = Vegetables.hakusai }
  fun hourensou() { this.vegetable = Vegetables.hourensou }
  fun jagaimo() { this.vegetable = Vegetables.jagaimo }
  fun kyabetsu() { this.vegetable = Vegetables.kyabetsu }
  fun kyuri() { this.vegetable = Vegetables.kyuri }
  fun nasu() { this.vegetable = Vegetables.nasu }
  fun negi() { this.vegetable = Vegetables.negi }
  fun ninjin() { this.vegetable = Vegetables.ninjin }
  fun piman() { this.vegetable = Vegetables.piman }
  fun retasu() { this.vegetable = Vegetables.retasu }
  fun satoimo() { this.vegetable = Vegetables.satoimo }
  fun tamanegi() { this.vegetable = Vegetables.tamanegi }
  fun tomato() { this.vegetable = Vegetables.tomato }

  fun get(callback: Callback) {
    val http = Http("$BASE_URL + ${if(this.isRecent) "recent" else "past"}/${if(this.vegetable == Vegetables.all) "" else this.vegetable.name}")
    val cb = object : Http.Callback {
      override fun onSuccess(body: String) {
        val gson = Gson()
        callback.onSuccess(gson.fromJson(body, Response::class.java))
      }
      override fun onError(code: Http.ErrorCode) {
        callback.onError(code)
      }
    }
    http.setCallback(cb)
    http.execute()
  }
}