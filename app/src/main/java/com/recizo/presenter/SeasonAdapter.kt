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
import kotlin.properties.Delegates

class SeasonAdapter(val context: Context,val seasonList: List<String>): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
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
  
  private fun setFramePadding(frame: LinearLayout, start: Int, top: Int, end: Int, bottom: Int) {
    val s = PropertyHolder.framePaddingStart!!.times(start)
    val e = PropertyHolder.framePaddingEnd!!.times(end)
    val t = PropertyHolder.framePaddingTop!!.times(top)
    val b = PropertyHolder.framePaddingBottom!!.times(bottom)
    frame.setPaddingRelative(s, e, t, b)
  }

  private fun dividerSetting(frame: LinearLayout, name: TextView) {
    frame.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
    setFramePadding(frame, start = 10, top = 0, end = 0, bottom = 0)
    name.textSize = PropertyHolder.nameSize!!.times(0.5f)
  }

  private fun columnSetting(frame: LinearLayout, name: TextView) {
    frame.setBackgroundColor(Color.WHITE)
    setFramePadding(frame, start = 20,top = 0, end = 0, bottom = 0)
    name.textSize = PropertyHolder.nameSize!!.times(0.3f)
  }
  
  private fun setPropertyHolder(holder: SeasonViewHolder?) {
    PropertyHolder.nameSize = holder!!.name.textSize
    PropertyHolder.framePaddingTop = holder.frame.paddingTop
    PropertyHolder.framePaddingBottom = holder.frame.paddingBottom
    PropertyHolder.framePaddingStart = holder.frame.paddingStart
    PropertyHolder.framePaddingEnd = holder.frame.paddingEnd
  }
  
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SeasonViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.season_list_item, parent, false)
    return SeasonViewHolder(v)
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getItemCount(): Int {
    return seasonList.size
  }

  override fun onBindViewHolder(holder: SeasonViewHolder?, position: Int) {
    val pos = holder!!.itemId.toInt()
    holder.name.text = seasonList[pos]
    setPropertyHolder(holder)
    if(isDivider(seasonList[pos])) dividerSetting(holder.frame, holder.name)
    else columnSetting(holder.frame, holder.name)
  }
  
  class SeasonViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val name: TextView = v.findViewById(R.id.season_list_item_name)
    val frame: LinearLayout = v.findViewById(R.id.season_list_item_frame)
  }

  private object PropertyHolder {
    var nameSize: Float? = null
      set(value) {field = field ?: value}
    var framePaddingStart: Int? = null
      set(value) {field = field ?: value}
    var framePaddingEnd: Int? = null
      set(value) {field = field ?: value}
    var framePaddingTop: Int? = null
      set(value) {field = field ?: value}
    var framePaddingBottom: Int? = null
      set(value) {field = field ?: value}
  }
}