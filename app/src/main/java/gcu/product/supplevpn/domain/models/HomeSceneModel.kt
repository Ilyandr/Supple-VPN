package gcu.product.supplevpn.domain.models

import gcu.product.supplevpn.repository.entities.ApplicationEntity
import gcu.product.supplevpn.repository.entities.ConnectionStatus

internal sealed class HomeSceneModel {

    object DefaultState : HomeSceneModel()

    object LoadingAppState : HomeSceneModel()

    data class ConnectionStatusState(val status: ConnectionStatus) : HomeSceneModel()

    data class InitPageState(val data: List<ApplicationEntity?>) : HomeSceneModel()
}