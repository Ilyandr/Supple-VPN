package gcu.product.supplevpn.presentation.views.other

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcu.product.supplevpn.R


@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CustomOutlinedTextField(
    inputType: KeyboardType = KeyboardType.Text,
    @StringRes labelTextId: Int,
    value: String,
    keyboardAction: ImeAction,
    onValueChange: (String) -> Unit
) {
    val isPasswordHidden by remember { mutableStateOf(false) }
    val primaryColor = colorResource(id = R.color.primaryColor)
    val secondColor = Color.White
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = inputType, imeAction = keyboardAction),
        visualTransformation = if (inputType != KeyboardType.Password || isPasswordHidden) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        textStyle = TextStyle.Default.copy(fontSize = 18.sp, fontFamily = FontFamily(Font(resId = R.font.sf_regular))),
        onValueChange = { newValue ->
            onValueChange.invoke(newValue)
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = secondColor,
            textColor = secondColor,
            focusedLabelColor = secondColor,
            unfocusedLabelColor = secondColor,
            cursorColor = secondColor
        ),
        label = { Text(stringResource(labelTextId, "World")) }
    )
}

@Composable
internal fun CustomEditText(
    modifier: Modifier,
    @StringRes labelTextId: Int,
    value: String,
    onValueChange: (String) -> Unit
) {
    colorResource(id = R.color.primaryColor)
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier.background(color = MaterialTheme.colors.background, shape = RectangleShape),
        value = value,
        textStyle = TextStyle.Default.copy(fontSize = 18.sp, fontFamily = FontFamily(Font(resId = R.font.sf_regular))),
        onValueChange = { newValue ->
            onValueChange.invoke(newValue)
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = colorResource(id = R.color.gray)
        ),
        placeholder = { Text(stringResource(labelTextId, "World"), fontSize = 17.sp) }
    )
}

@Composable
internal inline fun BaseButton(
    loadingAction: Boolean, @StringRes textId: Int, crossinline action: () -> Unit
) {
    Button(
        onClick = { if (!loadingAction) action.invoke() },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        colors = ButtonDefaults.buttonColors(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if (!loadingAction)
                Text(
                    color = colorResource(id = R.color.primaryColor),
                    text = stringResource(id = textId),
                    fontSize = 17.sp,
                    fontStyle = FontStyle(R.font.sf_heavy)
                )
            else
                CircularProgressIndicator(
                    Modifier
                        .height(32.dp)
                        .width(32.dp), colorResource(id = R.color.primaryColor)
                )
        }
    }
}