package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.other.TextWithShadow
import gcu.product.supplevpn.repository.features.utils.requireConnectionSpeedSource
import gcu.product.supplevpn.repository.features.utils.requireConnectionTypeSource
import gcu.product.supplevpn.repository.features.utils.requireImage
import gcu.product.supplevpn.repository.features.utils.vpnAction

@Composable
internal inline fun ProxyDefaultItem(
    imageRequest: ImageRequest.Builder,
    imageLoader: ImageLoader,
    position: Int,
    item: ConnectionEntity,
    crossinline callback: vpnAction
) {
    ConstraintLayout(constraintSet = requireConstraintSet(), modifier = Modifier
        .fillMaxWidth()
        .clickable { callback.invoke(item) }) {
        AsyncImage(
            imageLoader = imageLoader,
            model = imageRequest.data(item.requireImageHost()).build(),
            placeholder = R.drawable.ic_launcher_foreground.requireImage(),
            contentDescription = null,
            modifier = Modifier
                .layoutId("flagImage")
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(6.dp))
        )

        TextWithShadow(
            modifier = Modifier.layoutId("countryDescription"),
            text = "${item.countryShort} - №$position",
            style = TextStyle.Default.copy(
                fontSize = 21.sp,
                fontFamily = FontFamily(Font(resId = R.font.sf_regular))
            )
        )

        Row(
            Modifier
                .layoutId("speedImage")
                .clip(RoundedCornerShape(16))
                .background(Color.White)
                .wrapContentSize(Alignment.Center)
        ) {
            Image(
                modifier = Modifier.size(36.dp),
                painter = requireConnectionSpeedSource(item.ping?.toIntOrNull() ?: 999).requireImage(),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier.layoutId("typeDescription"),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextWithShadow(
                modifier = Modifier.layoutId("countryDescription"),
                text = item.protocol!!,
                style = TextStyle.Default.copy(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(resId = R.font.sf_regular))
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = requireConnectionTypeSource(item.protocol!!).requireImage(),
                contentDescription = null
            )
            if (!item.isDefaultServer) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = R.drawable.ic_premium.requireImage(),
                    contentDescription = null
                )
            }
        }
    }
}

internal fun requireConstraintSet() = ConstraintSet {

    with(createRefFor("flagImage")) flagImage@{

        with(createRefFor("countryDescription")) countryDescription@{

            with(createRefFor("typeDescription")) typeDescription@{

                with(createRefFor("speedImage")) speedImage@{

                    constrain(this@flagImage) {
                        width = Dimension.value(96.dp)
                        height = Dimension.value(64.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, 12.dp)
                    }

                    constrain(this@countryDescription) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(this@flagImage.end, 12.dp)
                    }

                    constrain(this@speedImage) {
                        width = Dimension.value(36.dp)
                        height = Dimension.value(36.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, 12.dp)
                    }

                    constrain(this@typeDescription) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(this@speedImage.start)
                        start.linkTo(this@countryDescription.end)
                    }
                }
            }
        }
    }
}