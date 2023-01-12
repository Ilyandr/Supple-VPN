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
            ConnectionsSceneModel.InitState
        )
    }
    override val stateFlow by lazy { mutableStateFlow.asStateFlow() }


    init {
        this.stateFlow.onEach { currentState ->
            when (currentState) {
                is ConnectionsSceneModel.InitState -> requireProxyList()
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() = mutableStateFlow set ConnectionsSceneModel.DefaultState

    override fun faultAction(error: Throwable) =
        mutableStateFlow set ConnectionsSceneModel.FaultState(handleError(error))

    override fun setLoadingAction(isLoading: Boolean) =
        mutableStateFlow set ConnectionsSceneModel.LoadingState(isLoading)

    private fun requireProxyList() =
        connectionUseCase.getDefaultProxyList().simpleRequest { response ->
            mutableStateFlow set ConnectionsSceneModel.ProxyListState(response)
        }
}