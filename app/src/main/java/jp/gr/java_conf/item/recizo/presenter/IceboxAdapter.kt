package jp.gr.java_conf.item.recizo.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.IceboxDatabaseHelper
import jp.gr.java_conf.item.recizo.model.IceboxViewHolder
import jp.gr.java_conf.item.recizo.model.Vegetable
import jp.gr.java_conf.item.recizo.view.ChangeActivity

object IceboxAdapter: RecyclerView.Adapter<IceboxViewHolder>() {
  var vegetableList = mutableListOf<Vegetable>()
  var searchList = mutableListOf<String>()
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
    holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,holder.swipeLayout.findViewById(R.id.icebox_item_search))
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,holder.swipeLayout.findViewById(R.id.icebox_item_del))
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener(){
      override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
        if(xvel < 0){
          //左スワイプ時
          var test = holder.swipeLayout.openStatus
          searchList.add(vegetableList[position].name)
          Log.d("来てはいる","左" + test.toString())
          Log.d("リストのサイズ", searchList.size.toString() +"  "+test.toString())
        }
        else if(xvel >= 0.toFloat()){
          var test = holder.swipeLayout.openStatus
          searchList.remove(vegetableList[position].name)
          Log.d("来てはいる","右")
          Log.d("リストのサイズ", searchList.size.toString() +"  "+test.toString())
        }
      }

    //verride fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
      //  super.onUpdate(layout, leftOffset, topOffset)
      //  Log.d("Update左",leftOffset.toString())
      //  Log.d("Update上",topOffset.toString())
      //}
    })
    val intent = Intent(useContext as Activity , ChangeActivity::class.java)
    holder.cardView.setOnClickListener {
      if(position == vegetableList.size){
        intent.putExtra("vegetable", vegetableList[position - 1])
        intent.putExtra("position", position -1)
      }else {
      intent.putExtra("vegetable", vegetableList[position])
      intent.putExtra("position", position)
      }

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

  fun getOneItem(position: Int): String{
    val target: String = vegetableList[position].name
    return target
  }

  fun getItem(): MutableList<Vegetable> {
    val idh = IceboxDatabaseHelper(this.useContext)
    idh.readableOpen()
    this.vegetableList = idh.getVegetableAll()

    return vegetableList
  }

  fun getTargetList(): MutableList<String>{
    return searchList
  }

  fun initItem() {
    // TODO RecyclerViewのItemが移動(Move)されていた場合反映されない問題
    val list = getItem()
    notifyItemRangeInserted(0, list.size)
  }
}