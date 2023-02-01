package gcu.product.supplevpn.presentation.views.dialogs

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.Constants.APP_STORE_LINK
import gcu.product.supplevpn.repository.features.utils.unitAction
import gcu.product.supplevpn.repository.source.callback.RateAppCallback

@Composable
internal inline fun RateAppDialog(callback: RateAppCallback, crossinline cancelAction: unitAction) {

    val openDialog = remember { mutableStateOf(true) }
    val context = LocalContext.current

    SuppleDefaultDialog(
        openDialogCustom = openDialog,
        titleTextId = R.string.title_rate_dialog,
        descriptionTextId = R.string.description_rate_dialog,
        iconId = R.drawable.ic_stars,
        positiveButton = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APP_STORE_LINK)))
            callback saveFirstRateAppStatus true
        },
        negativeButton = {},
        cancelAction = cancelAction
    )
}