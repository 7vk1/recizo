package com.recizo.presenter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.recizo.R
import com.recizo.model.entity.IceboxItem

class ItemCategoryAdapter(private val context: Context) : BaseAdapter() {
  private var dataList = IceboxItem.Category.values()

  private fun getResource(v: IceboxItem.Category): Int {
    return when(v) {
      IceboxItem.Category.vegetable -> R.drawable.cat_vegetable
      IceboxItem.Category.mushroom -> R.drawable.cat_mushroom
      IceboxItem.Category.dairy -> R.drawable.cat_dairy
      IceboxItem.Category.fruit -> R.drawable.cat_fruit
      IceboxItem.Category.meat -> R.drawable.cat_meat
      IceboxItem.Category.seafood -> R.drawable.cat_seafood
      IceboxItem.Category.seasoning -> R.drawable.cat_seasoning
    }
  }

  override fun getCount(): Int { return dataList.size }
  override fun getItem(position: Int): IceboxItem.Category { return dataList[position] }
  override fun getItemId(position: Int): Long { return dataList[position].ordinal.toLong() }
  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spinner_item, null)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    ret.findViewById<ImageView>(R.id.imageView).setImageResource(getResource(dataList[position]))
    return ret
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val ret = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_spinner_item, null)
    ret.findViewById<TextView>(R.id.textView).text = dataList[position].name_jp
    ret.findViewById<ImageView>(R.id.imageView).setImageResource(getResource(dataList[position]))
    return ret
  }
}