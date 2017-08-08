package com.recizo.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.preference.Preference
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import com.recizo.R

class PostalCodePreference(context: Context, attr: AttributeSet) : Preference(context, attr) {
  var value: String
    get() = getPersistedString("000-0000")
    set(value) {
      persistString(value)
      summary = value
    }
  init { summary = value }

  override fun onClick() {
    val builder = AlertDialog.Builder(context)
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val content = inflater.inflate(R.layout.view_postal_code, null)
    builder.setPositiveButton("OK", { _, _ -> value = content.findViewById<EditText>(R.id.postal_code).text.toString() })
        .setNegativeButton("cancel", null)
        .setView(content)
        .create()
        .show()
  }
}