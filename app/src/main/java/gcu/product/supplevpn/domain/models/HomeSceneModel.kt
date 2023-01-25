package gcu.product.supplevpn.domain.models

import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.base.models.apps.ConnectionStatus
import gcu.product.base.models.proxy.ConnectionEntity

internal sealed class HomeSceneModel {

    object DefaultState : HomeSceneModel()

    object LoadingAppState : HomeSceneModel()

    data class UpdateLanguageState(val country: String) : HomeSceneModel()

    data class ConnectionStatusState(val status: ConnectionStatus) : HomeSceneModel()

    data class InitPageState(
        val data: List<ApplicationEntity?>,
        val currentModel: ConnectionEntity? = null,
    ) : HomeSceneModel()
}