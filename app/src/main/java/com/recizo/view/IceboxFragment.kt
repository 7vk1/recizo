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
import android.view.animation.Animation
import android.view.animation.AnimationSet


class IceboxFragment : Fragment(), IceboxAdapter.IceboxButtons {
  var iceboxAdapter: IceboxAdapter? = null
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
    sort_btn.setOnClickListener {
      fun createAnimation(target: View, x: Float, y: Float): ObjectAnimator {
        val xHolder = PropertyValuesHolder.ofFloat("translationX", 0f, x)
        val yHolder = PropertyValuesHolder.ofFloat("translationY", 0f, y)
        val animator = ObjectAnimator.ofPropertyValuesHolder(target, xHolder, yHolder)
        animator.duration = 500
        return animator
      }
      val set = AnimatorSet()
      set.playTogether(listOf(
          createAnimation(sort_by_name, -20f, -150f),
          createAnimation(sort_by_date, 110f, -110f),
          createAnimation(sort_by_category, 150f, 20f)))
      set.start()
    }


  }

  interface ChangeToSearchFragment {
    fun changeSearchFragment(items: Set<String>)
  }

}
