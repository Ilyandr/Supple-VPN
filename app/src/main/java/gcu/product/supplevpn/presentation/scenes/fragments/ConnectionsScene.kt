package gcu.product.supplevpn.presentation.scenes.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.ConnectionsSceneModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel.Companion.SELECTED_VPN_KEY
import gcu.product.supplevpn.presentation.views.items.BaseHeaderItem
import gcu.product.supplevpn.presentation.views.items.ProxyDefaultItem
import gcu.product.supplevpn.presentation.views.other.DefaultText
import gcu.product.supplevpn.repository.features.utils.Utils.showToast
import gcu.product.supplevpn.repository.features.utils.requireImage

// Добавит тени к текству. Исключить лишние сервера, передалать состояние главное кнопки на главной странице (при ошибки подключения / отключения).
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ConnectionsScene(navController: NavController, viewModel: ConnectionsSceneViewModel = hiltViewModel()) {

    val viewState = viewModel.stateFlow.collectAsState()
    val proxyListDefault = remember { mutableStateOf(sequenceOf<ConnectionEntity>()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }
    val refreshState = rememberSwipeRefreshState(isRefreshing = true)

    when (val value = viewState.value) {
        is ConnectionsSceneModel.ProxyListState -> {
            proxyListDefault.value = value.list
            viewModel.setLoadingAction(false)
        }

        is ConnectionsSceneModel.LoadingState -> {
            loadingState.value = value.isLoading
            refreshState.isRefreshing = value.isLoading
        }

        is ConnectionsSceneModel.FaultState -> {
            LocalContext.current showToast value.error
        }

        else -> Unit
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.gradient_start),
                        colorResource(id = R.color.gradient_end),
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            painter = R.drawable.ic_earth.requireImage(),
            contentDescription = "background"
        )

        if (loadingState.value) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color.White)
        }

        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent),
                title = {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable { navController.popBackStack() },
                        painter = R.drawable.ic_back.requireImage(),
                        contentDescription = null
                    )
                    DefaultText(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.description_connection_list)
                    )
                })

            SwipeRefresh(
                state = refreshState,
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        contentColor = colorResource(id = R.color.primaryColor),
                    )
                }, onRefresh = {
                    refreshState.isRefreshing = true
                    viewModel.requireProxyList(false)
                }) {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 12.dp),
                ) {
                    proxyListDefault.value.groupBy { it.countryLong }.forEach { (section, sectionProxy) ->
                        stickyHeader {
                            BaseHeaderItem(section!!)
                            Divider(Modifier.padding(top = 12.dp), color = Color.Transparent)
                        }
                        items(sectionProxy) {
                            val position = sectionProxy.indexOf(it)
                            ProxyDefaultItem(
                                viewModel.requireImageRequest(),
                                viewModel.requireImageLoader(),
                                position + 1,
                                it
                            ) { item ->
                                with(navController) {
                                    previousBackStackEntry?.savedStateHandle?.set(SELECTED_VPN_KEY, item)
                                    popBackStack()
                                }
                            }
                            if (position != sectionProxy.size - 1) {
                                Divider(Modifier.padding(12.dp), color = colorResource(id = R.color.grayLight))
                            } else {
                                Divider(Modifier.padding(bottom = 12.dp), color = Color.Transparent)
                            }
                        }
                    }
                }
            }
        }
    }
}