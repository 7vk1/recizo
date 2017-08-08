package com.recizo.view

import android.app.TimePickerDialog
import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import com.recizo.module.Notification

class NotificationTimePreference(context: Context, attr: AttributeSet) : Preference(context, attr) {
  var value: String
    get() = getPersistedString("18:00")
    set(value) { persistString(value) }
  init { summary = value + "に通知" }

  override fun onClick() {
    TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, min ->
          value = "$hour:${if (min < 10) "0$min" else "$min"}"
          summary = value + "に通知"
          Notification.change(context, hour, min)
        }, value.split(":")[0].toInt(), value.split(":")[1].toInt(), false).show()
  }
}