package com.recizo.presenter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.mikephil.charting.data.LineRadarDataSet
import com.recizo.R
import com.recizo.view.SeasonsFragment
import com.recizo.view.SeasonsItemFragment
import kotlin.properties.Delegates

class SeasonAdapter(val context: Context,val seasonList: List<SeasonsItemFragment.SeasonItem>): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
  init {this.setHasStableIds(true)}
  private fun isDivider(name: String): Boolean {
    when(name) {
      "野菜" -> return true
      "魚貝" -> return true
      "キノコ" -> return true
      "果物" -> return true
      "その他" -> return true
      else -> return false
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SeasonViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.season_list_card, parent, false)
    return SeasonViewHolder(v)
  }

  override fun getItemId(position: Int): Long { return position.toLong() }

  override fun getItemCount(): Int { return seasonList.size }

  override fun onBindViewHolder(holder: SeasonViewHolder?, position: Int) {
    val pos = holder!!.itemId.toInt()
    holder.category.text = seasonList[pos].category
    holder.image.setImageResource(R.drawable.cat_vegetable)
    seasonList[pos].item.map { holder.itemFrame.addView(createItem(it)) }
  }

  private fun createItem(text: String): TextView {
    val textView = TextView(context)
    textView.text = text
    return textView
  }
  
  class SeasonViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val category: TextView = v.findViewById(R.id.season_list_category_title)
    val image: ImageView = v.findViewById(R.id.season_list_category_image)
    val itemFrame: LinearLayout = v.findViewById(R.id.season_item_list)
  }
}