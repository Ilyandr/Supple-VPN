package gcu.product.gateway.api

import gcu.product.gateway.Constants.PROXY_API_KEY_DEFAULT
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ProxyDefaultApi {

    @GET("get?country_not=RU&api_key=$PROXY_API_KEY_DEFAULT")
    fun getProxyList(): Single<ResponseBody>
}