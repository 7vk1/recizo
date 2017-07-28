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
import com.recizo.ChangeActivity
import com.recizo.R
import com.recizo.RegisterActivity
import com.recizo.model.entity.IceboxItem
import com.recizo.presenter.IceboxAdapter
import kotlinx.android.synthetic.main.fragment_icebox.*
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder

class IceboxFragment : Fragment(), IceboxAdapter.IceboxButtons {
  var iceboxAdapter: IceboxAdapter? = null
  var isSortOpen = false
  override fun changeBtnVisibility(add: Boolean, undo: Boolean, search: Boolean, delete: Boolean) {
    add_btn.visibility = if(add) View.VISIBLE else View.INVISIBLE
    undo_btn.visibility = if(undo) View.VISIBLE else View.INVISIBLE
    delete_btn.visibility = if(delete) View.VISIBLE else View.INVISIBLE
    recipe_search_btn.visibility = if(search) View.VISIBLE else View.INVISIBLE
  }

  override fun toChangeActivity(item: IceboxItem) {
    val intent = Intent(activity, ChangeActivity::class.java)
    intent.putExtra("item", item)
    activity.startActivity(intent)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onResume() {
    super.onResume()
    iceboxAdapter?.updateDataSet()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    iceboxAdapter = IceboxAdapter(this)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = iceboxAdapter
    sort_by_name.alpha = 0f

    add_btn.setOnClickListener {
      activity.startActivity(Intent(activity, RegisterActivity::class.java))
    }
    delete_btn.setOnClickListener {
      AlertDialog.Builder(activity)
          .setMessage("削除しておk？")//todo
          .setPositiveButton("OK", { _, _ ->
            iceboxAdapter?.onDeleteClicked()
          })
          .setNegativeButton("CANCEL", null)
          .show()
    }
    recipe_search_btn.setOnClickListener {
      (activity as ChangeToSearchFragment).changeSearchFragment(iceboxAdapter!!.getSearchItemList())
    }
    undo_btn.setOnClickListener {
      iceboxAdapter?.onUndoClicked()
    }
    sort_btn.setOnClickListener { toggleSortBtn() }
    sort_by_name.setOnClickListener { onSortMethodClicked(IceboxAdapter.Sort.NAME) }
    sort_by_date.setOnClickListener { onSortMethodClicked(IceboxAdapter.Sort.DATE) }
    sort_by_category.setOnClickListener { onSortMethodClicked(IceboxAdapter.Sort.CATEGORY) }
    //todo sort by item id いるか　デフォルトソート考える
  }

  private fun onSortMethodClicked(type: IceboxAdapter.Sort) {
    if(isSortOpen) {
      iceboxAdapter?.sortItems(type)
      toggleSortBtn()
    }
  }

  private fun toggleSortBtn() {
    fun createAnimation(target: View, x: Float, y: Float, close: Boolean = false): ObjectAnimator {
      val xHolder = if(!close) PropertyValuesHolder.ofFloat("translationX", 0f, x) else PropertyValuesHolder.ofFloat("translationX", x, 0f)
      val yHolder = if(!close) PropertyValuesHolder.ofFloat("translationY", 0f, y) else PropertyValuesHolder.ofFloat("translationY", y, 0f)
      val opacity = if(!close) PropertyValuesHolder.ofFloat("alpha", 0f, 1f ) else PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
      val animator = ObjectAnimator.ofPropertyValuesHolder(target, xHolder, yHolder, opacity)
      animator.duration = 500
      return animator
    }
    val set = AnimatorSet()
    set.playTogether(listOf(
        createAnimation(sort_by_name, -30f, -160f, isSortOpen),
        createAnimation(sort_by_date, 120f, -120f, isSortOpen),
        createAnimation(sort_by_category, 160f, 30f, isSortOpen)))
    isSortOpen = !isSortOpen
    set.start()
  }

  interface ChangeToSearchFragment {
    fun changeSearchFragment(items: Set<String>)
  }

}
