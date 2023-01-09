package gcu.product.gateway.api

import gcu.product.gateway.Constants.PROXY_API_KEY_PREMIUM
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ProxyPremiumApi {

    @GET("$PROXY_API_KEY_PREMIUM/getproxy")
    fun getProxyList(): Single<ResponseBody>
}