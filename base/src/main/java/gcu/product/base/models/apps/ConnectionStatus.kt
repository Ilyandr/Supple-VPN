package gcu.product.base.models.apps

enum class ConnectionStatus {
    LOADING, CONNECTED, FAULT
}

fun String.mapToConnectionStatus() = when (this) {
    "CONNECTED" -> ConnectionStatus.CONNECTED
    "AUTH_FAILED", "NOPROCESS", "EXITING" -> ConnectionStatus.FAULT
    else -> ConnectionStatus.LOADING
}