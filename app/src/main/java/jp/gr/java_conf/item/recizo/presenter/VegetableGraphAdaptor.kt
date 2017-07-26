package jp.gr.java_conf.item.recizo.presenter

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import jp.gr.java_conf.item.recizo.module.Http
import jp.gr.java_conf.item.recizo.module.RecizoApi

class VegetableGraphAdaptor(private val chart: LineChart) {
  init {
    chart.description.text = "野菜の卸売価格"
    chart.isHorizontalScrollBarEnabled = false
    chart.axisRight.setDrawLabels(false)
    chart.xAxis.axisMinimum = 0f
    chart.xAxis.axisMaximum = 366f
    chart.xAxis.labelCount = 12
  }
  var dataSet: LineDataSet? = null
  var isTheOtherFinished = false
  val colors = HashMap<RecizoApi.Vegetables, Int>()
  init {
    colors.put(RecizoApi.Vegetables.burokkori, Color.rgb(255,0,0))
    colors.put(RecizoApi.Vegetables.daikon, Color.rgb(255,102,0))
    colors.put(RecizoApi.Vegetables.hakusai, Color.rgb(255,204,0))
    colors.put(RecizoApi.Vegetables.hourensou, Color.rgb(204,255,0))
    colors.put(RecizoApi.Vegetables.jagaimo, Color.rgb(102,255,0))
    colors.put(RecizoApi.Vegetables.kyabetsu, Color.rgb(0,255,0))
    colors.put(RecizoApi.Vegetables.kyuri, Color.rgb(0,255,102))
    colors.put(RecizoApi.Vegetables.nasu, Color.rgb(0,255,204))
    colors.put(RecizoApi.Vegetables.negi, Color.rgb(0,204,255))
    colors.put(RecizoApi.Vegetables.ninjin, Color.rgb(0,102,255))
    colors.put(RecizoApi.Vegetables.piman, Color.rgb(0,0,255))
    colors.put(RecizoApi.Vegetables.retasu, Color.rgb(102,0,255))
    colors.put(RecizoApi.Vegetables.satoimo, Color.rgb(204,0,255))
    colors.put(RecizoApi.Vegetables.tamanegi, Color.rgb(255,0,204))
    colors.put(RecizoApi.Vegetables.tomato, Color.rgb(255,0,102))
  }
  private fun onResponseAll(response: Map<String, List<RecizoApi.DairyData>>){
    val lists: List<LineDataSet> = response.keys.map {
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
    if(isTheOtherFinished) {
      chart.data = LineData(dataSet, this.dataSet)
      chart.xAxis.valueFormatter = XAxisValueFormatter(date.toTypedArray())
      chart.invalidate()
    } else {
      this.dataSet = dataSet
      this.isTheOtherFinished = true
    }
  }
  private fun handleError(code: Http.ErrorCode) {//TODO IMPL
  }
  fun onItemChange(v: String) {
    val vegetable = RecizoApi.Vegetables.values().find { it.name_jp == v }!!
    chart.data = LineData()
    chart.data.setDrawValues(false)
    chart.invalidate()
    dataSet = null
    isTheOtherFinished = false

    if(vegetable == RecizoApi.Vegetables.all) {
      RecizoApi().recent().all().get(object : RecizoApi.Callback {
        override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponseAll(response) }
        override fun onError(errCode: Http.ErrorCode) { handleError(errCode) }
      })
    } else {
      RecizoApi().past().vegetable(vegetable).get(object : RecizoApi.Callback {
        override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponse(response, false) }
        override fun onError(errCode: Http.ErrorCode) { handleError(errCode) }
      })
      RecizoApi().recent().vegetable(vegetable).get(object : RecizoApi.Callback {
        override fun onSuccess(response: Map<String, List<RecizoApi.DairyData>>) { onResponse(response, true) }
        override fun onError(errCode: Http.ErrorCode) { handleError(errCode) }
      })
    }
  }
  private class XAxisValueFormatter(private val mValues: Array<String>) : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
      if(mValues.size -1 < value.toInt()) return ""
      return mValues[value.toInt()]
    }
  }
}