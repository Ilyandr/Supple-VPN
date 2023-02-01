package gcu.product.base.models.other

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
data class LanguageEntity(val countryCode: String, val languageCode: String, @StringRes val description: Int)