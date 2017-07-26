package jp.gr.java_conf.item.recizo.view


import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import jp.gr.java_conf.item.recizo.R
import jp.gr.java_conf.item.recizo.module.Http
import jp.gr.java_conf.item.recizo.module.RecizoApi
import kotlinx.android.synthetic.main.fragment_vegetable_graph.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class VegetableGraphFragment : Fragment() {

  private class LineDataAdapter(private val chart: LineChart) {
    private var xAxisValue: Array<String> = arrayOf()

    private fun createDataSet(arr: Array<RecizoApi.DairyData>, vData: VegetableData): LineDataSet {
      val data = mutableListOf<Entry>()
      val dateList = mutableListOf<String>()
      for(i in 0..arr.size - 1) {
        if(arr[i].price != -1) data.add(Entry(i.toFloat(), arr[i].price!!.toFloat()))
      }

      this.xAxisValue = dateList.toTypedArray()
//      val data = arr.filter {(it.price != -1)}.mapIndexed { index, ( date, price) ->
//        dateList.add(date)
//        Entry(index.toFloat(), price.toFloat())}
      val dataSet = LineDataSet(data, vData.vegetable.name_jp)
      dataSet.color = vData.color
      dataSet.valueTextColor = vData.color
      dataSet.setDrawCircles(false)
      return dataSet
    }
    private fun onResponse(response: RecizoApi.Response, vegetableData: VegetableData){
      val dataSet = when (vegetableData.vegetable) {
        RecizoApi.Vegetables.all -> createDataSet(response.burokkori!!, vegetableData)//todo impl all
        RecizoApi.Vegetables.burokkori ->  createDataSet(response.burokkori!!, vegetableData)
        RecizoApi.Vegetables.daikon -> createDataSet(response.daikon!!, vegetableData)
        RecizoApi.Vegetables.hakusai -> createDataSet(response.hakusai!!, vegetableData)
        RecizoApi.Vegetables.hourensou -> createDataSet(response.hourensou!!, vegetableData)
        RecizoApi.Vegetables.jagaimo -> createDataSet(response.jagaimo!!, vegetableData)
        RecizoApi.Vegetables.kyabetsu -> createDataSet(response.kyabetsu!!, vegetableData)
        RecizoApi.Vegetables.kyuri -> createDataSet(response.kyuri!!, vegetableData)
        RecizoApi.Vegetables.nasu -> createDataSet(response.nasu!!, vegetableData)
        RecizoApi.Vegetables.negi -> createDataSet(response.negi!!, vegetableData)
        RecizoApi.Vegetables.ninjin -> createDataSet(response.ninjin!!, vegetableData)
        RecizoApi.Vegetables.piman -> createDataSet(response.piman!!, vegetableData)
        RecizoApi.Vegetables.retasu -> createDataSet(response.retasu!!, vegetableData)
        RecizoApi.Vegetables.satoimo -> createDataSet(response.satoimo!!, vegetableData)
        RecizoApi.Vegetables.tamanegi -> createDataSet(response.tamanegi!!, vegetableData)
        RecizoApi.Vegetables.tomato -> createDataSet(response.tomato!!, vegetableData)
      }
      dataSet.color = vegetableData.color
      dataSet.valueTextColor = vegetableData.color
      dataSet.setDrawCircles(false)
      val lineData = LineData(mutableListOf(dataSet) as MutableList<ILineDataSet>)
      lineData.setDrawValues(false)
      chart.xAxis.valueFormatter = XAxisValueFormatter(xAxisValue)
      chart.data = lineData
      chart.invalidate()
    }
    private fun getDateList(response: RecizoApi.Response) {
    }
    fun onItemChange(v: VegetableData) {
      RecizoApi().recent().vegetable(v.vegetable).get(object : RecizoApi.Callback {
        override fun onSuccess(response: RecizoApi.Response) {
          onResponse(response, v)
        }
        override fun onError(errCode: Http.ErrorCode) {
          TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
      })
    }
  }


  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater!!.inflate(R.layout.fragment_vegetable_graph, container, false)
  }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val lineDataAdapter = LineDataAdapter(chart)
    val adapter = ArrayAdapter<VegetableData>(activity, android.R.layout.simple_spinner_item)
    spinner.adapter = adapter
    vegetables.forEach { adapter.add(it) }
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        lineDataAdapter.onItemChange(adapter.getItem(p2))
      }
      override fun onNothingSelected(p0: AdapterView<*>?) {
      }
    }
  }






  private data class VegetableData(
      val vegetable: RecizoApi.Vegetables,
      val color: Int
  ) {
    override fun toString(): String {
      return vegetable.name_jp
    }
  }

  companion object {
    private val vegetables = arrayListOf(
        VegetableData(RecizoApi.Vegetables.all, Color.rgb(0,0,0)),
        VegetableData(RecizoApi.Vegetables.burokkori, Color.rgb(255,0,0)),
        VegetableData(RecizoApi.Vegetables.daikon,Color.rgb(255,102,0)),
        VegetableData(RecizoApi.Vegetables.hakusai,Color.rgb(255,204,0)),
        VegetableData(RecizoApi.Vegetables.hourensou,Color.rgb(204,255,0)),
        VegetableData(RecizoApi.Vegetables.jagaimo,Color.rgb(102,255,0)),
        VegetableData(RecizoApi.Vegetables.kyabetsu,Color.rgb(0,255,0)),
        VegetableData(RecizoApi.Vegetables.kyuri,Color.rgb(0,255,102)),
        VegetableData(RecizoApi.Vegetables.nasu,Color.rgb(0,255,204)),
        VegetableData(RecizoApi.Vegetables.negi,Color.rgb(0,204,255)),
        VegetableData(RecizoApi.Vegetables.ninjin,Color.rgb(0,102,255)),
        VegetableData(RecizoApi.Vegetables.piman,Color.rgb(0,0,255)),
        VegetableData(RecizoApi.Vegetables.retasu,Color.rgb(102,0,255)),
        VegetableData(RecizoApi.Vegetables.satoimo,Color.rgb(204,0,255)),
        VegetableData(RecizoApi.Vegetables.tamanegi,Color.rgb(255,0,204)),
        VegetableData(RecizoApi.Vegetables.tomato,Color.rgb(255,0,102)))
  }

  class XAxisValueFormatter(private val mValues: Array<String>) : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
      return mValues[value.toInt()]
    }
  }
}