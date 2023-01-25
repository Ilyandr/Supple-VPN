package gcu.product.supplevpn.presentation.views.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.unitAction

@Composable
internal inline fun LoadingDialog(
    openDialogCustom: MutableState<Boolean>? = null,
    crossinline cancelAction: unitAction
) {
    Dialog(onDismissRequest = { openDialogCustom?.value = false; cancelAction.invoke() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.wrapContentSize(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.primaryColor),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}