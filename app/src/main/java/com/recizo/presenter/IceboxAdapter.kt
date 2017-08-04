package com.recizo.presenter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recizo.R
import com.daimajia.swipe.*
import com.recizo.model.entity.IceboxItem


class IceboxAdapter : RecyclerView.Adapter<IceboxAdapter.IceboxViewHolder>() {
  private var eventListener: EventListener? = null
  private var itemList = listOf(IceboxItem(-1, "empty", "", "", IceboxItem.Category.vegetable))
  init { setHasStableIds(true) }

  fun getItemList(): List<IceboxItem> { return itemList.filter { it.id != -1 } }
  fun setItemList(list: List<IceboxItem>) {
    itemList = list.plus(IceboxItem(-1, "empty", "", "", IceboxItem.Category.vegetable))
    notifyDataSetChanged()
  }

  fun removeItem(index: Int) {
    this.itemList = this.itemList.filterIndexed { i, _ -> index != i }
    notifyItemRemoved(index)
  }

  fun setEventListener(listener: EventListener) { eventListener = listener }

  override fun getItemCount(): Int { return itemList.size }
  override fun getItemId(position: Int): Long { return itemList[position].id.toLong() }
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  override fun onViewAttachedToWindow(holder: IceboxViewHolder) {
    super.onViewAttachedToWindow(holder)
    eventListener?.onViewAttached(holder)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder, position: Int) {
    val item = itemList[position]
    if(item.id == -1) {
      holder.itemView.visibility = View.INVISIBLE
      return
    }
    holder.bindView(item)
    holder.del.setOnClickListener { eventListener?.onDeleteClicked(holder.itemId) }
    holder.search.setOnClickListener { eventListener?.onSearchClicked(item) }
    eventListener?.onBindViewHolder(holder, position)
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener() {
      override fun onOpen(layout: SwipeLayout?) {
        super.onOpen(layout)
        eventListener?.onItemOpen(layout!!.dragEdge, holder.itemId)
      }
      override fun onClose(layout: SwipeLayout?) {
        super.onClose(layout)
        eventListener?.onItemClosed(holder.itemId)
      }
    })

    holder.cardView.setOnClickListener {
      this.eventListener?.onItemClicked(itemList.first { it.id.toLong() == holder.itemId })
    }
  }

  interface EventListener {
    fun onItemClicked(item: IceboxItem)
    fun onItemClosed(itemId: Long)
    fun onItemOpen(dragEdge: SwipeLayout.DragEdge, itemId: Long)
    fun onViewAttached(holder: IceboxViewHolder)
    fun onBindViewHolder(holder: IceboxViewHolder, position: Int)
    fun onDeleteClicked(itemId: Long)
    fun onSearchClicked(item: IceboxItem)
  }

  class IceboxViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById(R.id._icebox_item_title)
    val memo: TextView = v.findViewById(R.id._icebox_item_memo)
    val date: TextView = v.findViewById(R.id._icebox_item_date)
    val cardView: CardView = v.findViewById(R.id.icebox_item)
    val swipeLayout: SwipeLayout = v.findViewById(R.id.swipe_target)
    val categoryImg: ImageView = v.findViewById(R.id.category_img)
    val del: CardView = v.findViewById(R.id.icebox_item_del)
    val search: CardView = v.findViewById(R.id.icebox_item_search)
    fun bindView(item: IceboxItem) {
      this.title.text = item.name
      this.memo.text = item.memo
      this.date.text = item.date
      val img_id = when(item.category) {
        IceboxItem.Category.vegetable -> R.drawable.cat_vegetable
        IceboxItem.Category.fruit -> R.drawable.cat_fruit
        IceboxItem.Category.meat -> R.drawable.cat_meat
        IceboxItem.Category.seafood -> R.drawable.cat_seafood
        IceboxItem.Category.dairy -> R.drawable.cat_dairy
        IceboxItem.Category.mushroom -> R.drawable.cat_mushroom
        IceboxItem.Category.seasoning -> R.drawable.cat_seasoning
      }
      this.categoryImg.setImageResource(img_id)
      swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
      swipeLayout.addDrag(SwipeLayout.DragEdge.Right,swipeLayout.findViewById(R.id.icebox_item_del))
      swipeLayout.addDrag(SwipeLayout.DragEdge.Left,swipeLayout.findViewById(R.id.icebox_item_search))
    }
  }
}
