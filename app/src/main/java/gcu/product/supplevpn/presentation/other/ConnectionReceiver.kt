package gcu.product.supplevpn.presentation.other

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gcu.product.supplevpn.domain.viewModels.HomeSceneViewModel

internal class ConnectionReceiver(private val viewModel: HomeSceneViewModel) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.getStringExtra("detailstatus")?.let { currentStatus ->
            viewModel.setConnectionStatus(currentStatus)
        }
    }
}