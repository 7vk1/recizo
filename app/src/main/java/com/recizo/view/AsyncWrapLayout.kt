package com.recizo.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.support.constraint.ConstraintLayout
import android.text.Spanned
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

class AsyncWrapLayout : ConstraintLayout {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
  private val textView = TextView(context)
  private val progressbar = ProgressBar(context)
  private val retry = TextView(context)
  var retryMessage = "retry"
    set(value) {
      field = value
      retry.text = value
    }
  init {
    val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    params.leftToLeft = LayoutParams.PARENT_ID
    params.topToTop = LayoutParams.PARENT_ID
    params.rightToRight = LayoutParams.PARENT_ID
    params.bottomToBottom = LayoutParams.PARENT_ID
    textView.layoutParams = params
    textView.gravity = Gravity.CENTER_VERTICAL
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.id = View.generateViewId()
    textView.text = "test"
    textView.textSize = 20f
    val progressParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    progressParams.leftToLeft = LayoutParams.PARENT_ID
    progressParams.topToTop = LayoutParams.PARENT_ID
    progressParams.rightToRight = LayoutParams.PARENT_ID
    progressParams.bottomToBottom = LayoutParams.PARENT_ID
    progressbar.layoutParams = progressParams
    val retryParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    retryParams.leftToLeft = LayoutParams.PARENT_ID
    retryParams.topToBottom = textView.id
    retryParams.rightToRight = LayoutParams.PARENT_ID
    retry.layoutParams = retryParams
    retry.textAlignment = View.TEXT_ALIGNMENT_CENTER
    retry.setTextColor(Color.BLUE)
    retry.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    retry.text = retryMessage
    retry.textSize = 20f
    this.addView(textView)
    this.addView(retry)
    this.addView(progressbar)
  }

  fun showProgressbar() { progressbar.visibility = View.VISIBLE }
  fun hideProgressbar() { progressbar.visibility = View.INVISIBLE }
  fun onError(message: String) {
    textView.visibility = View.VISIBLE
    retry.visibility = View.VISIBLE
    textView.text = message
  }

  fun onError(message: Spanned) {
    textView.visibility = View.VISIBLE
    retry.visibility = View.VISIBLE
    textView.text = message
  }

  fun hideError() {
    textView.visibility = View.INVISIBLE
    retry.visibility = View.VISIBLE
  }
}

