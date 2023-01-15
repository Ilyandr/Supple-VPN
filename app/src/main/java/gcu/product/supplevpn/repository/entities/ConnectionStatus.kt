package gcu.product.supplevpn.repository.entities

internal enum class ConnectionStatus {
    LOADING, CONNECTED, FAULT
}

internal fun String.mapToConnectionStatus() = when (this) {
    "CONNECTED" -> ConnectionStatus.CONNECTED
    else -> ConnectionStatus.LOADING
}