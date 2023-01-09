package gcu.product.supplevpn.presentation.scenes.fragments

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gcu.product.base.models.proxy.ProxyDefaultEntity
import gcu.product.supplevpn.domain.models.ConnectionsSceneModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel
import gcu.product.supplevpn.presentation.views.items.ProxyDefaultItem

// Development now
@Composable
internal fun ConnectionsScene(viewModel: ConnectionsSceneViewModel = hiltViewModel()) {

    val viewState = viewModel.stateFlow.collectAsState()
    val proxyList = rememberSaveable { mutableStateOf(listOf<ProxyDefaultEntity>()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }

    when (val value = viewState.value) {
        is ConnectionsSceneModel.DefaultProxyListState -> {
            proxyList.value = value.list
            //loadingState.value = false
        }

        else -> Unit
    }

    Log.e("loaded", proxyList.value.toString())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
    ) { items(proxyList.value.distinctBy { it.address }) { ProxyDefaultItem(viewModel.imageRequest, it) } }

}