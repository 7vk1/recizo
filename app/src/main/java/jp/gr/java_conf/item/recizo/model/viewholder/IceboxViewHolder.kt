package jp.gr.java_conf.item.recizo.model.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import jp.gr.java_conf.item.recizo.R

class IceboxViewHolder(v: View): RecyclerView.ViewHolder(v) {
  val title: TextView = v.findViewById(R.id._icebox_item_title)
  val memo: TextView = v.findViewById(R.id._icebox_item_memo)
  val date: TextView = v.findViewById(R.id._icebox_item_date)
  val cardView: CardView = v.findViewById(R.id.icebox_item)
}