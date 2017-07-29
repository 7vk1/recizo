package com.recizo.view

import android.content.Context
import android.preference.ListPreference
import android.util.AttributeSet


internal class SettingItemView : ListPreference {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

  override fun onClick() {
    println("hogehogehoge")
  }
}