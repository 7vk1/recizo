package com.recizo.module

import android.app.NotificationManager
import android.app.Service

import android.app.PendingIntent
import android.content.Context
import com.recizo.MainActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.support.v4.app.NotificationCompat
import com.recizo.R
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.graphics.drawable.DrawableCompat
import android.os.Build
import android.graphics.drawable.Drawable












object Notification {
  fun notifyItem(drawable: Int, title: String, text: String, info: String, ticker: String) {
    val builder = NotificationCompat.Builder(AppContextHolder.context)
    builder.setSmallIcon(drawable)
    builder.setContentTitle(title)
    builder.setContentText(text)
    builder.setContentInfo(info)
    builder.setTicker(ticker)

    val bigIcon = BitmapFactory.decodeResource(AppContextHolder.context?.resources, drawable)
    builder.setLargeIcon(bigIcon)

    val intent = Intent(AppContextHolder.context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(AppContextHolder.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    builder.setContentIntent(pendingIntent)
    builder.setAutoCancel(true)
    val manager = AppContextHolder.context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(0, builder.build())
  }

  fun notifyLergeIcon(context: Context, title: String, message: String) {
    val builder = NotificationCompat.Builder(context)
    builder.setSmallIcon(R.drawable.cat_fruit)
    builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_reci_0611_01_grate))
    builder.setContentTitle(title)
    builder.setContentText(message)
    builder.setTicker(message)
    builder.setAutoCancel(true)
    val intent = Intent(AppContextHolder.context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(AppContextHolder.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    builder.setContentIntent(pendingIntent)
    builder.priority = NotificationCompat.PRIORITY_HIGH
    builder.color = ContextCompat.getColor(context, R.color.colorPrimary)
    val bigTextStyle = NotificationCompat.BigTextStyle(builder)
    bigTextStyle.setBigContentTitle(title)
    bigTextStyle.bigText(message)
    val managerCompat = NotificationManagerCompat.from(context)
    managerCompat.notify(0, builder.build())
  }

  private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    var drawable = ContextCompat.getDrawable(context, drawableId)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      drawable = DrawableCompat.wrap(drawable).mutate()
    }
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return bitmap
  }
}