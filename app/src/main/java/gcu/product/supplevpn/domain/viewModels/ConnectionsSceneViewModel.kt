package gcu.product.supplevpn.domain.viewModels

import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.supplevpn.domain.models.ConnectionsSceneModel
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.features.utils.Utils.actionWithDelay
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import gcu.product.usecase.database.connections.ConnectionsUseCase
import gcu.product.usecase.network.connections.VpnUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class ConnectionsSceneViewModel @Inject constructor(
    private val vpnUseCase: VpnUseCase,
    private val connectionsUseCase: ConnectionsUseCase,
    private val imageRequest: ImageRequest.Builder,
    private val imageLoader: ImageLoader
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
                is ConnectionsSceneModel.FaultState -> viewModelScope.actionWithDelay { setLoadingAction(false) }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() = mutableStateFlow set ConnectionsSceneModel.DefaultState

    override fun setFaultAction(error: Throwable) =
        mutableStateFlow set ConnectionsSceneModel.FaultState(handleError(error))

    override fun setLoadingAction(isLoading: Boolean) =
        mutableStateFlow set ConnectionsSceneModel.LoadingState(isLoading)

    fun requireImageRequest() = this.imageRequest

    fun requireImageLoader() = this.imageLoader

    fun requireProxyList(withLocale: Boolean = true) =
        connectionsUseCase.requireConnections().simpleRequest { localeList ->
            if (localeList.isNotEmpty() && withLocale) {
                mutableStateFlow set ConnectionsSceneModel.ProxyListState(localeList.asSequence())
            } else {
                vpnUseCase.getDefaultProxyList().simpleRequest { responseList ->
                    mutableStateFlow set ConnectionsSceneModel.ProxyListState(responseList)
                    responseList.forEach { item -> connectionsUseCase.insert(item).simpleRequest() }
                }
            }
        }

    companion object {
        const val SELECTED_VPN_KEY = "selected_vpn_key"
    }
}