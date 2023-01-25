package gcu.product.supplevpn.domain.models

import androidx.annotation.StringRes
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters

internal sealed class PaymentModel {

    object DefaultState: PaymentModel()

    object SuccessState: PaymentModel()

    data class LoadingState(val isLoading: Boolean): PaymentModel()

    data class RedirectState(val data: String): PaymentModel()

    data class OpenPaymentState(val data: PaymentParameters): PaymentModel()

    data class FaultState(@StringRes val error: Int) : PaymentModel()
}