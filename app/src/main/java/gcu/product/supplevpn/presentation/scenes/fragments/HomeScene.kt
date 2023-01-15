package gcu.product.supplevpn.presentation.scenes.fragments

import android.app.Activity
import android.net.VpnService.prepare
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSCustomLoadingEffect
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import com.simform.ssjetpackcomposeprogressbuttonlibrary.utils.six
import gcu.product.base.models.proxy.VpnModel
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.domain.viewModels.ConnectionsSceneViewModel
import gcu.product.supplevpn.domain.viewModels.HomeSceneViewModel
import gcu.product.supplevpn.presentation.views.items.ApplicationItem
import gcu.product.supplevpn.presentation.views.items.InfoConnectionView
import gcu.product.supplevpn.repository.entities.ApplicationEntity
import gcu.product.supplevpn.repository.entities.ConnectionStatus
import gcu.product.supplevpn.repository.features.utils.Constants
import gcu.product.supplevpn.repository.features.utils.Utils.launchVpnService
import gcu.product.supplevpn.repository.features.utils.requireImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScene(navController: NavHostController, viewModel: HomeSceneViewModel = hiltViewModel()) {


    val viewState = viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    var submitButtonState by rememberSaveable { mutableStateOf(SSButtonState.IDLE) }
    val appList = rememberSaveable { mutableStateOf(listOf<ApplicationEntity?>()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }
    val selectedVpnModelState = remember {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<VpnModel>(ConnectionsSceneViewModel.SELECTED_VPN_KEY)
    }
    val vpnContractLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            context launchVpnService selectedVpnModelState?.value
        }
    }

    with(viewModel.requireReceiverData()) {
        (context as ComponentActivity).registerReceiver(first, second)
    }

    when (val value = viewState.value) {
        is HomeSceneModel.InitPageState -> {
            appList.value = value.data
            loadingState.value = false
        }

        is HomeSceneModel.ConnectionStatusState -> {
            submitButtonState = when (value.status) {
                ConnectionStatus.CONNECTED -> SSButtonState.SUCCESS
                else -> SSButtonState.LOADING
            }
        }

        is HomeSceneModel.LoadingAppState -> loadingState.value = true
        else -> Unit
    }

    ConstraintLayout(requireHomeSceneConstraints()) {
        Image(
            modifier = Modifier.layoutId("backgroundView"),
            contentScale = ContentScale.FillBounds,
            painter = R.drawable.ic_home_background.requireImage(),
            contentDescription = "background"
        )
        TopAppBar(
            modifier = Modifier.layoutId("topAppBarView"),
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent),
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        painter = R.drawable.ic_payments.requireImage(),
                        contentDescription = "payments"
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Image(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            painter = R.drawable.ic_list.requireImage(),
                            contentDescription = "list"
                        )
                        Image(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            painter = R.drawable.ic_web.requireImage(),
                            contentDescription = "web"
                        )
                        Image(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            painter = R.drawable.ic_info.requireImage(),
                            contentDescription = "info"
                        )
                    }
                }
            },
        )

        InfoConnectionView(
            modifier = Modifier.layoutId("infoView"),
            imageLoader = viewModel.requireImageLoader(),
            imageRequest = viewModel.requireImageRequest(),
            item = selectedVpnModelState?.value
        )

        Box(modifier = Modifier.layoutId("powerButton")) {

            SSJetPackComposeProgressButton(
                type = SSButtonType.CLOCK,
                width = 120.dp,
                height = 120.dp,
                blinkingIcon = submitButtonState == SSButtonState.SUCCESS,
                buttonBorderStroke = BorderStroke(2.dp, Color.LightGray),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(Color.White),
                onClick = {
                    if (selectedVpnModelState?.value != null) {
                        prepare(context)?.run {
                            vpnContractLauncher.launch(this)
                        } ?: (context launchVpnService selectedVpnModelState.value)
                    } else {
                        navController.navigate(Constants.CONNECTIONS_DESTINATION)
                    }
                },
                elevation = androidx.compose.material.ButtonDefaults.elevation(6.dp),
                assetColor = colorResource(id = if (submitButtonState != SSButtonState.SUCCESS) R.color.primaryColor else R.color.green),
                buttonState = submitButtonState,
                successIconPainter = R.drawable.ic_connection_complete.requireImage(),
                failureIconPainter = R.drawable.ic_power.requireImage(),
                padding = PaddingValues(six.dp),
                rightImagePainter = if (submitButtonState != SSButtonState.SUCCESS) R.drawable.ic_power.requireImage() else R.drawable.ic_indicator_connection.requireImage(),
                customLoadingEffect = SSCustomLoadingEffect(rotation = false, zoomInOut = true, colorChanger = false)
            )
        }

        if (loadingState.value) {
            CircularProgressIndicator(
                modifier = Modifier.layoutId("progressView"), color = Color.White
            )
        }

        Card(
            modifier = Modifier.layoutId("listApp"),
            colors = CardDefaults.cardColors(colorResource(id = R.color.grayTransparent)),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (!loadingState.value) {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) { items(appList.value) { ApplicationItem(viewModel.requirePackageManager(), item = it) } }
            }
        }
    }
}

internal fun requireHomeSceneConstraints() = ConstraintSet {

    with(createRefFor("backgroundView")) backgroundView@{

        with(createRefFor("topAppBarView")) topAppBarView@{

            with(createRefFor("infoView")) infoView@{

                with(createRefFor("powerButton")) powerButton@{

                    with(createRefFor("listApp")) listApp@{

                        with(createRefFor("progressView")) progressView@{

                            constrain(this@backgroundView) {
                                width = Dimension.matchParent
                                height = Dimension.matchParent
                            }

                            constrain(this@topAppBarView) {
                                width = Dimension.matchParent
                                height = Dimension.wrapContent
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }

                            constrain(this@infoView) {
                                width = Dimension.wrapContent
                                height = Dimension.wrapContent
                                top.linkTo(this@topAppBarView.bottom, margin = 64.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }

                            constrain(this@powerButton) {
                                width = Dimension.value(128.dp)
                                height = Dimension.value(128.dp)
                                top.linkTo(this@infoView.bottom, margin = 36.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }

                            constrain(this@listApp) {
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                                top.linkTo(this@powerButton.bottom, margin = 32.dp)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }

                            constrain(this@progressView) {
                                width = Dimension.value(32.dp)
                                height = Dimension.value(32.dp)
                                top.linkTo(this@powerButton.bottom, margin = 32.dp)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        }
                    }
                }
            }
        }
    }
}