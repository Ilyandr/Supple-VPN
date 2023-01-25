package gcu.product.supplevpn.presentation.scenes.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.VpnService.prepare
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel
import gcu.product.supplevpn.domain.viewModels.HomeSceneViewModel
import gcu.product.supplevpn.presentation.views.other.InfoConnectionView
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.base.models.apps.ConnectionStatus
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.presentation.other.AutoVpnService.Companion.connectAutoVpnService
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.BACKGROUND_VIEW
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.INFO_VIEW
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.LANGUAGE_DROP_VIEW
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.LIST_APPS
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.POWER_BUTTON
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.PROGRESS_VIEW
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.TOP_APP_BAR_VIEW
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.TUMBLER_APPS
import gcu.product.supplevpn.presentation.scenes.constrains.HomeSceneConstraints.requireHomeSceneConstraints
import gcu.product.supplevpn.presentation.views.items.EnableVpnButtonItem
import gcu.product.supplevpn.presentation.views.items.TumblerWithTextItem
import gcu.product.supplevpn.presentation.views.other.ApplicationsList
import gcu.product.supplevpn.presentation.views.other.LanguageDropdownMenu
import gcu.product.supplevpn.repository.features.utils.Constants
import gcu.product.supplevpn.repository.features.utils.Constants.BROWSER_DESTINATION
import gcu.product.supplevpn.repository.features.utils.Utils.actionWithDelay
import gcu.product.supplevpn.repository.features.utils.Utils.checkGrantedPermission
import gcu.product.supplevpn.repository.features.utils.Utils.isStatAccessPermissionSet
import gcu.product.supplevpn.repository.features.utils.Utils.launchVpnService
import gcu.product.supplevpn.repository.features.utils.Utils.updateLocale
import gcu.product.supplevpn.repository.features.utils.requireImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@SuppressLint("UnspecifiedRegisterReceiverFlag", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScene(navController: NavHostController, viewModel: HomeSceneViewModel = hiltViewModel()) {

    val selectedVpnModelState = remember {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<ConnectionEntity>(ConnectionsSceneViewModel.SELECTED_VPN_KEY)
    }
    val viewState = viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val submitButtonState = rememberSaveable { mutableStateOf(SSButtonState.IDLE) }
    val browserState = rememberSaveable { mutableStateOf(false) }
    val appList = rememberSaveable { mutableStateOf(listOf<ApplicationEntity?>()) }
    val autoVpnEnabledState = remember { mutableStateOf(viewModel.requireAutoVpnStatus()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }
    val openLanguageState = remember { mutableStateOf(false) }
    val selectedConnectionState = mutableStateOf(false)

    val dialogLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (autoVpnEnabledState.value && submitButtonState.value == SSButtonState.IDLE) {
                viewModel saveAutoVpnStatus true
                if (selectedVpnModelState?.value != null) {
                    context connectAutoVpnService autoVpnEnabledState.value
                } else {
                    navController.navigate(Constants.CONNECTIONS_DESTINATION)
                }
            } else {
                context.launchVpnService(selectedVpnModelState?.value)
            }
        } else {
            viewModel saveAutoVpnStatus false
            autoVpnEnabledState.value = false
        }
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            prepare(context)?.run { dialogLauncher.launch(this) } ?: run {
                viewModel saveAutoVpnStatus true
                if (selectedVpnModelState?.value != null) {
                    context connectAutoVpnService autoVpnEnabledState.value
                } else {
                    selectedConnectionState.value = true
                }
            }
        } else {
            viewModel saveAutoVpnStatus false
            autoVpnEnabledState.value = false
        }
    }

    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (context.isStatAccessPermissionSet()) {
            if (context.checkGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                prepare(context)?.run { dialogLauncher.launch(this) } ?: run {
                    viewModel saveAutoVpnStatus true
                    if (selectedVpnModelState?.value != null) {
                        context connectAutoVpnService autoVpnEnabledState.value
                    } else {
                        selectedConnectionState.value = true
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            viewModel saveAutoVpnStatus false
            autoVpnEnabledState.value = false
        }
    }

    if (selectedVpnModelState?.value != null) {
        viewModel saveCurrentConnection selectedVpnModelState.value!!
    }
    with(viewModel.requireReceiverData()) {
        context.registerReceiver(first, second)
    }
    with(context.isStatAccessPermissionSet()) {
        if (autoVpnEnabledState.value && !this) {
            activityLauncher.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else if (autoVpnEnabledState.value && this && !context.checkGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                SideEffect {
                    permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else if (this && context.checkGrantedPermission(Manifest.permission.POST_NOTIFICATIONS) && !loadingState.value) {
            SideEffect {
                prepare(context)?.run { dialogLauncher.launch(this) }
                    ?: run {
                        if (selectedVpnModelState?.value != null) {
                            context connectAutoVpnService autoVpnEnabledState.value
                        }
                    }
            }
        }
    }

    when (val value = viewState.value) {
        is HomeSceneModel.InitPageState -> {
            appList.value = value.data
            value.currentModel?.run { if (selectedVpnModelState?.value == null) selectedVpnModelState?.value = this }
            loadingState.value = false
        }

        is HomeSceneModel.ConnectionStatusState -> {
            submitButtonState.value = when (value.status) {
                ConnectionStatus.CONNECTED -> SSButtonState.SUCCESS
                ConnectionStatus.FAULT -> SSButtonState.FAILIURE
                ConnectionStatus.LOADING -> SSButtonState.LOADING
                else -> {
                    browserState.value = false
                    SSButtonState.IDLE
                }
            }
        }

        is HomeSceneModel.UpdateLanguageState -> {
            context.applicationContext updateLocale value.country
            context updateLocale value.country
            if (autoVpnEnabledState.value) {
                context connectAutoVpnService false
                CoroutineScope(Dispatchers.Main).actionWithDelay {
                    context connectAutoVpnService true
                }
            }
        }

        is HomeSceneModel.LoadingAppState -> {
            loadingState.value = true
        }

        else -> Unit
    }

    ConstraintLayout(requireHomeSceneConstraints()) {
        Image(
            modifier = Modifier.layoutId(BACKGROUND_VIEW),
            contentScale = ContentScale.FillBounds,
            painter = R.drawable.ic_home_background.requireImage(),
            contentDescription = null
        )
        TopAppBar(
            modifier = Modifier.layoutId(TOP_APP_BAR_VIEW),
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent),
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable { navController.navigate(Constants.PAYMENT_DESTINATION) },
                        painter = R.drawable.ic_payments.requireImage(),
                        contentDescription = "payments"
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable {
                                    selectedConnectionState.value = true
                                },
                            painter = R.drawable.ic_list.requireImage(),
                            contentDescription = "list"
                        )
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable {
                                    if (submitButtonState.value == SSButtonState.SUCCESS) {
                                        navController.navigate(BROWSER_DESTINATION)
                                    } else {
                                        if (selectedVpnModelState?.value != null) {
                                            if (!context.checkGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                    permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                                }
                                            } else {
                                                prepare(context)?.run { activityLauncher.launch(this) }
                                                    ?: context.launchVpnService(selectedVpnModelState.value)
                                            }
                                        } else {
                                            selectedConnectionState.value = true
                                        }
                                        browserState.value = true
                                    }
                                },
                            painter = R.drawable.ic_web.requireImage(),
                            contentDescription = "web"
                        )
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable { openLanguageState.value = true },
                            painter = R.drawable.ic_language.requireImage(),
                            contentDescription = "info"
                        )
                    }
                }
            },
        )

        if (openLanguageState.value) {
            LanguageDropdownMenu(
                modifier = Modifier.layoutId(LANGUAGE_DROP_VIEW),
                expandedState = openLanguageState,
                callback = viewModel
            )
        }

        InfoConnectionView(
            modifier = Modifier.layoutId(INFO_VIEW),
            imageLoader = viewModel.requireImageLoader(),
            imageRequest = viewModel.requireImageRequest(),
            item = selectedVpnModelState?.value
        ) {
            selectedConnectionState.value = true
        }

        EnableVpnButtonItem(modifier = Modifier.layoutId(POWER_BUTTON), submitButtonState = submitButtonState) {
            if (selectedVpnModelState?.value != null) {
                if (!context.checkGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    prepare(context)?.run { activityLauncher.launch(this) }
                        ?: context.launchVpnService(selectedVpnModelState.value)
                }
            } else {
                selectedConnectionState.value = true
            }
        }

        if (loadingState.value) {
            CircularProgressIndicator(
                modifier = Modifier.layoutId(PROGRESS_VIEW), color = Color.White
            )
        }

        if (!loadingState.value) {
            TumblerWithTextItem(
                modifier = Modifier.layoutId(TUMBLER_APPS),
                textId = R.string.text_description_auto_vpn,
                enabledState = autoVpnEnabledState
            ) { toggle ->
                autoVpnEnabledState.value = toggle
                viewModel saveAutoVpnStatus toggle
            }
        }

        ApplicationsList(
            modifier = Modifier.layoutId(LIST_APPS),
            loadingState = loadingState,
            appList = appList,
            source = viewModel
        )

        if (selectedConnectionState.value) {
            navController.navigate(Constants.CONNECTIONS_DESTINATION)
        }
        if (browserState.value && submitButtonState.value == SSButtonState.SUCCESS) {
            navController.navigate(BROWSER_DESTINATION)
            browserState.value = false
        }
    }
}