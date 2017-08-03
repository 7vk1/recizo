package com.recizo.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.recizo.R
import com.recizo.module.RecizoApi

class VegetableAdapter(val context: Context) : BaseAdapter() {
  private var dataList = RecizoApi.Vegetables.values()

  override fun getCount(): Int { return dataList.size }
  override fun getItem(position: Int): RecizoApi.Vegetables { return dataList[position] }
  override fun getItemId(position: Int): Long { return dataList[position].ordinal.toLong() }
  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spinner_item, null)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    ret.findViewById<ImageView>(R.id.imageView).setImageResource(dataList[position].resource)
    return ret
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spinner_item, null)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    ret.findViewById<ImageView>(R.id.imageView).setImageResource(dataList[position].resource)
    return ret
  }
}