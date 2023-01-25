package gcu.product.supplevpn.presentation.views.items

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.dialogs.SuppleDefaultDialog
import gcu.product.supplevpn.presentation.views.other.HeavyText
import gcu.product.supplevpn.repository.features.utils.booleanAction

@Composable
internal fun TumblerWithTextItem(
    modifier: Modifier,
    @StringRes textId: Int,
    enabledState: MutableState<Boolean>,
    action: booleanAction
) {
    val dialogState = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(true) }

    if (dialogState.value) {
        SuppleDefaultDialog(
            openDialogCustom = openDialog,
            titleTextId = R.string.text_description_auto_vpn,
            descriptionTextId = R.string.text_dialog_clever_mode,
            cancelAction = { dialogState.value = false }
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(21.dp)
                .clickable { dialogState.value = true },
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        HeavyText(text = textId, fontSize = 19.sp)
        Switch(
            modifier = Modifier.padding(top = 4.dp, start = 8.dp),
            checked = enabledState.value,
            colors = SwitchDefaults.colors(Color.White),
            onCheckedChange = { isEnabled ->
                enabledState.value = isEnabled
                action.invoke(isEnabled)
            })
    }
}
