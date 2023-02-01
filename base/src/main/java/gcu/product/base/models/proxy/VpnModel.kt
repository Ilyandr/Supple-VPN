package gcu.product.base.models.proxy

import android.os.Parcelable
import androidx.annotation.Keep
import gcu.product.base.models.proxy.ConnectionEntity.Companion.MB_QUALITY
import gcu.product.base.models.proxy.ConnectionEntity.Companion.requireFormatVpnKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
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

    fun mapToConnectionEntity() = ConnectionEntity(
        key = ovpnConfigData!!.requireFormatVpnKey(),
        isDefaultServer = type is VpnTypeModel.Default,
        countryLong = countryLong,
        countryShort = countryShort,
        ovpnConfigData = ovpnConfigData!!,
        ping = ping,
        speed = (speed / MB_QUALITY).toInt(),
        protocol = protocol
    )

    companion object {

        const val FLAGS_API_HOST = "https://flagcdn.com/"
    }
}