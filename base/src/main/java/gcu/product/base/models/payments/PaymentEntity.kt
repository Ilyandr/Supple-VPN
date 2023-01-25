package gcu.product.base.models.payments

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PaymentEntity @JvmOverloads constructor(
    @field:SerializedName("payment_token") var paymentToken: String? = null,
    @field:SerializedName("amount") var paymentAmount: PaymentAmount? = null,
    @field:SerializedName("capture") var capture: Boolean = false,
    @field:SerializedName("description") var description: String? = null
)
