package gcu.product.supplevpn.presentation.views.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.supplevpn.R

@Composable
internal fun DefaultText(
    text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 23.sp,
) = Text(
    text = text,
    modifier = modifier,
    textAlign = TextAlign.Center,
    fontStyle = FontStyle(R.font.sf_medium),
    fontSize = fontSize,
    color = colorResource(id = R.color.white),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

@Composable
internal fun DefaultText(@StringRes text: Int) = Text(
    text = stringResource(id = text),
    textAlign = TextAlign.Center,
    fontStyle = FontStyle(R.font.sf_medium),
    fontSize = 23.sp,
    color = colorResource(id = R.color.white),
    modifier = Modifier
        .padding(top = 6.dp)
        .fillMaxWidth(),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)