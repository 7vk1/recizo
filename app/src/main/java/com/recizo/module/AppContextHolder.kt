package com.recizo.module

import android.content.Context

object AppContextHolder {
  var context: Context? = null
  get
  set(value) {if(field == null) field = value}
}