package gcu.product.supplevpn.domain.viewModels

import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.supplevpn.domain.models.ConnectionsSceneModel
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import gcu.product.usecase.connections.ConnectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class ConnectionsSceneViewModel @Inject constructor(
    private val connectionUseCase: ConnectionUseCase,
    val imageRequest: ImageRequest.Builder
) : FlowableViewModel<ConnectionsSceneModel>() {

    private val mutableStateFlow: MutableStateFlow<ConnectionsSceneModel> by lazy {
        MutableStateFlow(
            ConnectionsSceneModel.LoadingState
        )
    }
    override val stateFlow by lazy { mutableStateFlow.asStateFlow() }


    init {
        this.stateFlow.onEach { currentState ->
            when (currentState) {
                is ConnectionsSceneModel.LoadingState -> requireDefaultProxyList()
               // is ConnectionsSceneModel.DefaultProxyListState -> requirePremiumProxyList()
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() = mutableStateFlow set ConnectionsSceneModel.DefaultState

    override fun faultAction(error: Throwable) =
        mutableStateFlow set ConnectionsSceneModel.FaultState(handleError(error))

    override fun loadingAction(isLoading: Boolean) =
        mutableStateFlow set if (isLoading) ConnectionsSceneModel.LoadingState else ConnectionsSceneModel.DefaultState

    private fun requireDefaultProxyList() {
        connectionUseCase.getDefaultProxyList().regularRequest { response ->
            mutableStateFlow set ConnectionsSceneModel.DefaultProxyListState(response)
        }
    }

    private fun requirePremiumProxyList() {
        connectionUseCase.getPremiumProxyList().regularRequest { response ->
            mutableStateFlow set ConnectionsSceneModel.PremiumProxyListState(response)
        }
    }
}