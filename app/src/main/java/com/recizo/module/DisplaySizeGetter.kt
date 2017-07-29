package com.recizo.module

import android.app.Activity
import android.graphics.Point
import android.view.View

object DisplaySizeGetter {
  fun getDisplaySize(activity: Activity): Point {
    val display = activity.windowManager.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
  }

  fun getRealSize(activity: Activity): Point {
    val display = activity.windowManager.defaultDisplay
    val point = Point(0, 0)
    display.getRealSize(point)
    return point
  }

  fun getViewSize(view: View): Point {
    val point = Point(0, 0)
    point.set(view.width, view.height)
    return point
  }

}