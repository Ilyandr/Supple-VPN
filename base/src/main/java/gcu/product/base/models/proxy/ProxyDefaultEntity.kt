package gcu.product.base.models.proxy

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.util.Locale

@Keep
data class ProxyDefaultEntity(
    @SerializedName("name")
    val address: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("type")
    val connectionType: String,
    @SerializedName("speed")
    val speedMs: Long,
    @SerializedName("upd")
    val dateChecked: String,
    @SerializedName("work")
    val addressType: Int
) {
    fun requireImageHost() = "$FLAGS_API_HOST$country"

    companion object {
        private const val FLAGS_API_HOST = "https://countryflagsapi.com/png/"
    }
}