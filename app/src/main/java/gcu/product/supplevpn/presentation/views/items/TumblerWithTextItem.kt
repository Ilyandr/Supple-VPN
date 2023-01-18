package gcu.product.supplevpn.presentation.views.items

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.supplevpn.presentation.views.text.HeavyText
import gcu.product.supplevpn.repository.features.utils.booleanAction

@Composable
internal fun TumblerWithTextItem(
    modifier: Modifier,
    @StringRes textId: Int,
    enabledState: MutableState<Boolean>,
    action: booleanAction
) =
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
