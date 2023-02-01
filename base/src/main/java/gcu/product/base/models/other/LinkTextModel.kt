package gcu.product.base.models.other

import androidx.annotation.Keep


@Keep
data class LinkTextModel(
    val text: String,
    val tag: String? = null,
    val annotation: String? = null,
    val onClick: ((str: String?) -> Unit)? = null,
)