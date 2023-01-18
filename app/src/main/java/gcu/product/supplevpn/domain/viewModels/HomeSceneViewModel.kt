package gcu.product.supplevpn.domain.viewModels

import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.presentation.other.ConnectionReceiver
import gcu.product.base.models.apps.mapToConnectionStatus
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.repository.features.utils.Constants.AUTO_VPN_KEY
import gcu.product.supplevpn.repository.features.utils.Constants.CURRENT_CONNECTION_MODEL_KEY
import gcu.product.supplevpn.repository.features.utils.Utils.requireApplicationList
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.source.architecture.other.ConnectionStateSource
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import gcu.product.usecase.database.applications.ApplicationsUseCase
import gcu.product.usecase.database.connections.ConnectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class HomeSceneViewModel @Inject constructor(
    private val packageManager: PackageManager,
    private val applicationsUseCase: ApplicationsUseCase,
    private val connectionsUseCase: ConnectionsUseCase,
    private val imageRequest: ImageRequest.Builder,
    private val imageLoader: ImageLoader,
    private val sharedPreferences: SharedPreferences,
    private val connectionReceiverIntent: IntentFilter
) : FlowableViewModel<HomeSceneModel>(), ConnectionStateSource {

    private val connectionReceiver by lazy { ConnectionReceiver(this) }
    private val mutableStateFlow: MutableStateFlow<HomeSceneModel> by lazy { MutableStateFlow(HomeSceneModel.LoadingAppState) }
    override val stateFlow by lazy { mutableStateFlow.asStateFlow() }


    init {
        this.stateFlow.onEach { currentState ->
            when (currentState) {
                is HomeSceneModel.LoadingAppState -> requireListApplications()
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() = mutableStateFlow set HomeSceneModel.DefaultState

    fun changeAppEnabled(item: ApplicationEntity) = applicationsUseCase.updateApp(item).simpleRequest()

    override fun setConnectionStatus(currentStatus: String) {
        mutableStateFlow set HomeSceneModel.ConnectionStatusState(currentStatus.mapToConnectionStatus())
    }

    infix fun saveCurrentConnection(data: ConnectionEntity) {
        sharedPreferences.edit().putString(CURRENT_CONNECTION_MODEL_KEY, data.requireFormatKey()).apply()
    }

    fun requirePackageManager() = this.packageManager

    fun requireImageRequest() = this.imageRequest

    fun requireImageLoader() = this.imageLoader

    fun requireReceiverData() = Pair(connectionReceiver, connectionReceiverIntent)

    infix fun saveAutoVpnStatus(value: Boolean) = sharedPreferences.edit().putBoolean(AUTO_VPN_KEY, value).apply()

    fun requireAutoVpnStatus() = sharedPreferences.getBoolean(AUTO_VPN_KEY, false)

    private fun requireListApplications() =
        packageManager.requireApplicationList(applicationsUseCase) { listApps ->
            sharedPreferences.getString(CURRENT_CONNECTION_MODEL_KEY, null)?.let { currentModelKey ->
                connectionsUseCase.requireModel(currentModelKey).simpleRequest { currentModel ->
                    mutableStateFlow set HomeSceneModel.InitPageState(
                        data = listApps.toMutableList().apply { removeAll { it == null || it.name.isEmpty() } },
                        currentModel = currentModel,
                    )
                }
            } ?: (mutableStateFlow set HomeSceneModel.InitPageState(
                data = listApps.toMutableList().apply { removeAll { it == null || it.name.isEmpty() } },
            ))
        }
}