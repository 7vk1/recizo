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
    holder.image.setImageResource(getImageId(seasonList[pos].category))
    seasonList[pos].item.map { holder.itemFrame.addView(createItem(it, holder.itemFrame)) }
  }

  private fun createItem(text: String, viewGroup: ViewGroup): View {
    val item = LayoutInflater.from(context).inflate(R.layout.season_list_card_item, viewGroup, false)
    item.findViewById<TextView>(R.id.season_card_item_name).text = text
    return item
  }

  private fun getImageId(categoryName: String): Int {
    when(categoryName) {
      "野菜" -> return R.drawable.cat_vegetable
      "魚貝" -> return R.drawable.cat_seafood
      "キノコ" -> return R.drawable.cat_mushroom
      "果物" -> return R.drawable.cat_fruit
      else -> return R.drawable.cat_other
    }
  }
  
  class SeasonViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val category: TextView = v.findViewById(R.id.season_list_category_title)
    val image: ImageView = v.findViewById(R.id.season_list_category_image)
    val itemFrame: LinearLayout = v.findViewById(R.id.season_item_list)
  }
}