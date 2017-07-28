package com.recizo.presenter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.recizo.R
import com.recizo.model.entity.IceboxItem

class ItemCategoryAdapter(private val context: Context) : BaseAdapter() {
  private var dataList = IceboxItem.Category.values()

  override fun getCount(): Int { return dataList.size }
  override fun getItem(position: Int): Any { return dataList[position] }
  override fun getItemId(position: Int): Long { return dataList[position].ordinal.toLong() }
  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spiner_item, parent)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    return ret
  }
  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spiner_item, parent)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    return ret
  }
}