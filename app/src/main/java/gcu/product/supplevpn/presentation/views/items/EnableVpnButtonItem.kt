package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSCustomLoadingEffect
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import com.simform.ssjetpackcomposeprogressbuttonlibrary.utils.six
import de.blinkt.openvpn.core.OpenVPNService
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.Utils.actionWithDelay
import gcu.product.supplevpn.repository.features.utils.requireImage
import gcu.product.supplevpn.repository.features.utils.unitAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
internal inline fun EnableVpnButtonItem(
    modifier: Modifier,
    submitButtonState: MutableState<SSButtonState>,
    crossinline launchAction: unitAction
) =
    Box(modifier = modifier) {
        if (submitButtonState.value == SSButtonState.FAILIURE) {
            CoroutineScope(Dispatchers.Main).actionWithDelay(2000L) { submitButtonState.value = SSButtonState.IDLE }
        }
        SSJetPackComposeProgressButton(
            type = SSButtonType.CLOCK,
            width = 120.dp,
            height = 120.dp,
            blinkingIcon = submitButtonState.value == SSButtonState.SUCCESS,
            buttonBorderStroke = BorderStroke(2.dp, Color.LightGray),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                if (submitButtonState.value == SSButtonState.LOADING || submitButtonState.value == SSButtonState.SUCCESS) {
                    OpenVPNService.engineVpnInstance?.stopVPN(false)
                    OpenVPNService.engineVpnInstance = null
                } else {
                    launchAction.invoke()
                }
            },
            elevation = ButtonDefaults.elevation(6.dp),
            assetColor = colorResource(
                id = when (submitButtonState.value) {
                    SSButtonState.IDLE, SSButtonState.LOADING -> R.color.primaryColor
                    SSButtonState.FAILIURE -> R.color.red
                    else -> R.color.green
                }
            ),
            buttonState = submitButtonState.value,
            successIconPainter = R.drawable.ic_connection_complete.requireImage(),
            failureIconPainter = R.drawable.ic_error.requireImage(),
            padding = PaddingValues(six.dp),
            rightImagePainter = if (submitButtonState.value != SSButtonState.SUCCESS) R.drawable.ic_power.requireImage() else R.drawable.ic_indicator_connection.requireImage(),
            customLoadingEffect = SSCustomLoadingEffect(rotation = false, zoomInOut = true, colorChanger = false)
        )
    }