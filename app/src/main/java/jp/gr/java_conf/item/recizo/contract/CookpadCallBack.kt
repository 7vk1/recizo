package jp.gr.java_conf.item.recizo.contract

import jp.gr.java_conf.item.recizo.model.ErrorCode
import org.jsoup.nodes.Document

interface CookpadCallBack {
  fun succeed(html: Document?)
  fun failed(errorCode: ErrorCode)
}