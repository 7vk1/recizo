package jp.gr.java_conf.item.recizo.view


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.model.entity.ShufooFlyer
import jp.gr.java_conf.item.recizo.module.ShufooScraper
import jp.gr.java_conf.item.recizo.presenter.FlyerListAdapter
import jp.gr.java_conf.item.recizo.presenter.FlyerPresenter
import kotlinx.android.synthetic.main.searched_recipe_list.*


class FlyerFragment : Fragment(), FlyerPresenter.IFlyerFragment{

  val flyerListAdapter = FlyerListAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.searched_recipe_list, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    searched_recyclerView.layoutManager = LinearLayoutManager(activity)
    searched_recyclerView.addItemDecoration(DividerItemDecoration(
            searched_recyclerView.context,
            LinearLayoutManager(activity).orientation)
    )
    searched_recyclerView.adapter = flyerListAdapter

    val flyerPresenter = FlyerPresenter(ShufooScraper("1690074"))
    flyerPresenter.setView(this)
    flyerPresenter.startFlyerListCreate()

    searched_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        flyerPresenter.addFlyerList(recyclerView, dy)
      }
    })
  }
  override fun onStart() {
    super.onStart()
  }

  override fun showProgress() {
    searched_recipe_progressBar.visibility = View.VISIBLE
  }

  override fun dismissProgress() {
    searched_recipe_progressBar.visibility = View.GONE
  }

  override fun setResultToList(flyer: ShufooFlyer) {
    flyerListAdapter.addFlyer(flyer)
  }
}
