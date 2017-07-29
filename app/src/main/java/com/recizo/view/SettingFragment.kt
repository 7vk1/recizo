package com.recizo.view


import android.os.Bundle
import android.preference.PreferenceFragment
import com.recizo.R
import android.preference.Preference
import android.preference.PreferenceManager
import com.recizo.module.AppContextHolder




class SettingFragment : PreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)


    // postcode setting
    val editTextPreference = findPreference("edit_postcode_key")
    editTextPreference.summary =
            PreferenceManager.getDefaultSharedPreferences(AppContextHolder.context).getString("edit_postcode_key", "")
    editTextPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, v ->
      if(v.toString().matches("""^\d{7}$""".toRegex())){
        editTextPreference.summary = v.toString()
        true
      }else false
    }


    // alert settings
    val alertPreference = findPreference("alert_item") as SettingItemView
    alertPreference.summary = "1日前8時"
    alertPreference.setListOnClickListener(object : SettingItemView.OnListClickListener{
      override fun onListClick() {

      }
    })


    //  about this app

  }
}
