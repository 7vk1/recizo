package com.recizo.model.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.recizo.R

class FavoriteRecipeViewHolder(v: View): RecyclerView.ViewHolder(v) {
  val title: TextView = v.findViewById(R.id.recipe_title)
  val author: TextView = v.findViewById(R.id.recipe_author)
  val description: TextView = v.findViewById(R.id.recipe_description)
  val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
  val cardFrame: CardView = v.findViewById(R.id.favorite_recipe_surface_cardview)
  val swipeLayout: SwipeLayout = v.findViewById(R.id.swipelayout)
}