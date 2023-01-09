package gcu.product.supplevpn.domain.models

import androidx.annotation.StringRes
import gcu.product.base.models.proxy.ProxyDefaultEntity
import gcu.product.base.models.proxy.ProxyPremiumEntity

internal sealed class ConnectionsSceneModel {

    object DefaultState : ConnectionsSceneModel()
    object LoadingState : ConnectionsSceneModel()
    data class FaultState(@StringRes val error: Int) : ConnectionsSceneModel()
    data class DefaultProxyListState(val list: List<ProxyDefaultEntity>) : ConnectionsSceneModel()
    data class PremiumProxyListState(val list: List<ProxyPremiumEntity>) : ConnectionsSceneModel()
}