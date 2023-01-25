package gcu.product.supplevpn.repository.source.callback

internal interface ConnectionCallback {

    infix fun setConnectionStatus(currentStatus: String)
}