package gcu.product.supplevpn.presentation.views

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import gcu.product.supplevpn.repository.features.utils.unitAction
import kotlinx.coroutines.delay

internal inline fun ComponentActivity.setupSplashScene(
    visibleTime: Long = 1500L,
    beforeSuperCallAction: unitAction,
    crossinline afterPauseAction: unitAction
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        installSplashScreen().setKeepOnScreenCondition { true }
    }
    beforeSuperCallAction.invoke()
    lifecycleScope.launchWhenCreated {
        delay(visibleTime)
        afterPauseAction.invoke()
    }
}

@Composable
internal fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}