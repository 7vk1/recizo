package jp.gr.java_conf.item.recizo.view


import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.module.Http
import jp.gr.java_conf.item.recizo.module.RecizoApi
import kotlinx.android.synthetic.main.fragment_vegetable_graph.*


class VegetableGraphFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_vegetable_graph, container, false)
  }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    RecizoApi().past().burokkori().get(object : RecizoApi.Callback {
      override fun onSuccess(v: RecizoApi.Response) {
        val data = v.burokkori?.filter {(it.price != -1)}?.mapIndexed { index, dairyData -> Entry(index.toFloat(), dairyData.price.toFloat()) }
        val dataSet = LineDataSet(data, "bukkorori")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLUE
        dataSet.setDrawCircles(false)
        val lineData = LineData(dataSet)
        lineData.setDrawValues(false)
        chart.data = lineData
        chart.invalidate()
      }
      override fun onError(errCode: Http.ErrorCode) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }
    })
  }

}