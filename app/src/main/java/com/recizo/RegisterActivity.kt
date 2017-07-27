package com.recizo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils

import com.recizo.R
import com.recizo.model.database.IceboxDatabaseHelper
import com.recizo.model.entity.Vegetable
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox_register.*

class RegisterActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_icebox_register)

    fragment_icebox_register_cancel_btn.setOnClickListener {
      finish()
    }
    fragment_icebox_register_register_btn.setOnClickListener {
      val name: String = fragment_icebox_register_name.text.toString()
      val memo: String = fragment_icebox_register_memo.text.toString()
      val year: String = fragment_icebox_register_date.year.toString()
      val month: String = (fragment_icebox_register_date.month + 1).toString()
      val day: String = fragment_icebox_register_date.dayOfMonth.toString()

      if(!TextUtils.isEmpty(name) ) {
        // TODO TODO 第一引数はidだけど内部的にはSQLiteのAutoIncrementを使っているので意味は無い。Nullを許容するように変更する
        val vegetable = Vegetable(0, name, memo, year, month, day)
        val idh = IceboxDatabaseHelper(this)
        idh.addVegetable(vegetable)
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