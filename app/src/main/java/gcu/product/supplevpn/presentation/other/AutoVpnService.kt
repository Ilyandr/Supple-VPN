package gcu.product.supplevpn.presentation.other

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Binder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNService.engineVpnInstance
import gcu.product.base.models.apps.ConnectionStatus
import gcu.product.base.models.apps.mapToConnectionStatus
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.Constants
import gcu.product.supplevpn.repository.features.utils.Constants.CURRENT_CONNECTION_MODEL_KEY
import gcu.product.supplevpn.repository.features.utils.Utils.launchVpnService
import gcu.product.supplevpn.repository.features.utils.Utils.requireCurrentTask
import gcu.product.supplevpn.repository.features.utils.Utils.singleRequest
import gcu.product.supplevpn.repository.source.callback.ConnectionCallback
import gcu.product.usecase.database.applications.ApplicationsUseCase
import gcu.product.usecase.database.connections.ConnectionsUseCase
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
internal class AutoVpnService : Service(), ConnectionCallback {

    @Inject
    lateinit var applicationsUseCase: ApplicationsUseCase

    @Inject
    lateinit var connectionsUseCase: ConnectionsUseCase

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var connectionReceiverIntent: IntentFilter

    private val connectionReceiver by lazy { ConnectionReceiver(this) }
    private val executor by lazy { Executors.newSingleThreadScheduledExecutor() }
    private val notificationChannel: String? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        } else {
            CHANNEL_NAME
        }
    }
    private var isVpnServiceEnabled = false
    private var notificationManager: NotificationManager? = null
    private var vpnServiceIntent: Intent? = null


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        isAutoVpnServiceEnabled = true
        registerReceiver(connectionReceiver, connectionReceiverIntent)
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = Binder()

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        startForeground(SERVICE_ID, requireNotification(R.string.title_offline_vpn))
        setLaunchedAppsListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(connectionReceiver)
        } catch (_: Exception) {
        }
        executor.shutdown()
    }

    override fun setConnectionStatus(currentStatus: String) {
        notifyNotification(currentStatus.mapToConnectionStatus())
    }

    private fun requireNotification(@StringRes titleId: Int) =
        notificationChannel?.run {
            NotificationCompat.Builder(this@AutoVpnService, this)
                .setOngoing(true)
                .setSmallIcon(de.blinkt.openvpn.R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(getString(titleId))
                .setContentTitle(getString(R.string.header_clever_vpn))
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()
        }

    private fun notifyNotification(status: ConnectionStatus) = notificationManager?.notify(
        SERVICE_ID,
        requireNotification(
            when (status) {
                ConnectionStatus.CONNECTED -> R.string.title_online_vpn
                ConnectionStatus.LOADING -> R.string.title_loading_vpn
                else -> R.string.title_offline_vpn
            }
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager?): String {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.importance = NotificationManager.IMPORTANCE_NONE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager?.createNotificationChannel(channel)
        return CHANNEL_ID
    }

    private fun setLaunchedAppsListener() {
        executor.scheduleAtFixedRate({
            OpenVPNService.lastVpnStatus?.run { setConnectionStatus(this) }
            with(baseContext.requireCurrentTask()) {
                applicationsUseCase.requireApps().singleRequest { selectedApps ->
                    if (selectedApps.isNotEmpty()) {
                        diffLaunchedApps(
                            this ?: return@singleRequest,
                            selectedApps.filter { it.isEnabled }.map { it.imagePath!! }
                        )
                    }
                }
            }
        }, 2000, 1000, TimeUnit.MILLISECONDS)
    }

    private fun diffLaunchedApps(launchedApp: String, selectedApps: List<String>) {
        if ((selectedApps.contains(launchedApp)) && !isVpnServiceEnabled && launchedApp != Constants.APPLICATION_PATH) {
            vpnServiceAction(true)
        } else if (!selectedApps.contains(launchedApp) && launchedApp != Constants.APPLICATION_PATH) {
            engineVpnInstance?.stopVPN(false)
            engineVpnInstance = null
            vpnServiceAction(false)
        }
    }

    private fun vpnServiceAction(isEnabled: Boolean) {
        isVpnServiceEnabled = isEnabled
        if (isEnabled) {
            sharedPreferences.getString(CURRENT_CONNECTION_MODEL_KEY, null)?.let {
                connectionsUseCase.requireModel(it).singleRequest { currentConnection ->
                    baseContext.launchVpnService(currentConnection) { intent ->
                        vpnServiceIntent = intent
                    }
                }
            }
        }
    }


    companion object {

        private const val SERVICE_ID = 177
        private const val CHANNEL_ID = "AutoVpnService_channel_id"
        private const val CHANNEL_NAME = "AutoVpnService"
        var isAutoVpnServiceEnabled = false

        internal infix fun Context.connectAutoVpnService(isEnabled: Boolean) {
            with(Intent(this, AutoVpnService::class.java)) {
                if (!isEnabled && isAutoVpnServiceEnabled) {
                    stopService(this)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(this)
                    } else {
                        startService(this)
                    }
                }
            }
        }
    }
}