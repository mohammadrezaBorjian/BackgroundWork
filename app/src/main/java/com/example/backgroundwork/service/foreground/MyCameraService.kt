package com.example.backgroundwork.service.foreground

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.backgroundwork.R

class MyCameraService: Service() {
    companion object{
        const val CHANNEL_ID = "10"
    }
    private lateinit var notificationManager: NotificationManager

    private lateinit var customNotificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        customNotificationBuilder =
            NotificationCompat.Builder(baseContext, CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle("notification")
                .setSilent(true)
                .setOngoing(true)

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

  private fun startForeground() {
    // Before starting the service as foreground check that the app has the
    // appropriate runtime permissions. In this case, verify that the user has
    // granted the CAMERA permission.
//    val cameraPermission =
//            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//    if (cameraPermission == PackageManager.PERMISSION_DENIED) {
//        // Without camera permissions the service cannot run in the foreground
//        // Consider informing user or updating your app UI if visible.
//        stopSelf()
//        return
//    }
      startForeground(100 , createNotification())
  }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
            customNotificationBuilder.setChannelId(channelId)
        }
    }
    fun createNotification(): Notification {
        createNotificationChannel()
        return customNotificationBuilder.build()
    }
}