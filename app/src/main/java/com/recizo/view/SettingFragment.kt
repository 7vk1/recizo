package com.recizo.view


import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.preference.*
import com.recizo.R
import com.recizo.module.Notification
import com.recizo.setting_activities.AboutMeActivity
import com.recizo.setting_activities.LicenceActivity

class SettingFragment : PreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)

    // postcode setting
    val editTextPreference = findPreference("edit_postcode_key") as EditTextPreference

    editTextPreference.summary = editTextPreference.text
    editTextPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, v ->
      if (v.toString().matches("""^\d{7}$""".toRegex())) {
        editTextPreference.summary = v.toString()
        true
      } else false //todo impl
    }

    // alert settings
    val isAlert = findPreference("isAlert") as CheckBoxPreference
    val alertDay = findPreference("alert_day") as ListPreference
    alertDay.summary = "賞味期限の${alertDay.value}日前"
    alertDay.setOnPreferenceClickListener {
      NumberPickerDialog()
              .title("賞味期限の通知設定")
              .positiveBtn("set").negativeBtn("cancel")
              .range(0, 7)
              .value(alertDay.value.toInt())
              .listener(object : NumberPickerDialog.OnClickListener {
                override fun onPositive(number: Int) {
                  alertDay.value = number.toString()
                  alertDay.summary = "賞味期限の${number}日前"
                }

                override fun onNegative() {}
              })
              .show(fragmentManager, "set_alert_day")
      true
    }

    val alertTime = findPreference("alert_time") as SettingDialogPreference
    alertTime.defaultValue = this.resources.getString(R.string.default_time)
    alertTime.summary = alertTime.value + "に通知"
    val time = alertTime.value.split(":")
    alertTime.setDialog = TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener { _, hour, min ->
              alertTime.value = "$hour:${if (min < 10) "0$min" else "$min"}"
              alertTime.summary = alertTime.value + "に通知"
              Notification.change(activity, hour, min)
            }, time[0].toInt(), time[1].toInt(), false)

    if (!isAlert.isChecked) {
      alertDay.isEnabled = false
      alertTime.isEnabled = false
    }
    isAlert.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, f ->
      alertDay.isEnabled = f as Boolean
      alertTime.isEnabled = f
      if (f == false) Notification.cancel(activity)
      else Notification.set(activity)
      true
    }


    // about this app
    (findPreference("about_me_item") as ListPreference).setOnPreferenceClickListener {
      activity.startActivity(Intent(activity, AboutMeActivity::class.java))
      true
    }

    // ライセンス
    (findPreference("licence_item") as ListPreference).setOnPreferenceClickListener {
      activity.startActivity(Intent(activity, LicenceActivity::class.java))
      true
    }
  }
}
