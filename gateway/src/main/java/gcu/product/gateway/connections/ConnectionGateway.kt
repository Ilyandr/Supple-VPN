package gcu.product.gateway.connections

import gcu.product.base.models.proxy.ProxyEntity
import io.reactivex.rxjava3.core.Single

interface ConnectionGateway {

    fun getDefaultProxyList(): Single<List<ProxyEntity>>
    fun getPremiumProxyList(): Single<List<ProxyEntity>>
}