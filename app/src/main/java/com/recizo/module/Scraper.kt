package com.recizo.module

import com.recizo.model.ErrorCode
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Exception

abstract class Scraper {
  var queryString = ""
  var numberOfItem = -1
  var totalNumberOfPages = -1
  var nowPage = 1
  var isLoading = true

  fun scrapingHTML(callback: ScraperCallBack) = launch(UI) {
    try {
      sendHtmlToCallBack(getHTML(getSearchUrl(), nowPage).await(), callback)
      isLoading = false
      next()
    } catch (e: IOException) { callback.failed(ErrorCode.IO_ERROR)
    } catch (e: IndexOutOfBoundsException) { callback.failed(ErrorCode.INDEX_OUT_OF_BOUNDS_ERROR)
    } catch (e: UnsupportedOperationException){ callback.failed(ErrorCode.UNSUPPORTED_OPERATION_ERROR)
    } catch (e: Exception) {
      e.printStackTrace()
      callback.failed(ErrorCode.GENERIC_ERROR)
    }
  }

  fun next():Boolean {
    if(nowPage > totalNumberOfPages) return true
    else {
      nowPage++
      return false
    }
  }

  fun idFinished() :Boolean { return nowPage > totalNumberOfPages }

  private fun getHTML(url: String, pageNum: Int = 1) = async(CommonPool) {
    // TODO ProgressSpinnerの追加
    val result = try {Jsoup.connect(url + queryString + pageNum).get()} catch (e: IOException) {throw e} catch (e:Exception){throw e}
    return@async result
  }

  private fun sendHtmlToCallBack(html: Document, callback: ScraperCallBack){
    if(totalNumberOfPages == -1){
      numberOfItem = parseNumberOfItem(html)
      totalNumberOfPages = getTotalPage()
    }
    callback.succeed(html)
  }

  abstract protected fun parseNumberOfItem(html: Document): Int
  abstract protected fun getSearchUrl(): String
  abstract protected fun getTotalPage(): Int

  interface ScraperCallBack {
    fun succeed(html: Document?)
    fun failed(errorCode: ErrorCode)
  }
}