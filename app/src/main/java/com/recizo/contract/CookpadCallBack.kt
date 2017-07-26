package com.recizo.contract

import com.recizo.model.ErrorCode
import org.jsoup.nodes.Document

interface CookpadCallBack {
  fun succeed(html: Document?)
  fun failed(errorCode: ErrorCode)
}