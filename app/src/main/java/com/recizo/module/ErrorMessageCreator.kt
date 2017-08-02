package com.recizo.module

import android.text.Html
import android.text.Spanned

object ErrorMessageCreator {
  fun create(code: Http.ErrorCode): Spanned {
    return when(code) {
      Http.ErrorCode.CONNECTION_ERROR -> getString("インターネットに接続できませんでした", " 通信状態をお確かめください")
      else -> getString("サーバーエラー", "しばらく時間をおいてからもう一度アクセスしてください")
    }
  }
  private fun getString(title: String, message: String): Spanned {
    val text = "<font color=#FF9137><big>$title</big></font><br><br><small>$message</small>"
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else Html.fromHtml(text)
  }
}