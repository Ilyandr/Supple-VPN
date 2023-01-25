package gcu.product.supplevpn.presentation.views.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.requireImage
import gcu.product.supplevpn.repository.features.utils.unitAction

@Composable
internal inline fun InfoConnectionView(
    modifier: Modifier,
    imageRequest: ImageRequest.Builder,
    imageLoader: ImageLoader,
    item: ConnectionEntity?,
    crossinline action: unitAction,
) {
    Row(modifier = modifier.clickable { action.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            imageLoader = imageLoader,
            model = imageRequest.data(item?.requireImageHost()).build(),
            fallback = painterResource(id = R.drawable.ic_unknown_counry),
            placeholder = R.drawable.ic_launcher_foreground.requireImage(),
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .width(42.dp)
                .run {
                    if (item?.countryLong != null) {
                        this.border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(6.dp))
                    } else this
                }
        )
        Spacer(modifier = Modifier.width(if (item == null) 8.dp else 16.dp))
        DefaultText(text = item?.countryLong ?: stringResource(R.string.text_server_none_selected))
        Image(
            modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp),
            painter = R.drawable.ic_right_arrow.requireImage(),
            contentDescription = null
        )
    }
}