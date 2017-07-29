package com.recizo.presenter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.recizo.R
import com.recizo.model.viewholder.IceboxViewHolder

class SeasonAdapter(context: Context,val seasonList: List<String>): RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
  init {
    this.setHasStableIds(true)
  }

  override fun getItemCount(): Int {
    return seasonList.size
  }

  override fun onBindViewHolder(holder: SeasonViewHolder?, position: Int) {
    val pos = holder!!.itemId.toInt()
    holder.name.text = seasonList[pos]
    if(isDivider(seasonList[pos])) {
      holder.frame.setBackgroundColor(Color.GREEN)
    } else {
      holder.frame.setBackgroundColor(Color.WHITE)
    }


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
}