package jp.gr.java_conf.item.recizo.module

import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Exception
import kotlin.properties.Delegates

abstract class Search {
  var urlBase = ""
  var queryString = ""
  var numberOfItem: Int by Delegates.notNull()
  var totalNumberOfPages = 0
  var nowPage = 0

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

  fun pageToNext():Boolean {
    if(nowPage > totalNumberOfPages) return false
    else {
      nowPage++
      return true
    }
  }
  fun pageToPrev(): Boolean {
    if(nowPage < 1) return false
    else {
      nowPage--
      return true
    }
  }

  private fun getHTML(url: String, pageNum: Int = 1) = async(CommonPool) {
    // TODO ProgressSpinnerの追加
    val result = try {Jsoup.connect(url + queryString + pageNum).get()} catch (e: IOException) {"IoError"} catch (e:Exception){"OtherError"}
    return@async result
  }

  abstract protected fun parseNumberOfItem(html: Document): Int
  abstract protected fun getSearchUrl(): String
}