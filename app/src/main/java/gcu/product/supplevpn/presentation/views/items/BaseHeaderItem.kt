package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.text.HeavyText

@Composable
internal fun BaseHeaderItem(textId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.primaryColor))
    ) {
        HeavyText(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = textId,
            textAlign = TextAlign.Start
        )
    }
}