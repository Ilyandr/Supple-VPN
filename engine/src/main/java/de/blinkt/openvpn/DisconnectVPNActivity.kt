package de.blinkt.openvpn

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNService.LocalBinder
import de.blinkt.openvpn.core.ProfileManager

open class DisconnectVPNActivity : Activity(), DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocalBinder
            mService = binder.service
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, OpenVPNService::class.java)
        intent.action = OpenVPNService.START_SERVICE
        bindService(intent, mConnection, BIND_AUTO_CREATE)
        showDisconnectDialog()
    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
    }

    private fun showDisconnectDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.title_cancel)
        builder.setMessage(R.string.cancel_connection_query)
        builder.setNegativeButton(R.string.no, this)
        builder.setPositiveButton(R.string.yes, this)
        builder.setOnCancelListener(this)
        builder.show()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            stopVpn()
        }
        finish()
    }

    private fun stopVpn() {
        ProfileManager.setConntectedVpnProfileDisconnected(this)
        if (mService != null && mService!!.management != null) {
            mService!!.management.stopVPN(false)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        finish()
    }

    companion object {
        protected var mService: OpenVPNService? = null
    }
}