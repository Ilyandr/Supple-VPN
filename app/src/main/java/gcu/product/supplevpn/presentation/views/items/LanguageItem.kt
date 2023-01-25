package gcu.product.supplevpn.presentation.views.items

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.other.DefaultText
import gcu.product.supplevpn.repository.features.utils.requireImage
import gcu.product.supplevpn.repository.source.callback.LanguageCallback

@Composable
internal fun LanguageItem(
    @StringRes titleId: Int,
    countryCode: String,
    callback: LanguageCallback
) = with(callback) {
    DropdownMenuItem(
        text = {
            DefaultText(
                fontSize = 19.sp,
                text = stringResource(id = titleId),
                textColor = colorResource(if (callback.requireCurrentLanguage() == countryCode) R.color.selectedColor else R.color.blackBlue)
            )
        },
        onClick = {
            saveCurrentLanguage(countryCode)
        },
        leadingIcon = {
            AsyncImage(
                imageLoader = requireImageLoader(),
                model = requireImageRequest().data(ConnectionEntity requireImageHost countryCode).build(),
                fallback = painterResource(id = R.drawable.ic_unknown_counry),
                placeholder = R.drawable.ic_launcher_foreground.requireImage(),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(28.dp)
                    .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(4.dp))
            )
        },
    )
}