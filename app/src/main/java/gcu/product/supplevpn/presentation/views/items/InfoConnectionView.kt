package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import gcu.product.supplevpn.presentation.views.text.DefaultText
import gcu.product.supplevpn.repository.features.utils.requireImage

@Composable
internal fun InfoConnectionView(
    modifier: Modifier,
    imageRequest: ImageRequest.Builder,
    imageLoader: ImageLoader,
    item: ConnectionEntity?
) =
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            imageLoader = imageLoader,
            model = imageRequest.data(item?.requireImageHost()).build(),
            fallback = painterResource(id = R.drawable.ic_unknown_counry),
            placeholder = R.drawable.ic_launcher_foreground.requireImage(),
            contentDescription = null,
            modifier = if (item != null) {
                Modifier
                    .height(32.dp)
                    .width(48.dp)
                    .border(2.dp, color = Color.LightGray, shape = RoundedCornerShape(16))
            } else Modifier
        )
        Spacer(modifier = Modifier.width(12.dp))
        DefaultText(text = item?.countryLong ?: stringResource(R.string.text_server_none_selected))
    }