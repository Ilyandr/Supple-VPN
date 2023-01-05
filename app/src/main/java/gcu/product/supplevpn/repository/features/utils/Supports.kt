package gcu.product.supplevpn.repository.features.utils

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.flow.MutableStateFlow


internal typealias unitAction = () -> Unit

@SuppressLint("ComposableNaming")
@Composable
internal fun @receiver:DrawableRes Int.requireImage() = painterResource(id = this)

internal object FlowSupport {

    inline infix fun <reified T : Any?> MutableStateFlow<T>.set(newState: T) = run { value = newState }
}

