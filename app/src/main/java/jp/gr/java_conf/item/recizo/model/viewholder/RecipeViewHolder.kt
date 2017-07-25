package jp.gr.java_conf.item.recizo.model.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jp.gr.java_conf.item.recizo.R

class RecipeViewHolder(v: View): RecyclerView.ViewHolder(v) {
  val title: TextView = v.findViewById(R.id.recipe_title)
  val author: TextView = v.findViewById(R.id.recipe_author)
  val description: TextView = v.findViewById(R.id.recipe_description)
  val imageUrl: ImageView = v.findViewById(R.id.recipe_image)
}