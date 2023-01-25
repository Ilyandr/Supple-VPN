package gcu.product.supplevpn.presentation.views.dialogs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.unitAction

@Composable
internal inline fun SuppleDefaultDialog(
    openDialogCustom: MutableState<Boolean>? = null,
    @StringRes titleTextId: Int? = null,
    @StringRes descriptionTextId: Int,
    @DrawableRes iconId: Int? = null,
    noinline positiveButton: unitAction? = null,
    noinline negativeButton: unitAction? = null,
    crossinline cancelAction: unitAction
) {
    Dialog(onDismissRequest = {
        openDialogCustom?.value = false
        cancelAction.invoke()
    }) {
        val primaryColor = colorResource(id = R.color.primaryColor)

        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.background(Color.White)) {
                iconId?.run {
                    Image(
                        painter = painterResource(id = this),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(70.dp)
                            .fillMaxWidth(),
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    titleTextId?.run {
                        Text(
                            text = stringResource(id = this),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle(R.font.sf_heavy),
                            fontSize = 24.sp,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = stringResource(id = descriptionTextId),
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle(R.font.sf_regular),
                        fontSize = 19.sp,
                        modifier = Modifier
                            .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                    )
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            openDialogCustom?.value = false
                            negativeButton?.invoke()
                            cancelAction.invoke()
                        }) {
                        Text(
                            stringResource(R.string.dialog_back),
                            fontStyle = FontStyle(R.font.sf_heavy),
                            fontSize = 18.sp,
                            color = primaryColor,
                            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                        )
                    }

                    positiveButton?.run {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                positiveButton.invoke()
                                cancelAction.invoke()
                            }) {
                            Text(
                                stringResource(R.string.dialog_next),
                                fontStyle = FontStyle(R.font.sf_heavy),
                                fontSize = 18.sp,
                                color = primaryColor,
                                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}