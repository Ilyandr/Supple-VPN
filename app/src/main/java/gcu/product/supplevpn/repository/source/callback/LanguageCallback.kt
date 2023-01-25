package gcu.product.supplevpn.repository.source.callback

internal interface LanguageCallback: ImageCallback {

    infix fun saveCurrentLanguage(value: String)

    fun requireCurrentLanguage(): String
}