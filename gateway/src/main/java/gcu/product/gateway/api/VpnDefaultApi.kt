package gcu.product.gateway.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.POST

interface VpnDefaultApi {

    @POST("iphone/")
    fun getProxyList(): Single<String>
}