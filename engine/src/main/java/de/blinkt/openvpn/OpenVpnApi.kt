package de.blinkt.openvpn

import android.content.Context
import android.os.RemoteException
import de.blinkt.openvpn.core.ConfigParser
import de.blinkt.openvpn.core.ConfigParser.ConfigParseError
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VPNLaunchHelper
import java.io.IOException
import java.io.StringReader

object OpenVpnApi {

    private const val TAG = "OpenVpnApi"

    fun startVpn(context: Context, inlineConfig: String?, sCountry: String?, userName: String?, pw: String?) =
        startVpnInternal(context, inlineConfig, sCountry, userName, pw)

    @Throws(RemoteException::class)
    fun startVpnInternal(context: Context, inlineConfig: String?, sCountry: String?, userName: String?, pw: String?) {
        val cp = ConfigParser()
        try {
            cp.parseConfig(StringReader(inlineConfig))
            val vp = cp.convertProfile()
            vp.mName = sCountry
            if (vp.checkProfile(context) != R.string.no_error_found) {
                throw RemoteException(context.getString(vp.checkProfile(context)))
            }
            vp.mProfileCreator = context.packageName
            vp.mUsername = userName
            vp.mPassword = pw
            ProfileManager.setTemporaryProfile(context, vp)
            VPNLaunchHelper.startOpenVpn(vp, context)
        } catch (e: IOException) {
            throw RemoteException(e.message)
        } catch (e: ConfigParseError) {
            throw RemoteException(e.message)
        }
    }
}