package gcu.product.supplevpn.presentation.views.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gcu.product.base.models.other.LanguageEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.presentation.views.items.LanguageItem
import gcu.product.supplevpn.repository.features.utils.Constants.CHINESE_COUNTRY_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.CHINESE_LANGUAGE_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.ENGLISH_COUNTRY_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.ENGLISH_LANGUAGE_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.RUSSIAN_COUNTRY_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.RUSSIAN_LANGUAGE_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.SPANISH_COUNTRY_CODE
import gcu.product.supplevpn.repository.features.utils.Constants.SPANISH_LANGUAGE_CODE
import gcu.product.supplevpn.repository.source.callback.LanguageCallback

@Composable
internal fun LanguageDropdownMenu(
    modifier: Modifier,
    expandedState: MutableState<Boolean>,
    callback: LanguageCallback
) {
    val languageList by rememberSaveable { mutableStateOf(requireLanguageList()) }

    Box(modifier = modifier) {
        MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp))) {
            DropdownMenu(
                expanded = expandedState.value,
                onDismissRequest = { expandedState.value = false }
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    languageList.forEachIndexed { index, item ->
                        LanguageItem(data = item, callback = callback)
                        if (index != languageList.size - 1) Divider()
                    }
                }
            }
        }
    }
}

internal fun requireLanguageList() = listOf(
    LanguageEntity(
        description = R.string.language_title_ru,
        countryCode = RUSSIAN_COUNTRY_CODE,
        languageCode = RUSSIAN_LANGUAGE_CODE
    ),
    LanguageEntity(
        description = R.string.language_title_gb,
        countryCode = ENGLISH_COUNTRY_CODE,
        languageCode = ENGLISH_LANGUAGE_CODE
    ),
    LanguageEntity(
        description = R.string.language_title_es,
        countryCode = SPANISH_COUNTRY_CODE,
        languageCode = SPANISH_LANGUAGE_CODE
    ),
    LanguageEntity(
        description = R.string.language_title_cn,
        countryCode = CHINESE_COUNTRY_CODE,
        languageCode = CHINESE_LANGUAGE_CODE
    )
)