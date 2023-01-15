package gcu.product.supplevpn.repository.features.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import de.blinkt.openvpn.OpenVpnApi
import gcu.product.base.models.proxy.VpnModel
import gcu.product.gateway.Constants.VPN_API_LOGIN
import gcu.product.gateway.Constants.VPN_API_PASSWORD
import gcu.product.supplevpn.repository.entities.ApplicationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal object Utils {

    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    infix fun PackageManager.isSystemApp(app: ApplicationInfo) = try {
        val targetPkgInfo: PackageInfo = getPackageInfo(
            app.packageName, PackageManager.GET_SIGNATURES
        )
        val sys: PackageInfo = getPackageInfo(
            "android", PackageManager.GET_SIGNATURES
        )
        targetPkgInfo.signatures != null && sys.signatures.first() == targetPkgInfo.signatures.first()
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

    @Suppress("DEPRECATION")
    suspend infix fun PackageManager.requireApplicationList(userPreferences: SharedPreferences) =
        withContext(Dispatchers.IO) {
            getInstalledApplications(PackageManager.GET_META_DATA).map { singleInfo ->
                if (!isSystemApp(singleInfo)) {
                    val label = getApplicationLabel(singleInfo)
                    if (label.length <= 16) {
                        ApplicationEntity(
                            name = getApplicationLabel(singleInfo),
                            isLaunched = singleInfo.enabled,
                            imagePath = singleInfo.packageName,
                            isEnabled = userPreferences.getBoolean(singleInfo.name, false)
                        )
                    } else null
                } else null
            }
        }

    internal infix fun Context.launchVpnService(item: VpnModel?) = item?.run {
        OpenVpnApi.startVpn(
            this@launchVpnService,
            ovpnConfigData,
            countryShort,
            VPN_API_LOGIN,
            VPN_API_PASSWORD
        )
    }
}