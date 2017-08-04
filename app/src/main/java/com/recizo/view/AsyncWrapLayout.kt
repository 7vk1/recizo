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
  var onErrorFillBackgroundColor = Color.WHITE
  var retryMessage = "retry"
    set(value) {
      field = value
      retry?.text = value
    }
  var onRetryClick: RetryClickListener? = null
  private var progressbar: ProgressBar? = null
  private var textView: TextView? = null
  private var retry: TextView? = null
  private var filler: View? = null

  fun showProgressbar() {
    if(progressbar == null) createProgressbar()
    else progressbar!!.visibility = View.VISIBLE
  }
  fun hideProgressbar() { progressbar?.visibility = View.INVISIBLE }
  fun onError(message: String) {
    showErrorView()
    textView!!.text = message
  }

  fun onError(message: Spanned) {
    showErrorView()
    textView!!.text = message
  }

  fun hideError() {
    textView?.visibility = View.INVISIBLE
    retry?.visibility = View.INVISIBLE
    filler?.visibility = View.INVISIBLE
  }

  private fun showErrorView() {
    if(textView == null) {
      createErrorView()
    } else {
      textView!!.visibility = View.VISIBLE
      retry!!.visibility = View.VISIBLE
      filler!!.visibility = View.VISIBLE
    }
  }

  private fun createErrorView() {
    textView = TextView(context)
    retry = TextView(context)
    filler = View(context)
    val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    params.leftToLeft = LayoutParams.PARENT_ID
    params.topToTop = LayoutParams.PARENT_ID
    params.rightToRight = LayoutParams.PARENT_ID
    params.bottomToBottom = LayoutParams.PARENT_ID
    textView!!.layoutParams = params
    textView!!.gravity = Gravity.CENTER_VERTICAL
    textView!!.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView!!.id = View.generateViewId()
    textView!!.text = "test"
    textView!!.textSize = 20f
    textView!!.setBackgroundColor(Color.WHITE)
    val retryParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    retryParams.leftToLeft = LayoutParams.PARENT_ID
    retryParams.topToBottom = textView!!.id
    retryParams.rightToRight = LayoutParams.PARENT_ID
    retry!!.layoutParams = retryParams
    retry!!.textAlignment = View.TEXT_ALIGNMENT_CENTER
    retry!!.setTextColor(Color.BLUE)
    retry!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    retry!!.text = retryMessage
    retry!!.textSize = 20f
    retry!!.setOnClickListener { onRetryClick?.onRetryClicked() }
    val fillerParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
    filler!!.layoutParams = fillerParams
    filler!!.setBackgroundColor(onErrorFillBackgroundColor)
    this.addView(filler)
    this.addView(textView)
    this.addView(retry)
  }

  private fun createProgressbar() {
    progressbar = ProgressBar(context)
    val progressParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    progressParams.leftToLeft = LayoutParams.PARENT_ID
    progressParams.topToTop = LayoutParams.PARENT_ID
    progressParams.rightToRight = LayoutParams.PARENT_ID
    progressParams.bottomToBottom = LayoutParams.PARENT_ID
    progressbar!!.layoutParams = progressParams
    this.addView(progressbar)
  }
  interface RetryClickListener {
    fun onRetryClicked()
  }

}

