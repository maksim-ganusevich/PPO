package com.example.mediaplayer


import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationHandler {
    fun displayNotification(context: Context?, filename: String?) {
        if(context == null)
            return
        val action1Intent = Intent(context, NotificationActionService::class.java).setAction(
            ACTION_1
        )
        val action1PendingIntent =
            PendingIntent.getService(context, 0, action1Intent, PendingIntent.FLAG_ONE_SHOT)
        val bigText = NotificationCompat.BigTextStyle()
        bigText.setBigContentTitle(context.resources.getString(R.string.app_name))
        bigText.bigText(context.resources.getString(R.string.now_playing))
        bigText.setSummaryText(context.resources.getString(R.string.now_playing))

        val mBuilder = NotificationCompat.Builder(context, "notify_001")
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
        mBuilder.setContentTitle(context.resources.getString(R.string.now_playing))
        mBuilder.setContentText(filename)
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)
        mBuilder.addAction(android.R.drawable.ic_delete, "delete", action1PendingIntent)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "001"
            val channel = NotificationChannel(
                channelId,
                "Music is playing",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    class NotificationActionService :
        IntentService(NotificationActionService::class.java.simpleName) {
        override fun onHandleIntent(intent: Intent?) {
            stopService(Intent(this, MediaService::class.java))
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val ACTION_1 = "action_1"
    }
}
