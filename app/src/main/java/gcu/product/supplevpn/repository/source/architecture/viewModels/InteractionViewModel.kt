package gcu.product.supplevpn.repository.source.architecture.viewModels

import kotlinx.coroutines.flow.StateFlow


internal interface InteractionViewModel<StateDataType> {

    val stateFlow: StateFlow<StateDataType>
    fun actionReady()
}