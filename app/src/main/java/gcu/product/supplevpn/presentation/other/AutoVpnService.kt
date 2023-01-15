package gcu.product.supplevpn.presentation.other

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import gcu.product.supplevpn.repository.features.utils.Utils.requireLaunchedApps
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


internal class AutoVpnService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = Binder()

    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(notificationManager) else ""
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(de.blinkt.openvpn.R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle("VPN выключен (авто-режим)")
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        startForeground(ID_SERVICE, notification)
        setLaunchedAppsListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager?): String {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.importance = NotificationManager.IMPORTANCE_NONE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager!!.createNotificationChannel(channel)
        return CHANNEL_ID
    }

    private fun setLaunchedAppsListener() =
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate({ baseContext.requireLaunchedApps() }, 2000, 1000, TimeUnit.MILLISECONDS)


    companion object {

        private const val ID_SERVICE = 177
        private const val CHANNEL_ID = "AutoVpnService_channel_id"
        private const val CHANNEL_NAME = "AutoVpnService"

        internal fun Context.launchAutoVpnService() {
            with(Intent(this, AutoVpnService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                } else {
                    startService(this)
                }
            }
        }
    }
}