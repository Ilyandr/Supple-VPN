package gcu.product.supplevpn.repository.source.architecture.viewModels

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.StateFlow

internal interface InteractionViewModel<StateDataType> {

    val stateFlow: StateFlow<StateDataType>
    fun actionReady()

    fun handleError(error: Throwable): Int
    fun faultAction(error: Throwable) {}
    fun loadingAction(isLoading: Boolean) {}
    infix fun <T : Any> Single<T>.regularRequest(successAction: (T) -> Unit)
    infix fun <T : Any> Single<T>.simpleRequest(successAction: (T) -> Unit)
}