package gcu.product.base.models.apps

enum class ConnectionStatus {
    LOADING, CONNECTED, FAULT
}

fun String.mapToConnectionStatus() = when (this) {
    "CONNECTED" -> ConnectionStatus.CONNECTED
    else -> ConnectionStatus.LOADING
}