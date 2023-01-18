package gcu.product.gateway.network.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Headers
import retrofit2.http.POST

interface VpnDefaultApi {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("iphone/")
    fun getProxyList(): Single<String>
}