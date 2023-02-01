package gcu.product.supplevpn.presentation.views.items

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.other.TextWithShadow

@Composable
internal inline fun ApplicationItem(
    packageManager: PackageManager,
    item: ApplicationEntity?,
    crossinline changeCallback: (ApplicationEntity) -> Unit
) {
    if (item == null || item.name.isEmpty() || item.imagePath.isNullOrEmpty()) return
    val checkedState = remember { mutableStateOf(item.isEnabled) }

    Row(
        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            modifier = Modifier.size(48.dp),
            bitmap = packageManager.getApplicationIcon(item.imagePath!!).toBitmap().asImageBitmap(),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        TextWithShadow(
            modifier = Modifier.padding(top = 2.dp),
            text = item.name,
            style = TextStyle.Default.copy(
                fontSize = 19.sp,
                fontFamily = FontFamily(Font(resId = R.font.sf_regular))
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Switch(
                checked = checkedState.value,
                colors = SwitchDefaults.colors(Color.White),
                onCheckedChange = { isEnabled ->
                    item.isEnabled = isEnabled
                    changeCallback.invoke(item)
                    checkedState.value = isEnabled
                })
        }
    }
}
