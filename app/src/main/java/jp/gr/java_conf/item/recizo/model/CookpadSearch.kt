package jp.gr.java_conf.item.recizo.model

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import kotlinx.android.synthetic.main.loading_spinner.*
import kotlinx.android.synthetic.main.searched_recipe_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Exception
import java.util.zip.Inflater
import kotlin.properties.Delegates

class CookpadSearch(val searchKeyWords: List<String>): AppCompatActivity() {
  val urlBase = "https://cookpad.com/search/"
  var numberOfItem: Int by Delegates.notNull()
  var totalNumberOfPages = 0
  var nowPage = 0

  init{
    this.nowPage = 0
  }

  fun scrapingHTML(progressCallback: ProgressBarCallBack, callback: CookpadCallBack) = launch(UI) {
    progressCallback.progressBarStart()
    val html = getHTML(getSearchUrl(), nowPage).await()
    progressCallback.progressBarStop()

    when(html) {
      "IoError" -> callback.failed(ErrorCode.IO_ERROR)
      "OtherError" -> callback.failed(ErrorCode.GENERIC_ERROR)
      else -> {
        numberOfItem = parseNumberOfItem(html as Document)
        totalNumberOfPages = Math.ceil((numberOfItem.toDouble() / 10)).toInt()
        callback.succeed(html)
      }
    }
  }

  fun pageToFirst() {
    nowPage = 1
  }

  fun pageToLast() {
    nowPage = totalNumberOfPages
  }

  fun pageToNext():Boolean {
    if(nowPage > totalNumberOfPages) {
      return false
    } else {
      nowPage++
      return true
    }
  }

  fun pageToPrev(): Boolean {
    if(nowPage < 1) {
      return false
    } else {
      nowPage--
      return true
    }
  }

  private fun getHTML(url: String, pageNum: Int = 1) = async(CommonPool) {
    // TODO ProgressSpinnerの追加
    val result = try {Jsoup.connect(url + "?order=date&page=$pageNum").get()} catch (e: IOException) {"IoError"} catch (e:Exception){"OtherError"}

    return@async result
  }

  private fun parseNumberOfItem(html: Document): Int {
    val numberOfItemHtml = html.getElementsByClass("count").html()
    val numberOfItemText:String = Jsoup.parse(numberOfItemHtml).getElementsByTag("em").text()
    return numberOfItemText.removeSuffix("品").replace(",".toRegex(), "").toInt()
  }

  private fun getSearchUrl(): String {
    var url = urlBase

    url += searchKeyWords.map { keyword -> "材料：$keyword "}.reduce { acc, s -> "$acc $s" }

    return url
  }
}