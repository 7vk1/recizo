package com.recizo.presenter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.recizo.R

class SeasonAdapter(context: Context, val layoutId: Int,val seasonList: List<String>): ArrayAdapter<String>(context, layoutId, seasonList) {
  var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    Log.d("_TEST", "GET VIEW TOP")
    val view = convertView ?: inflater.inflate(layoutId, null)
    view.findViewById<TextView>(R.id.season_list_item_name).text = seasonList[position]
    Log.d("_TEST", "GET VIEW BOTTOM")

    if(!isEnabled(position)) {
      view.setBackgroundColor(Color.GREEN)
    }

    return view
  }

  override fun isEnabled(position: Int): Boolean {
    when(getItem(position)) {
      // TODO カテゴリの名前を入れる
      "野菜" -> return false
      "魚介類" -> return false
      "" -> return false
      "" -> return false
      "" -> return false
      "" -> return false
    }
    return true
  }
}