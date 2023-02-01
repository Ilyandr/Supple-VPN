package gcu.product.base.models.payments

import androidx.annotation.Keep

@Keep
data class PaymentAmount(var value: String? = null, var currency: String? = null)
