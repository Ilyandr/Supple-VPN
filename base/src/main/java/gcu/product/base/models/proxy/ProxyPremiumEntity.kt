package gcu.product.base.models.proxy

import com.google.gson.annotations.SerializedName
import java.util.Locale

data class ProxyPremiumEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("version")
    val version: String,
    @SerializedName("ip")
    val ip: String,
    @SerializedName("host")
    val host: String,
    @SerializedName("port")
    val port: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("date_end")
    val dateEnd: String,
    @SerializedName("unixtime")
    val unixtime: Long,
    @SerializedName("unixtime_end")
    val unixtimeEnd: Long,
    @SerializedName("descr")
    val descr: Int,
    @SerializedName("active")
    val active: Int
) {
    fun mapToDefaultProxyEntity() = ProxyEntity(
        type = ProxyTypeModel.Premium(user, pass),
        address = host,
        country = country.uppercase(Locale.getDefault()),
        connectionType = type.uppercase(Locale.getDefault()),
        speedMs = -1,
        dateChecked = date,
        addressType = version.toInt()
    )
}

