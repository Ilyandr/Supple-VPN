package gcu.product.supplevpn.domain.viewModels

import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.repository.features.utils.FileUtils.requireApplicationList
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
    private val userPreferences: SharedPreferences
) : FlowableViewModel<HomeSceneModel>() {

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

    private fun requireListApplications() =
        viewModelScope.launch(Dispatchers.Main) {
            mutableStateFlow set HomeSceneModel.InitPageState((packageManager requireApplicationList userPreferences))
        }

    override fun actionReady() = mutableStateFlow set HomeSceneModel.DefaultState
}