package com.recizo.setting_activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.recizo.R
import android.webkit.WebView



class LicenceActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_licence)
    val webView = findViewById(R.id.licence_view) as WebView
    webView.loadUrl("file:///android_asset/licenses.html")
  }
}
