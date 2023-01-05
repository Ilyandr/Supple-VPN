package gcu.product.supplevpn.domain.models

import gcu.product.supplevpn.repository.entities.ApplicationEntity

internal sealed class HomeSceneModel {

    object DefaultState : HomeSceneModel()

    object LoadingAppState : HomeSceneModel()

    data class InitPageState(val data: List<ApplicationEntity?>) : HomeSceneModel()
}