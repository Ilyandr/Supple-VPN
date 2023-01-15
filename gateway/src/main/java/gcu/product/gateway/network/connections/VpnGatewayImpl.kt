package gcu.product.gateway.network.connections

import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.gateway.network.api.VpnDefaultApi
import gcu.product.gateway.network.mappers.VpnEntityMapper.mapToVpnSequence
import io.reactivex.rxjava3.core.Single

class VpnGatewayImpl(private val vpnDefaultApi: VpnDefaultApi) : VpnGateway {
    override fun getDefaultProxyList(): Single<Sequence<ConnectionEntity>> =
        vpnDefaultApi.getProxyList().map { response -> response.mapToVpnSequence() }
}