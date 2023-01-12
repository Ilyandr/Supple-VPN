package gcu.product.supplevpn.domain.models

import androidx.annotation.StringRes
import gcu.product.base.models.proxy.VpnModel

internal sealed class ConnectionsSceneModel {

    object DefaultState : ConnectionsSceneModel()
    data class LoadingState(val isLoading: Boolean) : ConnectionsSceneModel()
    object InitState : ConnectionsSceneModel()
    data class FaultState(@StringRes val error: Int) : ConnectionsSceneModel()
    data class ProxyListState(val list: List<VpnModel>) : ConnectionsSceneModel()
}