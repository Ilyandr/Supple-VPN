package gcu.product.base.models.proxy

import gcu.product.base.R

sealed class ProxyTypeModel {

    object Default : ProxyTypeModel()
    data class Premium(val user: String, val password: String) : ProxyTypeModel()

    fun requireHeaderStringResByType() = when (this) {
        is Default -> R.string.proxy_list_header_default
        is Premium -> R.string.proxy_list_header_premium
    }
}