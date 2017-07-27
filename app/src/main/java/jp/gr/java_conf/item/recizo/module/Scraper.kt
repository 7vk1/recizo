package jp.gr.java_conf.item.recizo.module

import android.util.Log
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.ErrorCode
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Exception
import kotlin.properties.Delegates

abstract class Scraper {
  var queryString = ""
  var numberOfItem = -1
  var totalNumberOfPages = 0
  var nowPage = 0

  protected fun scrapingHTML(progressCallback: ProgressBarCallBack, callback: CookpadCallBack) = launch(UI) {
    progressCallback.progressBarStart()
    sendHtmlToCallBack(getHTML(getSearchUrl(), nowPage).await(), callback)
//    try{
//      sendHtmlToCallBack(getHTML(getSearchUrl(), nowPage).await(), callback)
//    }catch (e: IOException){
//      callback.failed(ErrorCode.IO_ERROR)
//    }catch (e: Exception){
//      Log.d("TEST",e.message)
//
//      callback.failed(ErrorCode.GENERIC_ERROR)
//    }
    progressCallback.progressBarStop()
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
    val result = try {Jsoup.connect(url + queryString + pageNum).get()} catch (e: IOException) {throw e} catch (e:Exception){throw e}
    return@async result
  }

  private fun sendHtmlToCallBack(html: Document, callback: CookpadCallBack){
      if(totalNumberOfPages == -1){
        numberOfItem = parseNumberOfItem(html)
        totalNumberOfPages = Math.ceil((numberOfItem.toDouble() / 10)).toInt()
      }
      callback.succeed(html)
  }

  abstract protected fun parseNumberOfItem(html: Document): Int
  abstract protected fun getSearchUrl(): String
}