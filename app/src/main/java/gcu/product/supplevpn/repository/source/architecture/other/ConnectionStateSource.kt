package gcu.product.supplevpn.repository.source.architecture.other

internal interface ConnectionStateSource {

    infix fun setConnectionStatus(currentStatus: String)
}