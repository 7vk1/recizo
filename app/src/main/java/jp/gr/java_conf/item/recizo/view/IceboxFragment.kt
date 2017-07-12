package jp.gr.java_conf.item.recizo.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.gr.java_conf.item.recizo.R
import kotlinx.android.synthetic.main.fragment_icebox.*

class IceboxFragment() : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_icebox, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fab.setOnClickListener {
      startActivity(Intent(activity, RegisterActivity::class.java) )
    }
  }
}
