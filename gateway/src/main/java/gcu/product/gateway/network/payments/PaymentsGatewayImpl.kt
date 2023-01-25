package gcu.product.gateway.network.payments

import gcu.product.base.models.payments.PaymentEntity
import gcu.product.gateway.network.api.PaymentsApi
import org.json.JSONObject

class PaymentsGatewayImpl(private val api: PaymentsApi) : PaymentsGateway {

    override fun sendTokenizeData(
        headerAuth: String,
        uniqueOperationKey: String,
        sendPaymentDataEntity: PaymentEntity
    ) = api.sendTokenizeData(headerAuth, uniqueOperationKey, sendPaymentDataEntity)
        .map { JSONObject(it.string()) }

    override fun getPaymentResult(headerAuth: String, singlePaymentID: String) =
        api.getPaymentResult(headerAuth, singlePaymentID).map { JSONObject(it.string()) }
}