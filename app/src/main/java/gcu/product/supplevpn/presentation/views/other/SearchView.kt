package gcu.product.supplevpn.presentation.views.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.supplevpn.R
import gcu.product.supplevpn.repository.features.utils.requireImage
import gcu.product.supplevpn.repository.features.utils.stringAction
import gcu.product.supplevpn.repository.features.utils.unitAction
import kotlinx.coroutines.delay

@Composable
internal inline fun SearchView(
    value: String,
    crossinline cancelAction: unitAction,
    crossinline searchAction: stringAction
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.clickable { cancelAction.invoke() },
            painter = R.drawable.ic_back.requireImage(),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
            onValueChange = { newText ->
                searchAction.invoke(newText)
            },
            textStyle = TextStyle.Default.copy(
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(resId = R.font.sf_regular))
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = CircleShape)
                        .border(
                            width = 2.dp,
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Favorite icon",
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    if (value.isEmpty()) {
                        DefaultText(
                            text = stringResource(R.string.search_apps_hint),
                            fontSize = 16.sp,
                            textColor = Color.DarkGray
                        )
                    }
                    innerTextField()
                }
            }
        )

        LaunchedEffect("focus") {
            delay(300)
            focusRequester.requestFocus()
        }
    }
}