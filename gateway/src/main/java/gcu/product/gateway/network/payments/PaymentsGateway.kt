package gcu.product.gateway.network.payments

import gcu.product.base.models.payments.PaymentEntity
import io.reactivex.rxjava3.core.Single
import org.json.JSONObject

interface PaymentsGateway {

    fun sendTokenizeData(
        headerAuth: String,
        uniqueOperationKey: String,
        sendPaymentDataEntity: PaymentEntity,
    ): Single<JSONObject>

    fun getPaymentResult(headerAuth: String, singlePaymentID: String): Single<JSONObject>
}