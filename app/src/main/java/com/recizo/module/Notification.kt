package com.recizo.module

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.app.NotificationCompat
import com.recizo.R
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.os.Build
import java.util.*
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.preference.PreferenceManager
import com.recizo.MainActivity

object Notification {
  fun set(context: Context) {
    val time = PreferenceManager.getDefaultSharedPreferences(context).getString("alert_time", context.resources.getString(R.string.default_time)).split(":")
    setNotification(context, time[0].toInt(), time[1].toInt())
  }

  fun change(context: Context, hour: Int, minute: Int) {
    cancel(context)
    setNotification(context, hour, minute)
  }

  fun cancel(context: Context) {
    val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.setClass(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    alarm.cancel(pendingIntent)
  }

  private fun setNotification(context: Context, hour: Int, minute: Int) {
    val cal = Calendar.getInstance()
    cal.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
    val now = cal.timeInMillis
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, minute)
    cal.set(Calendar.SECOND, 0)
    if (cal.timeInMillis < now) cal.add(Calendar.DAY_OF_MONTH, 1)
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.setClass(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if(Build.VERSION.SDK_INT < 23) alarm.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    else alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
  }

  private fun notifyLargeIcon(context: Context, title: String, message: String) {
    val builder = NotificationCompat.Builder(context)
    builder.setSmallIcon(R.drawable.cat_fruit)
    builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_reci_0611_01_grate))
    builder.setContentTitle(title)
    builder.setContentText(message)
    builder.setAutoCancel(true)
    val intent = Intent(AppContextHolder.context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(AppContextHolder.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    builder.setContentIntent(pendingIntent)
    builder.priority = NotificationCompat.PRIORITY_HIGH
    builder.color = ContextCompat.getColor(context, R.color.colorPrimary)
    builder.setDefaults(NotificationCompat.DEFAULT_ALL)
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

  class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val prefManager = PreferenceManager.getDefaultSharedPreferences(context)
      val day = prefManager.getString("alert_day", context.resources.getString(R.string.default_day)).toInt()
      val cal = Calendar.getInstance()
      cal.add(Calendar.DAY_OF_MONTH, day)
      val now = cal.timeInMillis
      val items = IceboxDao.getAll().filter {
        val date = it.date.split("/")
        cal.set(Calendar.YEAR, date[0].toInt())
        cal.set(Calendar.MONTH, date[1].toInt())
        cal.set(Calendar.DAY_OF_MONTH, date[2].toInt())
        cal.timeInMillis < now
      }
      Notification.notifyLargeIcon(context, "賞味期限通知", "${items.size}つの素材の賞味期限が切れそうです！！")//TODO MESSAGE
      Notification.set(context)
    }
  }
}