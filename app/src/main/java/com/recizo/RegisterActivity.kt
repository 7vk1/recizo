package com.recizo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils

import com.recizo.model.entity.IceboxItem
import com.recizo.module.IceboxDao
import com.recizo.presenter.ItemCategoryAdapter
import kotlinx.android.synthetic.main.fragment_icebox_change.*

class RegisterActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_icebox_change)

    spinner.adapter = ItemCategoryAdapter(this)

    fragment_icebox_change_cancel_btn.setOnClickListener {
      finish()
    }
    fragment_icebox_change_register_btn.setOnClickListener {
      val name: String = fragment_icebox_change_name.text.toString()
      val memo: String = fragment_icebox_change_memo.text.toString()
      val year: String = fragment_icebox_change_date.year.toString()
      val month: String = (fragment_icebox_change_date.month + 1).toString()
      val day: String = fragment_icebox_change_date.dayOfMonth.toString()
      val categoryId = spinner.selectedItem as IceboxItem.Category

      if(!TextUtils.isEmpty(name) ) {
        IceboxDao.add(IceboxItem(0, name, memo, year, month, day, categoryId))
        finish()
      } else {
        AlertDialog.Builder(this)
            .setTitle("エラー")
            .setMessage("野菜の名前が入力されていません!")
            .setPositiveButton("OK", null)
            .show()
      }
    }
  }
}
