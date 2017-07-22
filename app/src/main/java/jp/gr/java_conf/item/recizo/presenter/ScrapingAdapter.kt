package jp.gr.java_conf.item.recizo.presenter

import android.content.Context
import jp.gr.java_conf.item.recizo.contract.CookpadCallBack
import jp.gr.java_conf.item.recizo.contract.ProgressBarCallBack
import jp.gr.java_conf.item.recizo.model.CookpadSearch
import jp.gr.java_conf.item.recizo.model.ErrorCode
import org.jsoup.nodes.Document


class ScrapingAdapter(keywords: List<String>) {
  private val cookpad = CookpadSearch(keywords)

  fun cookpadScraping(progressCallback: ProgressBarCallBack, cookpadCallback: CookpadCallBack){
    cookpad.scrapingHTML(object: ProgressBarCallBack {
      override fun progressBarStart() {
        progressCallback.progressBarStart()
      }

      override fun progressBarStop() {
        progressCallback.progressBarStop()
      }

    }, object: CookpadCallBack {
      override fun succeed(html: Document?) {
        cookpadCallback.succeed(html)
      }

      override fun failed(errorCode: ErrorCode) {
        cookpadCallback.failed(errorCode)
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
