package gcu.product.supplevpn.domain.viewModels

import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.product.base.models.payments.PaymentAmount
import gcu.product.base.models.payments.PaymentEntity
import gcu.product.supplevpn.R
import gcu.product.supplevpn.domain.models.PaymentModel
import gcu.product.supplevpn.repository.features.utils.Constants.CURRENCY_APP_RU
import gcu.product.supplevpn.repository.features.utils.Constants.GENERAL_CLIENT_API_ID
import gcu.product.supplevpn.repository.features.utils.Constants.GENERAL_SHOP_API_PASSWORD
import gcu.product.supplevpn.repository.features.utils.Constants.GENERAL_SHOP_ID
import gcu.product.supplevpn.repository.features.utils.Constants.GENERAL_SHOP_PASSWORD
import gcu.product.supplevpn.repository.features.utils.FlowSupport.set
import gcu.product.supplevpn.repository.source.architecture.viewModels.FlowableViewModel
import gcu.product.usecase.network.payments.PaymentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Credentials
import org.json.JSONObject
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import java.math.BigDecimal
import java.util.Currency
import java.util.Random
import javax.inject.Inject

@HiltViewModel
internal class PaymentViewModel @Inject constructor(
    private val paymentsUseCase: PaymentsUseCase
) : FlowableViewModel<PaymentModel>() {

    private val mutableStateFlow: MutableStateFlow<PaymentModel> by lazy { MutableStateFlow(PaymentModel.DefaultState) }
    override val stateFlow by lazy { mutableStateFlow.asStateFlow() }
    private val authHeader by lazy { Credentials.basic(GENERAL_SHOP_ID, GENERAL_SHOP_PASSWORD) }
    private var paymentId: String? = null


    override fun actionReady() = mutableStateFlow.set(PaymentModel.DefaultState)
    override fun setLoadingAction(isLoading: Boolean) = mutableStateFlow.set(PaymentModel.LoadingState(isLoading))
    override fun setFaultAction(error: Throwable) = mutableStateFlow set PaymentModel.FaultState(handleError(error))
    override fun setFaultAction(message: Int) = mutableStateFlow set PaymentModel.FaultState(message)

    fun sendRequestWithToken(token: String, price: Double) {
        setLoadingAction(true)
        paymentsUseCase.sendTokenizeData(
            headerAuth = authHeader,
            uniqueOperationKey = "${Random().nextInt(10000000) + 12}",
            sendPaymentDataEntity = PaymentEntity(
                token, PaymentAmount(
                    BigDecimal.valueOf(price).toString(),
                    Currency.getInstance(CURRENCY_APP_RU).toString()
                ), true
            )
        ).simpleRequest { result ->
            checkedPaymentResult(result)
        }
    }

    fun sendConfirmPayment() {
        paymentId?.let {
            setLoadingAction(true)
            paymentsUseCase.getPaymentResult(
                headerAuth = authHeader,
                singlePaymentID = it
            ).simpleRequest { result ->
                checkedPaymentResult(result)
            }
        } ?: setFaultAction(R.string.message_unknown_exception)
    }

    fun showPaymentDialog(price: Double, description: String, amountType: String) =
        mutableStateFlow.set(
            PaymentModel.OpenPaymentState(
                PaymentParameters(
                    amount = Amount(
                        BigDecimal.valueOf(price),
                        Currency.getInstance(amountType)
                    ),
                    title = description,
                    clientApplicationKey = GENERAL_SHOP_API_PASSWORD,
                    subtitle = "",
                    shopId = GENERAL_SHOP_ID,
                    savePaymentMethod = SavePaymentMethod.OFF,
                    paymentMethodTypes = setOf(
                        PaymentMethodType.BANK_CARD
                    ),
                    authCenterClientId = GENERAL_CLIENT_API_ID
                )
            )
        )

    private fun checkedPaymentResult(allJsonObject: JSONObject?) {
        try {
            allJsonObject?.let {
                if (it.getString("status").equals("succeeded")) {
                    mutableStateFlow.set(PaymentModel.SuccessState)
                } else if (
                    it.getString("status").equals("pending")
                ) {
                    paymentId = allJsonObject.getString("id")
                    mutableStateFlow.set(
                        PaymentModel.RedirectState(
                            allJsonObject
                                .getJSONObject("confirmation")
                                .getString("confirmation_url")
                        )
                    )
                } else {
                    setFaultAction(R.string.message_unknown_exception)
                }
            } ?: setFaultAction(R.string.message_unknown_exception)
        } catch (ex: Exception) {
            setFaultAction(R.string.message_unknown_exception)
        }
    }
}