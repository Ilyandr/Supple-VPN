package gcu.product.base.models.proxy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class VpnModel(
    val type: VpnTypeModel,
    var hostName: String? = null,
    var ipAddress: String? = null,
    var score: Int = 0,
    var ping: String? = null,
    var speed: Long = 0,
    var countryLong: String? = null,
    var countryShort: String? = null,
    var vpnSessions: Long = 0,
    var uptime: Long = 0,
    var totalUsers: Long = 0,
    var totalTraffic: String? = null,
    var logType: String? = null,
    var operator: String? = null,
    var message: String? = null,
    var ovpnConfigData: String? = null,
    var port: Int = 0,
    var protocol: String? = null,
    var isStarred: Boolean = false
) : Parcelable {

    fun requireImageHost() = "${FLAGS_API_HOST}${countryShort?.lowercase(Locale.getDefault())}.svg"

    companion object {
        private const val FLAGS_API_HOST = "https://flagcdn.com/"
    }
}