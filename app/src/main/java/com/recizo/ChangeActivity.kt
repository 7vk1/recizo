package com.recizo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.EditText
import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao
import com.recizo.presenter.ItemCategoryAdapter

import kotlinx.android.synthetic.main.fragment_icebox_change.*

class ChangeActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_icebox_change)
    setFormValue(fragment_icebox_change_name, fragment_icebox_change_memo, fragment_icebox_change_date)
    fragment_icebox_change_cancel_btn.setOnClickListener {
      finish()
    }
    val item = intent.getSerializableExtra("item") as IceboxItem

    spinner.adapter = ItemCategoryAdapter(this)

    fragment_icebox_change_register_btn.setOnClickListener {
      val name: String = fragment_icebox_change_name.text.toString()
      val memo: String = fragment_icebox_change_memo.text.toString()
      val year: String = fragment_icebox_change_date.year.toString()
      val month: String = (fragment_icebox_change_date.month + 1).toString()
      val day: String = fragment_icebox_change_date.dayOfMonth.toString()
      val categoryId = spinner.selectedItem as IceboxItem.Category
//      val category: IceboxItem.Category = spinner
      if(!TextUtils.isEmpty(name) ) {
        IceboxDao.update(IceboxItem(item.id, name, memo, year, month, day, categoryId))
        finish()
      } else {
        AlertDialog.Builder(this).
            setTitle("エラー").
            setMessage("野菜の名前が入力されていません!").
            setPositiveButton("OK", null).
            show()
      }
    }
  }
  fun setFormValue(name: EditText, memo: EditText,date: DatePicker) {
    val item = intent.getSerializableExtra("item") as IceboxItem
    name.setText(item.name)
    memo.setText(item.memo)
    val dates = item.date.split("/".toRegex() )
    val year = dates[0].toInt()
    val month = dates[1].toInt()
    val day = dates[2].toInt()
    date.updateDate(year, month, day)
  }
}