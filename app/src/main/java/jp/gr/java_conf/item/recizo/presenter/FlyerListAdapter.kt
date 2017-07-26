package jp.gr.java_conf.item.recizo.presenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.entity.ShufooFlyer

class FlyerListAdapter: RecyclerView.Adapter<FlyerListAdapter.FlyerViewHolder>() {

  inner class FlyerViewHolder(v: View): RecyclerView.ViewHolder(v){
    val storeName: TextView = v.findViewById(R.id.shufoo_shopName)
    val shufooLink: TextView = v.findViewById(R.id.shufoo_link)
    val description: TextView = v.findViewById(R.id.shufoo_description)
    //val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
  }
  val FlyerList = mutableListOf<ShufooFlyer>()

  override fun getItemCount(): Int {
    return FlyerList.size
  }

  override fun onBindViewHolder(holder: FlyerViewHolder?, position: Int) {
    holder!!.storeName.text = FlyerList[position].storeName
    holder.shufooLink.text = FlyerList[position].shufooLink
    holder.description.text = FlyerList[position].description
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FlyerViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.flyer_list_item, parent, false)
    return FlyerViewHolder(v)
  }

  fun addFlyer(flyer: ShufooFlyer) {
    FlyerList.add(flyer)
    notifyItemInserted(FlyerList.size)
  }

}