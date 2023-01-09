package gcu.product.supplevpn.presentation.views.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
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
internal fun HeavyText(
    text: Int, modifier: Modifier = Modifier, fontSize: TextUnit = 20.sp, textAlign: TextAlign = TextAlign.Center
) = Text(
    text = stringResource(id = text),
    modifier = modifier,
    textAlign = textAlign,
    fontStyle = FontStyle(R.font.sf_heavy),
    fontSize = fontSize,
    color = colorResource(id = R.color.white),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)