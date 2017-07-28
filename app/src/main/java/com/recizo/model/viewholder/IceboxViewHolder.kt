package com.recizo.model.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.recizo.R


class IceboxViewHolder(v: View): RecyclerView.ViewHolder(v) {
  val title: TextView = v.findViewById(R.id._icebox_item_title)
  val memo: TextView = v.findViewById(R.id._icebox_item_memo)
  val date: TextView = v.findViewById(R.id._icebox_item_date)
  val cardView: CardView = v.findViewById(R.id.icebox_item)
  val swipeLayout: SwipeLayout = v.findViewById(R.id.swipe_target)
  fun bindView(name: String, memo: String, date: String) {
    this.title.text = name
    this.memo.text = memo
    this.date.text = date
    swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
    swipeLayout.addDrag(SwipeLayout.DragEdge.Right,swipeLayout.findViewById(R.id.icebox_item_del))
    swipeLayout.addDrag(SwipeLayout.DragEdge.Left,swipeLayout.findViewById(R.id.icebox_item_search))
  }
}