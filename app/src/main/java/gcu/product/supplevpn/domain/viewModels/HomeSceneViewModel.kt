package gcu.product.supplevpn.domain.viewModels

import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.presentation.other.ConnectionReceiver
import gcu.product.supplevpn.repository.entities.mapToConnectionStatus
import gcu.product.supplevpn.repository.features.utils.Utils.requireApplicationList
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeSceneViewModel @Inject constructor(
    private val packageManager: PackageManager,
    private val userPreferences: SharedPreferences,
    private val imageRequest: ImageRequest.Builder,
    private val imageLoader: ImageLoader,
    private val connectionReceiverIntent: IntentFilter
) : FlowableViewModel<HomeSceneModel>() {

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

    infix fun setConnectionStatus(status: String) {
        mutableStateFlow set HomeSceneModel.ConnectionStatusState(status.mapToConnectionStatus())
    }

    fun requirePackageManager() = this.packageManager

    fun requireImageRequest() = this.imageRequest

    fun requireImageLoader() = this.imageLoader

    fun requireReceiverData() = Pair(connectionReceiver, connectionReceiverIntent)

    private fun requireListApplications() =
        viewModelScope.launch(Dispatchers.Main) {
            mutableStateFlow set HomeSceneModel.InitPageState(
                (packageManager requireApplicationList userPreferences).toMutableList().apply {
                    removeAll { it == null || it.name.isNullOrEmpty() }
                })
        }
}