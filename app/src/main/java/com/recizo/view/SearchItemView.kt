package com.recizo.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.recizo.R

class SearchItemView(context: Context, private var item: String) : LinearLayout(context) {
  val layout: View = LayoutInflater.from(context).inflate(R.layout.view_search_item, this)
  val textView: TextView = layout.findViewById(R.id.textView)
  val imageView: ImageView = layout.findViewById(R.id.imageView)
  var listener: OnCloseClickListener? = null
  init {
    textView.text = item
    imageView.setOnClickListener { this.listener?.onClick(item) }
  }

  fun setOnCloseClickListener(listener: OnCloseClickListener) { this.listener = listener }

  override fun onSaveInstanceState(): Parcelable {
    val ss = SavedState(super.onSaveInstanceState())
    ss.item = item
    return ss
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    if(state !is SavedState) { super.onRestoreInstanceState(state); return }
    val ss = state
    super.onRestoreInstanceState(state)// todo
    item = ss.item ?: ""
  }

  interface OnCloseClickListener {
    fun onClick(item: String)
  }

  class SavedState : BaseSavedState {
    var item: String? = null

    constructor(superState: Parcelable) : super(superState)
    constructor(`in`: Parcel) : super(`in`) {
      item = `in`.readString()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
      super.writeToParcel(out, flags)
      out.writeString(item)
    }

    companion object {
      val CREATOR = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(`in`: Parcel): SavedState {
          return SavedState(`in`)
        }
        override fun newArray(size: Int): Array<SavedState?> {
          return arrayOfNulls(size)
        }
      }
    }
  }
}
