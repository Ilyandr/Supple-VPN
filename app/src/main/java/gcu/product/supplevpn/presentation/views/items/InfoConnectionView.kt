package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gcu.product.supplevpn.presentation.views.text.DefaultText
import gcu.product.supplevpn.repository.features.utils.requireImage

@Composable
internal fun InfoConnectionView(modifier: Modifier, image: Int, text: String) =
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(painter = image.requireImage(), contentDescription = "infoConnectionImage")
        Spacer(modifier = Modifier.width(12.dp))
        DefaultText(text = text)
    }