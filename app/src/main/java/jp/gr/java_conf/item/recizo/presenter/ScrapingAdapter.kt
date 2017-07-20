package jp.gr.java_conf.item.recizo.presenter

import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.model.CookpadSearch
import jp.gr.java_conf.item.recizo.model.ErrorCode
import org.jsoup.nodes.Document


class ScrapingAdapter(keywords: List<String>) {
  private val cookpad = CookpadSearch(keywords)

  fun cookpadScraping(callback: CookpadCallBack){
    cookpad.scrapingHTML(object: CookpadCallBack {
      override fun succeed(html: Document?) {
        callback.succeed(html)
      }

      override fun failed(errorCode: ErrorCode) {
        callback.failed(errorCode)
      }
    })
  }
  fun pageToNext() {
    cookpad.pageToNext()
  }
  fun pageToPrev() {
    cookpad.pageToPrev()
  }
}
