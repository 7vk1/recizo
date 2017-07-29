package com.recizo.view

import android.content.Context
import android.preference.ListPreference
import android.util.AttributeSet


class SettingItemView : ListPreference {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

  var onListClickListener: SettingItemView.OnListClickListener? = null

  fun setListOnClickListener(listener: SettingItemView.OnListClickListener){
    onListClickListener = listener
  }

  override fun onClick() {
    onListClickListener?.onListClick()
  }

  interface OnListClickListener {
    fun onListClick()
  }
}