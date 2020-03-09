package com.example.rpodmp_lab_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.*
import android.util.Log
import androidx.core.app.NotificationCompat


class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getStringExtra("notification")
        val notificationId = intent.getIntExtra("ID", 0)
        Log.d("TEST", notificationId.toString())
        //val notificationId = 1
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.example.rpodmp_lab_1.R.drawable.ic_launcher_foreground)
            .setContentTitle("RPOdMP Scheduled Notification")
            .setContentText(notification)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)

        notificationManager.notify(notificationId, mBuilder.build())

    }

    companion object {

        var NOTIFICATION_ID = "1000"
        var NOTIFICATION = "notification"
    }
}