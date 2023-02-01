package gcu.product.supplevpn.presentation.other

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gcu.product.supplevpn.repository.source.callback.ConnectionCallback

internal class ConnectionReceiver(private val source: ConnectionCallback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getStringExtra(STATUS_KEY)?.let { currentStatus ->
            source.setConnectionStatus(currentStatus)
        }
    }

    companion object {
        private const val STATUS_KEY = "detailstatus"
    }
}