package gcu.product.gateway.connections

import gcu.product.base.models.proxy.ProxyDefaultEntity
import gcu.product.base.models.proxy.ProxyPremiumEntity
import io.reactivex.rxjava3.core.Single

interface ConnectionGateway {

    fun getDefaultProxyList(): Single<List<ProxyDefaultEntity>>
    fun getPremiumProxyList(): Single<List<ProxyPremiumEntity>>
}