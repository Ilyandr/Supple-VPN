package gcu.product.supplevpn.repository.source.callback

interface RateAppCallback {

    fun requireFirstRateAppStatus(): Boolean

    infix fun saveFirstRateAppStatus(value: Boolean)
}