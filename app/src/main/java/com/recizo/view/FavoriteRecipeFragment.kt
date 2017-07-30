package com.recizo.view

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.presenter.FavoriteRecipeAdapter
import com.recizo.presenter.FavoriteRecipePresenter
import kotlinx.android.synthetic.main.fragment_favorite_recipe.*

class FavoriteRecipeFragment: Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_favorite_recipe, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fragment_favorite_recipe_frame.layoutManager = LinearLayoutManager(activity)
    val removeBtn: FloatingActionButton = view!!.findViewById(R.id.favorite_recipe_remove_btn)
    val undoBtn: FloatingActionButton = view.findViewById(R.id.favorite_recipe_undo_btn)
    val presenter =  FavoriteRecipePresenter(fragment_favorite_recipe_frame, removeBtn, undoBtn)

    removeBtn.setOnClickListener {
      presenter.onRemoveClicked()
    }

    undoBtn.setOnClickListener {
      presenter.onUndoClicked()
    }

  }
}