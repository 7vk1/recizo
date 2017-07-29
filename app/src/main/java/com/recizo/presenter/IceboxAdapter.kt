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
import com.recizo.module.IceboxDao


class IceboxAdapter(val fragment: IceboxButtons) : RecyclerView.Adapter<IceboxAdapter.IceboxViewHolder>() {
  private var itemList = IceboxDao.getAll().toMutableList()
  private var searchList = mutableSetOf<Long>()
  private var garbageList = mutableSetOf<Long>()
  private var recyclerView: RecyclerView? = null
  private var sort = Sort.CATEGORY
  init {
    setHasStableIds(true)
    itemList.add(IceboxItem(id = -1, memo = "", name = "empty", date = "", category = IceboxItem.Category.vegetable))
  }

  fun removeItem(id: Long) {
    IceboxDao.delete(id.toInt())
    itemList = itemList.filter { it.id.toLong() != id }.toMutableList()
    notifyDataSetChanged()
  }

  fun updateDataSet() {
    itemList = IceboxDao.getAll().toMutableList()
    itemList.add(IceboxItem(-1, "empty", "", "", IceboxItem.Category.vegetable))
    sortItems(sort)
    this.notifyDataSetChanged()
    fragment.changeBtnVisibility(add = true)
  }

  fun onDeleteClicked() {
    garbageList.sorted().reversed().map {
      removeItem(it)
    }
    garbageList.clear()
    fragment.changeBtnVisibility(add = true)
  }

  fun sortItems(type: Sort) {
    itemList.removeAt(itemList.size -1)
    sort = type
    when(type) {
      Sort.NAME -> itemList.sortBy { it.name }
      Sort.DATE -> itemList.sortBy { it.date }
      Sort.CATEGORY -> itemList.sortBy { it.category }
    }
    itemList.add(IceboxItem(-1, "blank", "", "", IceboxItem.Category.vegetable))
    notifyDataSetChanged()
  }

  fun getSearchItemList(): Set<String> {
    return searchList.map { id -> itemList.first { it.id.toLong() == id }.name }.toSet()
  }

  fun onUndoClicked() {
    searchList.plus(garbageList).map {
      val item = recyclerView?.findViewHolderForItemId(it)
      if(item != null) (item as IceboxViewHolder).swipeLayout.close()
    }
    fragment.changeBtnVisibility(add = true)
  }

  override fun getItemCount(): Int {
    return itemList.size
  }

  override fun getItemId(position: Int): Long {
    return itemList[position].id.toLong()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IceboxViewHolder {
    val v: View = LayoutInflater.from(parent.context).inflate(R.layout.icebox_item, parent, false)
    return IceboxViewHolder(v)
  }

  override fun onBindViewHolder(holder: IceboxViewHolder?, position: Int) {
    val item = itemList[position]
    if(item.id == -1) {
      holder!!.itemView.visibility = View.INVISIBLE
      return
    }
    holder!!.bindView(item)
    holder.swipeLayout.addSwipeListener(object: SimpleSwipeListener() {
      override fun onOpen(layout: SwipeLayout?) {
        super.onOpen(layout)
        when(layout!!.dragEdge) {
          SwipeLayout.DragEdge.Left -> {
            searchList.add(holder.itemId)
            fragment.changeBtnVisibility(search = true, undo = true)
          }
          SwipeLayout.DragEdge.Right -> {
            garbageList.add(holder.itemId)
            fragment.changeBtnVisibility(delete = true, undo = true)
          }
          else -> return
        }
      }
      override fun onClose(layout: SwipeLayout?) {
        super.onClose(layout)
        garbageList.remove(holder.itemId)
        searchList.remove(holder.itemId)
        if(garbageList.size == 0 && searchList.size == 0) fragment.changeBtnVisibility(add = true)
      }
    })

    holder.cardView.setOnClickListener {
      fragment.toChangeActivity(itemList.first { it.id.toLong() == holder.itemId })
    }
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
    super.onAttachedToRecyclerView(recyclerView)
    this.recyclerView = recyclerView
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
    super.onDetachedFromRecyclerView(recyclerView)
    this.recyclerView = null
  }

  enum class Sort {
    NAME, DATE, CATEGORY
  }

  interface IceboxButtons {
    fun changeBtnVisibility(add: Boolean = false, undo: Boolean = false, search: Boolean = false, delete: Boolean = false)
    fun toChangeActivity(item: IceboxItem)
  }

  class IceboxViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById(R.id._icebox_item_title)
    val memo: TextView = v.findViewById(R.id._icebox_item_memo)
    val date: TextView = v.findViewById(R.id._icebox_item_date)
    val cardView: CardView = v.findViewById(R.id.icebox_item)
    val swipeLayout: SwipeLayout = v.findViewById(R.id.swipe_target)
    val categoryImg: ImageView = v.findViewById(R.id.category_img)
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