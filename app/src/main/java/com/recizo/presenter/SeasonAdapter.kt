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
import com.recizo.R
import kotlin.properties.Delegates

class SeasonAdapter(val context: Context,val seasonList: List<String>): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
  init {
    this.setHasStableIds(true)
  }

  override fun getItemCount(): Int {
    return seasonList.size
  }

  override fun onBindViewHolder(holder: SeasonViewHolder?, position: Int) {
    val pos = holder!!.itemId.toInt()
    holder.name.text = seasonList[pos]
    PropertyHolder.nameSize = holder.name.textSize
    PropertyHolder.framePaddginTop = holder.frame.paddingTop
    PropertyHolder.framePaddginBottom = holder.frame.paddingBottom
    PropertyHolder.framePaddginStart = holder.frame.paddingStart
    PropertyHolder.framePaddginEnd = holder.frame.paddingEnd

    if(isDivider(seasonList[pos])) dividerSetting(holder.frame, holder.name)
    else columnSetting(holder.frame, holder.name)
  }

  private fun dividerSetting(frame: LinearLayout, name: TextView) {
    frame.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
    val start = PropertyHolder.framePaddginStart!!.times(1)
    val end = PropertyHolder.framePaddginEnd!!.times(1)
    val top = PropertyHolder.framePaddginTop!!.plus(0)
    val bottom = PropertyHolder.framePaddginBottom!!.plus(0)
    frame.setPaddingRelative(start, top, end, bottom)
    name.gravity = Gravity.DISPLAY_CLIP_VERTICAL
    name.textSize = PropertyHolder.nameSize!!.times(0.5f)
  }

  private fun columnSetting(frame: LinearLayout, name: TextView) {
    frame.setBackgroundColor(Color.WHITE)
    val start = PropertyHolder.framePaddginStart!!.times(2)
    val end = PropertyHolder.framePaddginEnd!!.times(2)
    val top = PropertyHolder.framePaddginTop!!.plus(10)
    val bottom = PropertyHolder.framePaddginBottom!!.plus(0)
    frame.setPaddingRelative(start, top, end, bottom)
    name.textSize = PropertyHolder.nameSize!!.times(0.25f)
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SeasonViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.season_list_item, parent, false)
    return SeasonViewHolder(v)
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  fun isDivider(name: String): Boolean {
    when(name) {
      "野菜" -> return true
      "魚・貝" -> return true
      "芋・キノコ" -> return true
      "果物・きのみ" -> return true
    }
    return false
  }

  class SeasonViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val name: TextView = v.findViewById(R.id.season_list_item_name)
    val frame: LinearLayout = v.findViewById(R.id.season_list_item_frame)
  }

  private object PropertyHolder {
    var nameSize: Float? = null
    set(value) { field = field ?: value }
    var framePaddginStart: Int? = null
    set(value) { field = field ?: value}
    var framePaddginEnd: Int? = null
      set(value) { field = field ?: value}
    var framePaddginTop: Int? = null
      set(value) { field = field ?: value}
    var framePaddginBottom: Int? = null
      set(value) { field = field ?: value}
  }
}