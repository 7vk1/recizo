package com.recizo.presenter

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.recizo.module.Http
import com.recizo.module.RecizoApi

class VegetableGraphPresenter(private val chart: LineChart) {
  var dataSet: LineDataSet? = null
  var isTheOtherFinished = false
  var listener: LoadEventListener? = null
  val colors = HashMap<RecizoApi.Vegetables, Int>()
  init {
    chart.description.text = "野菜の卸売価格　alic「ベジ探」(原資料_農水省「青果物日別取扱高統計」)"
    chart.isHorizontalScrollBarEnabled = false
    chart.axisRight.setDrawLabels(false)
    chart.xAxis.axisMinimum = 0f
    chart.xAxis.axisMaximum = 366f
    chart.xAxis.labelCount = 7

    colors.put(RecizoApi.Vegetables.burokkori, Color.rgb(19,107,0))
    colors.put(RecizoApi.Vegetables.daikon, Color.rgb(170,200,150))
    colors.put(RecizoApi.Vegetables.hakusai, Color.rgb(140,250,70))
    colors.put(RecizoApi.Vegetables.hourensou, Color.rgb(31,145,31))
    colors.put(RecizoApi.Vegetables.jagaimo, Color.rgb(160,106,20))
    colors.put(RecizoApi.Vegetables.kyabetsu, Color.rgb(50,240,50))
    colors.put(RecizoApi.Vegetables.kyuri, Color.rgb(28,130,29))
    colors.put(RecizoApi.Vegetables.nasu, Color.rgb(142,24,221))
    colors.put(RecizoApi.Vegetables.negi, Color.rgb(153,199,100))
    colors.put(RecizoApi.Vegetables.ninjin, Color.rgb(252,128,5))
    colors.put(RecizoApi.Vegetables.piman, Color.rgb(24,112,61))
    colors.put(RecizoApi.Vegetables.retasu, Color.rgb(100,255,77))
    colors.put(RecizoApi.Vegetables.satoimo, Color.rgb(221,197,148))
    colors.put(RecizoApi.Vegetables.tamanegi, Color.rgb(255,181,63))
    colors.put(RecizoApi.Vegetables.tomato, Color.rgb(255,99,71))
  }

  fun onItemChange(v: String) {
    listener?.onLoadStart()
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

  private fun onResponseAll(response: Map<String, List<RecizoApi.DairyData>>){
    val lists: List<LineDataSet> = response.keys.map {
      val data = (0..response[it]!!.size -1)
          .asSequence()
          .filter { i -> response[it]!![i].price != -1 }
          .map { i -> Entry(i.toFloat(),response[it]!![i].price.toFloat()) }
          .toList()
      val dataSet = LineDataSet(data, RecizoApi.Vegetables.valueOf(it).name_jp)
      dataSet.color = colors[RecizoApi.Vegetables.valueOf(it)]!!
      dataSet.valueTextColor = colors[RecizoApi.Vegetables.valueOf(it)]!!
      dataSet.setDrawCircles(false)
      dataSet.lineWidth = 3f
      dataSet
    }
    val dateList = response[response.keys.first()]!!.map { it.date }
    val lineData = LineData(lists)
    lineData.setDrawValues(false)
    chart.xAxis.valueFormatter = XAxisValueFormatter(dateList.toTypedArray())
    chart.data = lineData
    chart.invalidate()
    listener?.onLoadEnd()
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
    val dataSet = LineDataSet(list, if(isRecent) "直近１年" else "過去５年平均")
    val color = if(isRecent)colors[RecizoApi.Vegetables.valueOf(key)]!! else Color.BLACK
    dataSet.lineWidth = 3f
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
    listener?.onLoadEnd()
  }

  private fun handleError(code: Http.ErrorCode) { listener?.onError(code) }

  interface LoadEventListener {
    fun onLoadStart()
    fun onLoadEnd()
    fun onError(code: Http.ErrorCode)
  }

  private class XAxisValueFormatter(private val mValues: Array<String>) : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
      if(mValues.size -1 < value.toInt()) return ""
      return mValues[value.toInt()]
    }
  }
}