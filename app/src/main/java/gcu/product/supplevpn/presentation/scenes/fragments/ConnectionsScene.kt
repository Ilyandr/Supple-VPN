package gcu.product.supplevpn.presentation.scenes.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import gcu.product.base.models.proxy.VpnModel
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.ConnectionsSceneModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel
import gcu.product.supplevpn.presentation.views.items.BaseHeaderItem
import gcu.product.supplevpn.presentation.views.items.ProxyDefaultItem
import gcu.product.supplevpn.presentation.views.text.DefaultText
import gcu.product.supplevpn.repository.features.utils.requireImage

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ConnectionsScene(navController: NavController, viewModel: ConnectionsSceneViewModel = hiltViewModel()) {

    val viewState = viewModel.stateFlow.collectAsState()
    val proxyListDefault = rememberSaveable { mutableStateOf(listOf<VpnModel>()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }

    when (val value = viewState.value) {
        is ConnectionsSceneModel.ProxyListState -> {
            proxyListDefault.value = value.list
            viewModel.setLoadingAction(false)
        }

        is ConnectionsSceneModel.LoadingState -> loadingState.value = value.isLoading
        else -> Unit
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = R.drawable.ic_home_background.requireImage(),
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

            LazyColumn(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                proxyListDefault.value.groupBy {
                    it.type.requireHeaderStringResByType()
                }.forEach { (section, sectionProxy) ->
                    stickyHeader { BaseHeaderItem(section) }
                    items(sectionProxy) { ProxyDefaultItem(viewModel.imageRequest, it) }
                }
            }
        }
    }
}