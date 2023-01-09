package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import gcu.product.supplevpn.presentation.views.text.DefaultText
import gcu.product.supplevpn.repository.entities.ApplicationEntity

@Composable
internal fun ApplicationItem(item: ApplicationEntity?) {

    if (item == null || item.name.isNullOrEmpty()) return

    Row(
        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        item.image?.toBitmap()?.asImageBitmap()
            ?.run { Image(modifier = Modifier.size(48.dp), bitmap = this, contentDescription = null) }
        Spacer(modifier = Modifier.width(12.dp))
        DefaultText(text = item.name.toString())
        Spacer(modifier = Modifier.width(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Switch(checked = item.isEnabled, onCheckedChange = {})
        }
    }
}
