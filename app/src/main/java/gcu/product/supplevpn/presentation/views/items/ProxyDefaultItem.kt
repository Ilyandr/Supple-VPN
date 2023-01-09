package gcu.product.supplevpn.presentation.views.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import gcu.product.base.models.proxy.ProxyDefaultEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.text.DefaultText
import gcu.product.supplevpn.repository.features.utils.requireConnectionSpeedSource
import gcu.product.supplevpn.repository.features.utils.requireConnectionTypeSource
import gcu.product.supplevpn.repository.features.utils.requireImage

@Composable
internal fun ProxyDefaultItem(imageRequestBuilder: ImageRequest.Builder, item: ProxyDefaultEntity) {

    ConstraintLayout(constraintSet = requireConstraintSet(), modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageRequestBuilder.data(item.requireImageHost()).build(),
            placeholder = R.drawable.ic_launcher_foreground.requireImage(),
            contentDescription = null,
            modifier = Modifier
                .layoutId("flagImage")
                .height(64.dp)
                .width(96.dp)
        )
        DefaultText(modifier = Modifier.layoutId("countryDescription"), text = item.country)

        Image(
            modifier = Modifier.layoutId("speedImage"),
            painter = requireConnectionSpeedSource(item.speedMs).requireImage(),
            contentDescription = null
        )

        Row(
            modifier = Modifier.layoutId("typeDescription"),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultText(text = item.connectionType, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = requireConnectionTypeSource(item.connectionType).requireImage(),
                contentDescription = null
            )
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