package gcu.product.supplevpn.repository.features.utils

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.onesignal.OneSignal
import gcu.product.supplevpn.R
import kotlinx.coroutines.flow.MutableStateFlow

internal typealias unitAction = () -> Unit

@SuppressLint("ComposableNaming")
@Composable
internal fun @receiver:DrawableRes Int.requireImage() = painterResource(id = this)

internal object FlowSupport {
    inline infix fun <reified T : Any?> MutableStateFlow<T>.set(newState: T) = run { value = newState }
}

internal fun Application.initOneSignal() {
    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    OneSignal.initWithContext(this)
    OneSignal.setAppId(Constants.oneSignalId)
}

internal fun requireConnectionSpeedSource(speedMs: Int) = when {
    speedMs <= 20 -> R.drawable.ic_connection_great
    speedMs <= 40 -> R.drawable.ic_connection_good
    speedMs <= 90 -> R.drawable.ic_connection_ok
    speedMs <= 150 -> R.drawable.ic_connection_low
    else -> R.drawable.ic_connection_bad
}

internal fun requireConnectionTypeSource(type: String) = when (type) {
    "HTTP", "http" -> R.drawable.ic_http
    else -> R.drawable.ic_https
}

