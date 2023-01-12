package gcu.product.base.models.proxy

import android.os.Parcelable
import gcu.product.base.R
import kotlinx.parcelize.Parcelize


// Development state
@Parcelize
sealed class VpnTypeModel: Parcelable {

    object Default : VpnTypeModel()
    object Premium : VpnTypeModel()

    fun requireHeaderStringResByType() = when (this) {
        is Default -> R.string.proxy_list_header_default
        is Premium -> R.string.proxy_list_header_premium
    }
}