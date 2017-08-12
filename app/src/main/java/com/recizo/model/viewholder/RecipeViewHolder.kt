package com.recizo.model.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.recizo.R

class RecipeViewHolder(v: View): RecyclerView.ViewHolder(v) {
  val title: TextView = v.findViewById(R.id.recipe_title)
  val author: TextView = v.findViewById(R.id.recipe_author)
  val description: TextView = v.findViewById(R.id.recipe_description)
  val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
  var linkUrl: String = ""
  val layout: View = v.findViewById(R.id.search_recipe_item_frame)
}