package gcu.product.gateway.connections

import gcu.product.base.models.proxy.VpnModel
import io.reactivex.rxjava3.core.Single

interface ConnectionGateway {

    fun getDefaultProxyList(): Single<List<VpnModel>>
}