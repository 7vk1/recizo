package com.recizo.view

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.recizo.R



class NumberPickerDialog : DialogFragment() {
  private var title: String? = null
  private var message: String? = null
  private var onClickListener: OnClickListener? = null
  private var positiveText: String? = null
  private var negativeText: String? = null
  private var min: Int? = null
  private var max: Int? = null
  private var value: Int = 0

  fun title(v: String): NumberPickerDialog { title = v; return this }
  fun message(v: String): NumberPickerDialog { message = v; return this }
  fun listener(listener: OnClickListener): NumberPickerDialog { onClickListener = listener; return this }
  fun positiveBtn(v: String): NumberPickerDialog { positiveText = v; return this}
  fun negativeBtn(v: String): NumberPickerDialog { negativeText = v; return this }
  fun range(min: Int, max: Int): NumberPickerDialog { this.min = min; this.max = max; return this }
  fun value(v: Int): NumberPickerDialog { this.value = v; return this }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(activity)
    val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val content = inflater.inflate(R.layout.dialog_number_picker, null)
    val numberPicker: NumberPicker = content.findViewById(R.id.numberPicker)
    if(min != null) {
      numberPicker.minValue = min!!
      numberPicker.maxValue = max!!
    }
    numberPicker.value = value
    builder.setView(content)
    if(title != null) builder.setTitle(title)
    if(message != null) builder.setMessage(message)
    if(positiveText != null) builder.setPositiveButton(positiveText, { _, _ -> onClickListener?.onPositive(numberPicker.value) })
    if(negativeText != null) builder.setNegativeButton(negativeText, { _, _ -> onClickListener?.onNegative() })
    return builder.create()
  }

  interface OnClickListener {
    fun onPositive(number: Int)
    fun onNegative()
  }

}