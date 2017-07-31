package com.recizo.view


import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import com.recizo.R
import android.preference.Preference
import android.preference.PreferenceManager
import com.recizo.module.AppContextHolder
import com.recizo.setting_avtivitys.AboutMeActivity
import com.recizo.setting_avtivitys.AlertSettingActivity
import com.recizo.setting_avtivitys.LicenceActivity

class SettingFragment : PreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)

    // postcode setting
    val editTextPreference = findPreference("edit_postcode_key")
    editTextPreference.summary = PreferenceManager.getDefaultSharedPreferences(AppContextHolder.context).getString("edit_postcode_key", "")
    editTextPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, v ->
      if(v.toString().matches("""^\d{7}$""".toRegex())){
        editTextPreference.summary = v.toString()
        true
      }else false //todo impl
    }

    // alert settings
    val alertDay = findPreference("alert_day") as SettingItemView
    alertDay.summary = "賞味期限の${alertDay.value}日前"
    alertDay.onListClickListener = object : SettingItemView.OnListClickListener {
      override fun onListClick() {
        NumberPickerDialog()
            .title("賞味期限の通知設定")
            .positiveBtn("set").negativeBtn("cancel")
            .range(0, 9)
            .value(alertDay.value.toInt())
            .listener(object : NumberPickerDialog.OnClickListener {
              override fun onPositive(number: Int) {
                alertDay.value = number.toString()
                alertDay.summary = "賞味期限の${number}日前"
              }
              override fun onNegative() {} })
            .show(fragmentManager, "set_alert_day")
      }
    }

    val alertTime = findPreference("alert_time") as SettingPreference
    alertTime.defaultValue = "18:00"
    alertTime.summary = alertTime.value + "に通知"
    val time = alertTime.value.split(":")
    alertTime.setDialog = TimePickerDialog(activity,
        TimePickerDialog.OnTimeSetListener { _, hour, min ->
          alertTime.value = "$hour:${if(min < 10) "0$min" else "$min"}"
          alertTime.summary = alertTime.value + "に通知"
        }, time[0].toInt(), time[1].toInt(), false)


    // about this app
    (findPreference("about_me_item") as SettingItemView).setListOnClickListener(
        object : SettingItemView.OnListClickListener{
          override fun onListClick() {
            activity.startActivity(Intent(activity, AboutMeActivity::class.java))
          }
        }
    )


    // ライセンス
    (findPreference("licence_item") as SettingItemView).setListOnClickListener(
            object : SettingItemView.OnListClickListener{
              override fun onListClick() {
                activity.startActivity(Intent(activity, LicenceActivity::class.java))
              }
            }
    )
  }
}
