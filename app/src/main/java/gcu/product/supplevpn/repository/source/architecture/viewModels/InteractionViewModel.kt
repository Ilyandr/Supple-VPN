package gcu.product.supplevpn.repository.source.architecture.viewModels

import androidx.annotation.StringRes
import gcu.product.supplevpn.repository.features.utils.unitAction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.StateFlow

internal interface InteractionViewModel<StateDataType> {

    val stateFlow: StateFlow<StateDataType>
    fun actionReady()

    fun handleError(error: Throwable): Int
    fun setFaultAction(error: Throwable) {}
    fun setFaultAction(@StringRes message: Int) {}
    fun setLoadingAction(isLoading: Boolean) {}
    infix fun <T : Any> Single<T>.simpleRequest(successAction: (T) -> Unit)

    fun Completable.simpleRequest(successAction: unitAction = {})
}