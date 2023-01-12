package gcu.product.supplevpn.presentation.service

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import gcu.product.supplevpn.presentation.scenes.activities.HostActivity
import gcu.product.supplevpn.repository.features.utils.Constants.VPN_MTU_CACHE
import gcu.product.supplevpn.repository.source.architecture.other.VpnServiceSource
import java.io.FileInputStream
import java.io.FileOutputStream


internal class SuppleVpnService : VpnService(), VpnServiceSource {

    private val vpnThread by lazy { Thread(this, VPN_SERVICE_THREAD_KEY) }
    private var inputStream: FileInputStream? = null
    private var outputStream: FileOutputStream? = null
    private var descriptor: ParcelFileDescriptor? = null
    private var isRun = false


    override fun onCreate() {
        super.onCreate()
        vpnThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }


    override fun run() {
        try {
            isRun = true
            openConnection()
        } catch (_: Exception) {
        } finally {
            stop()
        }
    }

    private fun openConnection() {
        //if (dataOptions == null) return
        Builder().setMtu(VPN_MTU_CACHE)
          /*  .setSession(VPN_SESSION_NAME).addAddress("10.0.0.2", 32)
            .addRoute(
                "0.0.0.0" // Intercept everything
                , 0)*/
            .establish()?.let { descriptor ->
                Log.e("dataX", "launch")
               // DatagramChannel.open().connect(InetSocketAddress("1.179.144.41", 8080))

                this.descriptor = descriptor
                inputStream = FileInputStream(descriptor.fileDescriptor)
                outputStream = FileOutputStream(descriptor.fileDescriptor)
                val bytes = ByteArray(VPN_MTU_CACHE)
                var size = 0

                while (isRun && size != -1) {
                    size = inputStream?.read(bytes) ?: 0
                    if (size <= 0) {
                        Thread.sleep(20)
                        continue
                    }
                }
            }
    }

    @Synchronized
    fun stop() {
        inputStream?.close()
        descriptor?.close()
        isRun = false
        stopSelf()
    }


    companion object {
        private const val VPN_SERVICE_THREAD_KEY = "SuppleVpnService"
        fun Context.requireVpnIntent() = Intent((this as HostActivity), SuppleVpnService::class.java)
    }
}

