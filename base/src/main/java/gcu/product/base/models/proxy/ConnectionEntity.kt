package gcu.product.base.models.proxy

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import gcu.product.base.R
import gcu.product.base.models.Constants
import gcu.product.base.models.proxy.VpnModel.Companion.FLAGS_API_HOST
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Entity(tableName = Constants.CONNECTIONS_ENTITY_NAME)
@Parcelize
@Keep
data class ConnectionEntity(
    @PrimaryKey val key: String,
    @ColumnInfo("ssh") var ovpnConfigData: String,
    @ColumnInfo("type") val isDefaultServer: Boolean,
    @ColumnInfo("country_long") val countryLong: String? = null,
    @ColumnInfo("speed") val speed: Int = 0,
    @ColumnInfo("country_short") var countryShort: String? = null,
    @ColumnInfo("ping") var ping: String? = null,
    @ColumnInfo("protocol") var protocol: String? = null,
) : Parcelable {

    fun requireHeaderStringResByFlag() = when (isDefaultServer) {
        true -> R.string.proxy_list_header_default
        false -> R.string.proxy_list_header_premium
    }

    fun requireImageHost() = "$FLAGS_API_HOST${countryShort?.lowercase(Locale.getDefault())}.svg"

    fun requireFormatKey() = ovpnConfigData.replaceFirst("remote", "").split("remote")[1].split("#")[0].trim()

    companion object {

        const val MB_QUALITY = 1000000

        fun String.requireFormatVpnKey() = replaceFirst("remote", "").split("remote")[1].split("#")[0].trim()

        infix fun requireImageHost(countryShort: String) =
            "$FLAGS_API_HOST${countryShort.lowercase(Locale.getDefault())}.svg"
    }
}
