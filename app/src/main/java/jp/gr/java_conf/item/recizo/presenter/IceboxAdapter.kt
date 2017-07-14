package jp.gr.java_conf.item.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.IceboxViewHolder
import jp.gr.java_conf.item.recizo.model.Vegetable

object IceboxAdapter: RecyclerView.Adapter<IceboxViewHolder>() {
  val vegetableList = mutableListOf<Vegetable>()

  override fun getItemCount(): Int {
    return vegetableList.size
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent!!.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    holder!!.title.text = vegetableList[position].name
    holder.memo.text = vegetableList[position].memo
    holder.date.text = vegetableList[position].date
  }

  fun addItem(vegetable: Vegetable) {
    vegetableList.add(vegetable)
    notifyItemInserted(vegetableList.size)
  }

  fun removeItem(position: Int) {
    if(position < vegetableList.size) {
      vegetableList.removeAt(position)
      notifyItemRemoved(position)
    }
  }

  fun removeItem(name: String) {
    for(i in vegetableList.indices) {
      if (vegetableList[i].name == name) {
        removeItem(i)
        break
      }
    }
  }

  fun moveItem(fromPosition: Int, toPosition: Int) {
    val target: Vegetable = vegetableList[fromPosition]
    vegetableList.removeAt(fromPosition)
    vegetableList.add(toPosition, target)
    notifyItemMoved(fromPosition, toPosition)
  }

  fun getItem(): List<Vegetable> {
    return vegetableList
  }
}