package gcu.product.supplevpn.domain.viewModels

import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blinkt.openvpn.core.OpenVPNService
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.base.models.apps.mapToConnectionStatus
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.presentation.other.ConnectionReceiver
import gcu.product.supplevpn.repository.features.utils.Constants.APPLICATION_PATH
import gcu.product.supplevpn.repository.features.utils.Constants.AUTO_VPN_KEY
import gcu.product.supplevpn.repository.features.utils.Constants.CURRENT_CONNECTION_MODEL_KEY
import gcu.product.supplevpn.repository.features.utils.Constants.CURRENT_LANGUAGE_KEY
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.features.utils.Utils.actionWithDelay
import gcu.product.supplevpn.repository.features.utils.Utils.requireApplicationList
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import gcu.product.supplevpn.repository.source.callback.ApplicationsCallback
import gcu.product.supplevpn.repository.source.callback.ConnectionCallback
import gcu.product.supplevpn.repository.source.callback.LanguageCallback
import gcu.product.usecase.database.applications.ApplicationsUseCase
import gcu.product.usecase.database.connections.ConnectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
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
) : FlowableViewModel<HomeSceneModel>(), ConnectionCallback, ApplicationsCallback, LanguageCallback {

    private val connectionReceiver by lazy { ConnectionReceiver(this) }
    private val mutableStateFlow: MutableStateFlow<HomeSceneModel> by lazy {
        MutableStateFlow(
            HomeSceneModel.UpdateLanguageState(
                requireCurrentLanguage()
            )
        )
    }
    override val stateFlow by lazy { mutableStateFlow.asStateFlow() }


    init {
        this.stateFlow.onEach { currentState ->
            when (currentState) {
                is HomeSceneModel.LoadingAppState -> {
                    requireListApplications()
                    viewModelScope.actionWithDelay {
                        OpenVPNService.lastVpnStatus?.run {
                            setConnectionStatus(this)
                        }
                    }
                }

                is HomeSceneModel.UpdateLanguageState -> {
                    viewModelScope.actionWithDelay { mutableStateFlow set HomeSceneModel.LoadingAppState }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() = mutableStateFlow set HomeSceneModel.DefaultState

    override fun changeAppEnabled(item: ApplicationEntity) = applicationsUseCase.updateApp(item).simpleRequest()

    override fun setConnectionStatus(currentStatus: String) {
        mutableStateFlow set HomeSceneModel.ConnectionStatusState(currentStatus.mapToConnectionStatus())
    }

    override fun requirePackageManager() = this.packageManager
    override fun requireImageRequest() = this.imageRequest
    override fun requireImageLoader() = this.imageLoader

    override fun saveCurrentLanguage(value: String) {
        sharedPreferences.edit().putString(CURRENT_LANGUAGE_KEY, value).apply()
        mutableStateFlow set HomeSceneModel.UpdateLanguageState(value)
    }

    override fun requireCurrentLanguage() =
        sharedPreferences.getString(CURRENT_LANGUAGE_KEY, Locale.getDefault().language)!!

    infix fun saveCurrentConnection(data: ConnectionEntity) {
        sharedPreferences.edit().putString(CURRENT_CONNECTION_MODEL_KEY, data.requireFormatKey()).apply()
    }

    fun requireReceiverData() = Pair(connectionReceiver, connectionReceiverIntent)

    fun requireAutoVpnStatus() =
        sharedPreferences.getBoolean(AUTO_VPN_KEY, false)

    infix fun saveAutoVpnStatus(value: Boolean) =
        sharedPreferences.edit().putBoolean(AUTO_VPN_KEY, value).apply()

    private fun requireListApplications() =
        packageManager.requireApplicationList(applicationsUseCase) { listApps ->
            sharedPreferences.getString(CURRENT_CONNECTION_MODEL_KEY, null)?.let { currentModelKey ->
                connectionsUseCase.requireModel(currentModelKey).simpleRequest { currentModel ->
                    mutableStateFlow set HomeSceneModel.InitPageState(
                        data = listApps.toMutableList()
                            .apply { removeAll { it == null || it.name.isEmpty() || it.imagePath == APPLICATION_PATH } },
                        currentModel = currentModel,
                    )
                }
            } ?: (mutableStateFlow set HomeSceneModel.InitPageState(
                data = listApps.toMutableList().apply { removeAll { it == null || it.name.isEmpty() } },
            ))
        }
}