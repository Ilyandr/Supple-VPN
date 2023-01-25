package gcu.product.base.models.other

import androidx.annotation.StringRes

data class LanguageEntity(val countryCode: String, val languageCode: String, @StringRes val description: Int)