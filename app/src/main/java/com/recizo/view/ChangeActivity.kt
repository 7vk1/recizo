package com.recizo.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText

import com.recizo.R
import com.recizo.model.entity.Vegetable
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox_change.*

class ChangeActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_icebox_change)

    Log.d("TEST CHANGE ACT", "Send ChangeActivity")

    setFormValue(fragment_icebox_change_name, fragment_icebox_change_memo, fragment_icebox_change_date)

    fragment_icebox_change_cancel_btn.setOnClickListener {
      finish()
    }

    fragment_icebox_change_register_btn.setOnClickListener {
      val name: String = fragment_icebox_change_name.text.toString()
      val memo: String = fragment_icebox_change_memo.text.toString()
      val year: String = fragment_icebox_change_date.year.toString()
      val month: String = (fragment_icebox_change_date.month + 1).toString()
      val day: String = fragment_icebox_change_date.dayOfMonth.toString()

      if(!TextUtils.isEmpty(name) ) {
        val vegetable = Vegetable(takeIntent().id, name, memo, year, month, day)
        IceboxAdapter.updateItem(vegetable, intent.getIntExtra("position", 0))
        finish()
      } else {
        AlertDialog.Builder(this).
            setTitle("エラー").
            setMessage("野菜の名前が入力されていません!").
            setPositiveButton("OK", null).
            show()
      }
      finish()
    }
  }

  fun takeIntent(): Vegetable {
    return intent.getSerializableExtra("vegetable") as Vegetable
  }

  fun setFormValue(name: EditText, memo: EditText,date: DatePicker) {
    val vegetable = takeIntent()
    name.setText(vegetable.name)
    memo.setText(vegetable.memo)
    val dates = vegetable.date.split("/".toRegex() )

    val year = dates[0].toInt()
    val month = dates[1].toInt()
    val day = dates[2].toInt()
    date.updateDate(year, month, day)
  }
}