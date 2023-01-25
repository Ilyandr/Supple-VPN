package gcu.product.usecase.network.payments

import gcu.product.base.models.payments.PaymentEntity
import gcu.product.gateway.network.payments.PaymentsGateway

class PaymentsUseCaseImpl(private val gateway: PaymentsGateway) : PaymentsUseCase {

    override fun sendTokenizeData(
        headerAuth: String,
        uniqueOperationKey: String,
        sendPaymentDataEntity: PaymentEntity
    ) = gateway.sendTokenizeData(headerAuth, uniqueOperationKey, sendPaymentDataEntity)

    override fun getPaymentResult(headerAuth: String, singlePaymentID: String) =
        gateway.getPaymentResult(headerAuth, singlePaymentID)
}