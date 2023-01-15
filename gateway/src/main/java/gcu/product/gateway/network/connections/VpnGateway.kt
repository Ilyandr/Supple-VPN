package gcu.product.gateway.network.connections

import gcu.product.base.models.proxy.ConnectionEntity
import io.reactivex.rxjava3.core.Single

interface VpnGateway {

    fun getDefaultProxyList(): Single<Sequence<ConnectionEntity>>
}