package com.recizo.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.preference.Preference
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.recizo.R
import com.recizo.module.AppContextHolder

class PostalCodePreference(context: Context, attr: AttributeSet) : Preference(context, attr) {
  var value: String
    get() = getPersistedString(default)
    set(value) {
      persistString(value)
      summary = value
    }
  val default = "000-0000"

  override fun onClick() {
    val builder = AlertDialog.Builder(context)
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val content = inflater.inflate(R.layout.view_postal_code, null)
    val et: EditText = content.findViewById(R.id.postal_code)
    val v = if(value.isEmpty()) default else value
    et.setText("${v.substring(0..2)}0${v.substring(4..7)}")
    et.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, delCount: Int, addCount: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, delCount: Int, addCount: Int) {
        if(s == null) return
        if(s.length == 3 && addCount == 1){
          et.append("0")
          et.setSelection(et.length())
        } else if(s.length == 3 && delCount == 1) {
          et.setText(et.text.substring(0..et.text.length - 2))
          et.setSelection(et.length())
        }
      }
      override fun afterTextChanged(e: Editable?) {}
    })
    val dialog = builder.setPositiveButton("OK", null)
        .setNegativeButton("cancel", null)
        .setView(content)
        .create()
    dialog.setOnShowListener {
      (AppContextHolder.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
          .showSoftInput(et, 0)
      et.selectAll()
    }
    dialog.show()
    val btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
    btn.setOnClickListener {
      val ret = et.text.toString()
      if(ret.length == 8) {
        value = "${ret.substring(0..2)}-${ret.substring(4..7)}"
        dialog.dismiss()
      }
    }
  }
}