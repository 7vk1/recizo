package com.recizo.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recizo.R
import com.recizo.model.entity.ShufooFlyer
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.URL

class FlyerListAdapter(private val recyclerView: RecyclerView): RecyclerView.Adapter<FlyerListAdapter.FlyerViewHolder>() , View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
  private var onItemClickListener: FlyerListAdapter.OnItemClickListener? = null
  private var onRefreshPullListener: FlyerListAdapter.OnRefreshPullListner? = null
  val flyerList = mutableListOf<ShufooFlyer>()

  fun setOnItemClickListener(listener: FlyerListAdapter.OnItemClickListener){ onItemClickListener = listener }
  fun setOnRefreshPullListener(listener: FlyerListAdapter.OnRefreshPullListner) { onRefreshPullListener = listener }

  fun addFlyer(flyer: ShufooFlyer) {
    flyerList.add(flyer)
    notifyItemInserted(flyerList.size)
  }

  fun clearFlyer() {
    val size = flyerList.size
    flyerList.clear()
    notifyItemRangeRemoved(0, size)
  }

  private fun getImageStream(imageUrl: String) = async(CommonPool) {
    val url = URL(imageUrl)
    // image scale
    val scale = 3
    val bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
    val width = bitmapImage.width * scale
    val height = bitmapImage.height * scale
    // TODO 画像取得失敗時のエラーハンドリング
    return@async Bitmap.createScaledBitmap(bitmapImage, width, height, false)
  }

  override fun getItemCount(): Int { return flyerList.size }

  override fun onBindViewHolder(holder: FlyerViewHolder?, position: Int) {
    launch(UI) {
      val image = getImageStream(flyerList[position].imgUrl).await()
      holder!!.imageUrl.setImageBitmap(image)
    }
    holder!!.storeName.text = flyerList[position].storeName
    holder.description.text = flyerList[position].description
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FlyerViewHolder {
    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.flyer_list_item, parent, false)
    v.setOnClickListener(this)
    return FlyerViewHolder(v)
  }

  override fun onClick(view: View?) {
    val position = recyclerView.getChildAdapterPosition(view)
    onItemClickListener?.onItemClick(this.flyerList[position])
  }

  override fun onRefresh() {
    onRefreshPullListener?.onRefreshPull()
  }

  interface OnItemClickListener { fun onItemClick(flyer: ShufooFlyer) }
  interface OnRefreshPullListner { fun onRefreshPull() }

  class FlyerViewHolder(v: View): RecyclerView.ViewHolder(v){
    val storeName: TextView = v.findViewById(R.id.shufoo_shopName)
    val description: TextView = v.findViewById(R.id.shufoo_description)
    val imageUrl: ImageView = v.findViewById(R.id.flyer_image)
  }
}