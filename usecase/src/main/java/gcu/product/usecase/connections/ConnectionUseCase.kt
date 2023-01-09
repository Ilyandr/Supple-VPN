package gcu.product.usecase.connections

import gcu.product.base.models.proxy.ProxyEntity
import io.reactivex.rxjava3.core.Single

interface ConnectionUseCase {

    fun getDefaultProxyList(): Single<List<ProxyEntity>>
    fun getPremiumProxyList(): Single<List<ProxyEntity>>
}