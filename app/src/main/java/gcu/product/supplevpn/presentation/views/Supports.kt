package gcu.product.supplevpn.presentation.views

import android.os.Build
import androidx.activity.ComponentActivity
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