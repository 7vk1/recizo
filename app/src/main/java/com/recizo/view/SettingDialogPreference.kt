package com.recizo.view

import android.app.Dialog
import android.content.Context
import android.preference.Preference
import android.util.AttributeSet

class SettingDialogPreference(context: Context, attr: AttributeSet) : Preference(context, attr) {
  init {
    summary = value
  }
  var defaultValue = ""
  var value: String
    get() = getPersistedString("18:00")
    set(value) {
      persistString(value)
      summary = value
    }
  var setDialog: Dialog? = null
  override fun onClick() {
    setDialog?.show()
  }
}