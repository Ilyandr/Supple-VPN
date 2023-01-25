package gcu.product.gateway.network.api

import gcu.product.base.models.payments.PaymentEntity
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentsApi {

    @POST(value = "payments")
    @Headers("Content-Type: application/json")
    fun sendTokenizeData(
        @Header(value = "Authorization") headerAuth: String,
        @Header(value = "Idempotence-Key") uniqueOperationKey: String,
        @Body sendPaymentDataEntity: PaymentEntity,
    ): Single<ResponseBody>

    @GET("payments/{id}")
    @Headers("Content-Type: application/json")
    fun getPaymentResult(
        @Header(value = "Authorization") headerAuth: String,
        @Path(value = "id") singlePaymentID: String,
    ): Single<ResponseBody>
}