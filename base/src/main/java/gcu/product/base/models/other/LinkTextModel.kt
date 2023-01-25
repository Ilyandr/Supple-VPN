package gcu.product.base.models.other


data class LinkTextModel(
    val text: String,
    val tag: String? = null,
    val annotation: String? = null,
    val onClick: ((str: String?) -> Unit)? = null,
)