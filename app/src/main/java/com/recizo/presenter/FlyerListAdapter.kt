package com.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.recizo.R
import com.recizo.model.entity.ShufooFlyer

class FlyerListAdapter(private val recyclerView: RecyclerView): RecyclerView.Adapter<FlyerListAdapter.FlyerViewHolder>() , View.OnClickListener {

  inner class FlyerViewHolder(v: View): RecyclerView.ViewHolder(v){
    val storeName: TextView = v.findViewById(R.id.shufoo_shopName)
    val shufooLink: TextView = v.findViewById(R.id.shufoo_link)
    val description: TextView = v.findViewById(R.id.shufoo_description)
    //val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
  }
  val flyerList = mutableListOf<ShufooFlyer>()
  private var onItemClickListener: FlyerListAdapter.OnItemClickListener? = null

  fun setOnItemClickListener(listener: FlyerListAdapter.OnItemClickListener){
    onItemClickListener = listener
  }

  override fun getItemCount(): Int {
    return flyerList.size
  }

  override fun onBindViewHolder(holder: FlyerViewHolder?, position: Int) {
    holder!!.storeName.text = flyerList[position].storeName
    holder.shufooLink.text = flyerList[position].shufooLink
    holder.description.text = flyerList[position].description
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FlyerViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.flyer_list_item, parent, false)
    v.setOnClickListener(this)
    return FlyerViewHolder(v)
  }

  fun addFlyer(flyer: ShufooFlyer) {
    flyerList.add(flyer)
    notifyItemInserted(flyerList.size)
  }

  override fun onClick(view: View?) {
    val position = recyclerView.getChildAdapterPosition(view)
    onItemClickListener?.onItemClick(this.flyerList[position])
  }


  interface OnItemClickListener {
    fun onItemClick(flyer: ShufooFlyer)
  }

}