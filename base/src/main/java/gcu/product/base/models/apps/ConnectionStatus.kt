package gcu.product.base.models.apps

enum class ConnectionStatus {
    LOADING, CONNECTED, DISCONNECTED, FAULT
}

fun String.mapToConnectionStatus() = when (this) {
    "CONNECTED" -> ConnectionStatus.CONNECTED
    "NOPROCESS", "EXITING" -> ConnectionStatus.DISCONNECTED
    else -> ConnectionStatus.LOADING
}