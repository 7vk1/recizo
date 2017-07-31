package com.recizo.view

import android.content.Context
import android.preference.ListPreference
import android.util.AttributeSet


class SettingItemPreference(context: Context, attr: AttributeSet) : ListPreference(context, attr){

  var onListClickListener: SettingItemPreference.OnListClickListener? = null

  fun setListOnClickListener(listener: SettingItemPreference.OnListClickListener){
    onListClickListener = listener
  }

  override fun onClick() {
    onListClickListener?.onListClick()
  }

  interface OnListClickListener {
    fun onListClick()
  }
}