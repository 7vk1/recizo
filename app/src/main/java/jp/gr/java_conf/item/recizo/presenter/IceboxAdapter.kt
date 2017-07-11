package jp.gr.java_conf.item.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.Vegetable

class IceboxAdapter(val vegetableList :MutableList<Vegetable>): RecyclerView.Adapter<IceboxAdapter.ViewHolder>() {
  override fun getItemCount(): Int {
    return vegetableList.size
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val v: View = LayoutInflater.from(parent!!.context).inflate(R.layout.icebox_item, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    holder!!.title.text = vegetableList[position].name
    holder.memo.text = vegetableList[position].memo
    holder.date.text = vegetableList[position].date
  }

  fun addItem(position: Int, vegetable: Vegetable) {
    var itemPositon = position
    if(itemPositon > vegetableList.size) {itemPositon = vegetableList.size}
    vegetableList.add(itemPositon, vegetable)
    notifyItemInserted(position)
  }

  fun removeItem(position: Int) {
    if(position < vegetableList.size) {
      vegetableList.removeAt(position)
      notifyItemRemoved(position)
    }
  }

  fun moveItem(fromPosition: Int, toPosition: Int) {
    val target: Vegetable = vegetableList[fromPosition]
    vegetableList.removeAt(fromPosition)
    vegetableList.add(toPosition, target)
    notifyItemMoved(fromPosition, toPosition)
  }

  inner class ViewHolder(v: View):RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById(R.id._icebox_item_title)
    val memo: TextView = v.findViewById(R.id._icebox_item_memo)
    val date: TextView = v.findViewById(R.id._icebox_item_date)
  }
}