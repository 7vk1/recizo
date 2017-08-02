package com.recizo.view

import android.animation.AnimatorSet
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recizo.R
import com.recizo.model.entity.IceboxItem
import kotlinx.android.synthetic.main.fragment_icebox.*
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.recizo.IceboxItemSetActivity
import com.recizo.MainActivity
import com.recizo.presenter.IceboxPresenter

class IceboxFragment : Fragment(), IceboxPresenter.IceboxButtons {
  var iceboxPresenter = IceboxPresenter(this)
  var isSortOpen = false

  private fun onSortMethodClicked(type: IceboxPresenter.Sort) {
    if(isSortOpen) {
      iceboxPresenter.sortItems(type)
      toggleSortBtn()
    }
  }

  private fun toggleSortBtn() {
    val viewSize = sort_btn.width.toFloat()
    fun createAnimation(target: View, x: Float, y: Float, close: Boolean = false): ObjectAnimator {
      val xHolder = if(!close) PropertyValuesHolder.ofFloat("translationX", 0f, x) else PropertyValuesHolder.ofFloat("translationX", x, 0f)
      val yHolder = if(!close) PropertyValuesHolder.ofFloat("translationY", 0f, y) else PropertyValuesHolder.ofFloat("translationY", y, 0f)
      val opacity = if(!close) PropertyValuesHolder.ofFloat("alpha", 0f, 1f ) else PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
      val animator = ObjectAnimator.ofPropertyValuesHolder(target, xHolder, yHolder, opacity)
      animator.duration = 200
      return animator
    }
    val set = AnimatorSet()
    set.playTogether(listOf(
        createAnimation(sort_by_created, 0 - viewSize * 0.4f, 0 - viewSize * 1.3f, isSortOpen),
        createAnimation(sort_by_name, viewSize * 0.6f, 0 - viewSize * 1.3f, isSortOpen),
        createAnimation(sort_by_date, viewSize * 1.3f, 0 - viewSize * 0.6f, isSortOpen),
        createAnimation(sort_by_category, viewSize * 1.3f, viewSize * 0.4f, isSortOpen)
    ))
    isSortOpen = !isSortOpen
    set.start()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    iceboxPresenter.setRecyclerView(recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    sort_by_name.alpha = 0f

    add_btn.setOnClickListener {
      activity.startActivity(Intent(activity, IceboxItemSetActivity::class.java))
    }
    delete_btn.setOnClickListener {
      AlertDialog.Builder(activity)
          .setMessage("削除しておk？")//todo
          .setPositiveButton("OK", { _, _ ->
            iceboxPresenter.onDeleteClicked()
          })
          .setNegativeButton("CANCEL", null)
          .show()
    }
    recipe_search_btn.setOnClickListener {
      (activity as MoveToSearchFragment).moveToSearchFragment(iceboxPresenter.getSearchItemList())
    }
    undo_btn.setOnClickListener {
      iceboxPresenter.onUndoClicked()
    }
    sort_btn.setOnClickListener { toggleSortBtn() }
    sort_by_name.setOnClickListener { onSortMethodClicked(IceboxPresenter.Sort.NAME) }
    sort_by_date.setOnClickListener { onSortMethodClicked(IceboxPresenter.Sort.DATE) }
    sort_by_category.setOnClickListener { onSortMethodClicked(IceboxPresenter.Sort.CATEGORY) }
    sort_by_created.setOnClickListener { onSortMethodClicked(IceboxPresenter.Sort.CREATED) }
  }

  override fun onResume() {
    super.onResume()
    iceboxPresenter.dataUpdated()
    (activity as MainActivity).changeSelectedNavItem(MainActivity.NavMenuItems.icebox)
  }

  override fun changeBtnVisibility(add: Boolean, undo: Boolean, search: Boolean, delete: Boolean) {
    add_btn.visibility = if(add) View.VISIBLE else View.INVISIBLE
    undo_btn.visibility = if(undo) View.VISIBLE else View.INVISIBLE
    delete_btn.visibility = if(delete) View.VISIBLE else View.INVISIBLE
    recipe_search_btn.visibility = if(search) View.VISIBLE else View.INVISIBLE
  }

  override fun toIceboxItemSetActivity(item: IceboxItem) {
    val intent = Intent(activity, IceboxItemSetActivity::class.java)
    intent.putExtra("item", item)
    activity.startActivity(intent)
  }

  override fun toSearchActivity(set: Set<String>) {
    (activity as MoveToSearchFragment).moveToSearchFragment(set)
  }

  override fun onSortMethodChange(type: IceboxPresenter.Sort) {
    val colorPrimary = ContextCompat.getColor(activity, R.color.colorPrimary)
    val defColor = Color.rgb(255, 204, 102)
    sort_by_created.backgroundTintList = ColorStateList.valueOf(defColor)
    sort_by_category.backgroundTintList = ColorStateList.valueOf(defColor)
    sort_by_date.backgroundTintList = ColorStateList.valueOf(defColor)
    sort_by_name.backgroundTintList = ColorStateList.valueOf(defColor)
    when(type) {
      IceboxPresenter.Sort.CREATED -> sort_by_created.backgroundTintList = ColorStateList.valueOf(colorPrimary)
      IceboxPresenter.Sort.CATEGORY -> sort_by_category.backgroundTintList = ColorStateList.valueOf(colorPrimary)
      IceboxPresenter.Sort.DATE -> sort_by_date.backgroundTintList = ColorStateList.valueOf(colorPrimary)
      IceboxPresenter.Sort.NAME -> sort_by_name.backgroundTintList = ColorStateList.valueOf(colorPrimary)
    }
  }

  interface MoveToSearchFragment {
    fun moveToSearchFragment(items: Set<String>)
  }
}
