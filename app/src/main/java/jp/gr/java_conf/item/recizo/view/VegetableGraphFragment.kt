package jp.gr.java_conf.item.recizo.view


import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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


class VegetableGraphFragment : Fragment() {

  private class LineDataAdapter(private val chart: LineChart) {
    var dataSet: LineDataSet? = null
    var isOtherFinish = false
    val colors = HashMap<RecizoApi.Vegetables, Int>()
    init {
      colors.put(RecizoApi.Vegetables.burokkori, Color.rgb(255,0,0))
      colors.put(RecizoApi.Vegetables.daikon,Color.rgb(255,102,0))
      colors.put(RecizoApi.Vegetables.hakusai,Color.rgb(255,204,0))
      colors.put(RecizoApi.Vegetables.hourensou,Color.rgb(204,255,0))
      colors.put(RecizoApi.Vegetables.jagaimo,Color.rgb(102,255,0))
      colors.put(RecizoApi.Vegetables.kyabetsu,Color.rgb(0,255,0))
      colors.put(RecizoApi.Vegetables.kyuri,Color.rgb(0,255,102))
      colors.put(RecizoApi.Vegetables.nasu,Color.rgb(0,255,204))
      colors.put(RecizoApi.Vegetables.negi,Color.rgb(0,204,255))
      colors.put(RecizoApi.Vegetables.ninjin,Color.rgb(0,102,255))
      colors.put(RecizoApi.Vegetables.piman,Color.rgb(0,0,255))
      colors.put(RecizoApi.Vegetables.retasu,Color.rgb(102,0,255))
      colors.put(RecizoApi.Vegetables.satoimo,Color.rgb(204,0,255))
      colors.put(RecizoApi.Vegetables.tamanegi,Color.rgb(255,0,204))
      colors.put(RecizoApi.Vegetables.tomato,Color.rgb(255,0,102))
    }
    private fun onResponseAll(response: Map<String, List<RecizoApi.DairyData>>){
      val lists: List<LineDataSet> = response.keys.map {
        Log.d("DD", response[it]!![0].toString())
        val data = mutableListOf<Entry>()
        for(i in 0..response[it]!!.size -1) {
          if(response[it]!![i].price != -1) data.add(Entry(i.toFloat(),response[it]!![i].price.toFloat()))
        }
        val dataSet = LineDataSet(data, RecizoApi.Vegetables.valueOf(it).name_jp)
        dataSet.color = colors[RecizoApi.Vegetables.valueOf(it)]!!
        dataSet.valueTextColor = colors[RecizoApi.Vegetables.valueOf(it)]!!
        dataSet.setDrawCircles(false)
        dataSet
      }
      val dateList = response[response.keys.first()]!!.map { it.date }
      val lineData = LineData(lists)
      lineData.setDrawValues(false)
      chart.xAxis.valueFormatter = XAxisValueFormatter(dateList.toTypedArray())
      chart.data = lineData
      chart.invalidate()
    }
    private fun onResponse(response: Map<String, List<RecizoApi.DairyData>>, isRecent: Boolean){
      val key = response.keys.first()
      val vegetableData = response[key]!!
      val list = mutableListOf<Entry>()
      val date = mutableListOf<String>()
      for(i in 0..vegetableData.size -1) {
        date.add(vegetableData[i].date)
        if(vegetableData[i].price != -1) list.add(Entry(i.toFloat(), vegetableData[i].price.toFloat()))
      }
      val dataSet = LineDataSet(list, if(isRecent) "今年" else "過去")
      val color = if(isRecent)colors[RecizoApi.Vegetables.valueOf(key)]!! else Color.BLACK
      dataSet.color = color
      dataSet.valueTextColor = color
      dataSet.setDrawCircles(false)
      if(isOtherFinish) {
        chart.data = LineData(dataSet, this.dataSet)
        chart.xAxis.valueFormatter = XAxisValueFormatter(date.toTypedArray())
        chart.invalidate()
      } else {
        this.dataSet = dataSet
        this.isOtherFinish = true
      }
    }
    private fun onError(code: Http.ErrorCode) {//TODO IMPL
    }
    fun onItemChange(v: VegetableData) {
      chart.data = LineData()
      chart.data.setDrawValues(false)
      chart.invalidate()
      dataSet = null
      isOtherFinish = false

      if( v.vegetable == RecizoApi.Vegetables.all) {
        RecizoApi().recent().all().get(object : RecizoApi.Callback {
          override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponseAll(response) }
          override fun onError(errCode: Http.ErrorCode) { this.onError(errCode) }
        })
      } else {
        RecizoApi().past().vegetable(v.vegetable).get(object : RecizoApi.Callback {
          override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponse(response, false) }
          override fun onError(errCode: Http.ErrorCode) { this.onError(errCode) }
        })
        RecizoApi().recent().vegetable(v.vegetable).get(object : RecizoApi.Callback {
          override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponse(response, true) }
          override fun onError(errCode: Http.ErrorCode) { this.onError(errCode) }
        })
      }
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