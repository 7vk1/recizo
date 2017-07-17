package jp.gr.java_conf.item.recizo.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.IceboxDatabaseHelper
import jp.gr.java_conf.item.recizo.model.IceboxViewHolder
import jp.gr.java_conf.item.recizo.model.Vegetable
import jp.gr.java_conf.item.recizo.view.ChangeActivity

object IceboxAdapter: RecyclerView.Adapter<IceboxViewHolder>() {
  var vegetableList = mutableListOf<Vegetable>()
  lateinit var useContext: Context

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

    val intent = Intent(useContext as Activity , ChangeActivity::class.java)
    holder.cardView.setOnClickListener {
      intent.putExtra("vegetable", vegetableList[position])
      intent.putExtra("position", position)
      (useContext as Activity).startActivity(intent)
    }
  }

  fun setContext(context: Context) {
    this.useContext = context
  }

  fun updateItem(vegetable: Vegetable, position: Int) {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.writebleOpen()
    idh.updateVegetable(vegetable)
    // vegetableListの更新
    for(i in vegetableList.indices) {
      if(vegetable.id == vegetableList[i].id) {
        vegetableList[i] = vegetable
        break
      }
    }
    notifyItemChanged(position)
  }

  fun addItem(vegetable: Vegetable) {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.writebleOpen()
    idh.addVegetable(vegetable)
    vegetableList.add(idh.getVegetableLast() )
    notifyItemInserted(vegetableList.size)
  }

  fun removeItem(position: Int) {
    if(position < vegetableList.size) {
      val idh = IceboxDatabaseHelper(this.useContext)
      idh.writebleOpen()
      idh.deleteVegetable(vegetableList[position].id)
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
    this.vegetableList.removeAt(fromPosition)
    this.vegetableList.add(toPosition, target)
    notifyItemMoved(fromPosition, toPosition)
  }

  fun getItem(): MutableList<Vegetable> {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.readableOpen()
    this.vegetableList = idh.getVegetableAll()

    return vegetableList
  }

  fun initItem() {
    // TODO RecyclerViewのItemが移動(Move)されていた場合反映されない問題
    val list = getItem()
    notifyItemRangeInserted(0, list.size)
  }
}