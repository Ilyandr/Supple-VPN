package gcu.product.supplevpn.presentation.views.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.base.models.other.LinkTextModel
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.Utils.createAnnotatedString

@Composable
internal fun DefaultText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 21.sp,
    singleLine: Boolean = true,
    textColor: Color = Color.White
) = Text(
    text = text,
    modifier = modifier,
    textAlign = TextAlign.Center,
    fontStyle = FontStyle(R.font.sf_medium),
    fontSize = fontSize,
    color = textColor,
    maxLines = if (singleLine) 1 else 100,
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

@Composable
internal fun HeavyText(
    text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 20.sp, textAlign: TextAlign = TextAlign.Center
) = Text(
    text = text,
    modifier = modifier,
    textAlign = textAlign,
    fontStyle = FontStyle(R.font.sf_heavy),
    fontSize = fontSize,
    color = colorResource(id = R.color.white),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

@Composable
fun LinkText(linkTextData: List<LinkTextModel>, modifier: Modifier = Modifier) {
    val annotatedString = createAnnotatedString(linkTextData)
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.subtitle1,
        onClick = { offset ->
            linkTextData.forEach { annotatedStringData ->
                if (annotatedStringData.tag != null && annotatedStringData.annotation != null) {
                    annotatedString.getStringAnnotations(
                        tag = annotatedStringData.tag!!,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        annotatedStringData.onClick?.invoke(annotatedStringData.annotation)
                    }
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
fun TextWithShadow(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) =
    Box(modifier = modifier) {
        Text(
            text = text,
            style = style,
            textAlign = textAlign,
            color = Color.DarkGray,
            modifier = Modifier
                .offset(x = 2.dp, y = 2.dp)
                .alpha(0.75f)
        )
        Text(
            text = text,
            textAlign = textAlign,
            style = style,
            color = Color.White,
        )
    }