package gcu.product.supplevpn.presentation.scenes.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSCustomLoadingEffect
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import com.simform.ssjetpackcomposeprogressbuttonlibrary.utils.six
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.HomeSceneModel
import gcu.product.supplevpn.domain.viewModels.HomeSceneViewModel
import gcu.product.supplevpn.presentation.views.items.ApplicationItem
import gcu.product.supplevpn.presentation.views.items.InfoConnectionView
import gcu.product.supplevpn.repository.entities.ApplicationEntity
import gcu.product.supplevpn.repository.features.utils.requireImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScene(viewModel: HomeSceneViewModel = hiltViewModel()) {

    val viewState = viewModel.stateFlow.collectAsState()
    val appList = rememberSaveable { mutableStateOf(listOf<ApplicationEntity?>()) }
    val loadingState = rememberSaveable { mutableStateOf(true) }

    when (val value = viewState.value) {
        is HomeSceneModel.InitPageState -> {
            appList.value = value.data
            loadingState.value = false
        }
        is HomeSceneModel.LoadingAppState -> loadingState.value = true
        else -> Unit
    }

    ConstraintLayout(requireHomeSceneConstraints()) {
        Image(
            modifier = Modifier.layoutId("backgroundView"),
            contentScale = ContentScale.FillHeight,
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
            image = R.drawable.ic_flag_example,
            text = "Норвегия"
        )
        var submitButtonState by remember { mutableStateOf(SSButtonState.IDLE) }

        Box(modifier = Modifier.layoutId("powerButton")) {

            SSJetPackComposeProgressButton(
                type = SSButtonType.CLOCK,
                width = 120.dp,
                height = 120.dp,
                buttonBorderStroke = BorderStroke(2.dp, Color.LightGray),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(Color.White),
                onClick = { submitButtonState = SSButtonState.LOADING },
                elevation = androidx.compose.material.ButtonDefaults.elevation(6.dp),
                assetColor = colorResource(id = R.color.primaryColor),
                buttonState = submitButtonState,
                successIconPainter = R.drawable.ic_launcher_foreground.requireImage(),
                failureIconPainter = R.drawable.ic_power.requireImage(),
                padding = PaddingValues(six.dp),
                rightImagePainter = R.drawable.ic_power.requireImage(),
                customLoadingEffect = SSCustomLoadingEffect(rotation = false, zoomInOut = true, colorChanger = false)
            )
        }

        if (loadingState.value) {
            CircularProgressIndicator(modifier = Modifier.layoutId("progressView"), color = Color.White)
        }

        Card(
            modifier = Modifier.layoutId("listApp"),
            colors = CardDefaults.cardColors(colorResource(id = R.color.grayTransparent)),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (!loadingState.value) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) { items(appList.value) { ApplicationItem(item = it) } }
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