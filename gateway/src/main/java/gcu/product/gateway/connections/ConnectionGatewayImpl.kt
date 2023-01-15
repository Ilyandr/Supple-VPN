package gcu.product.gateway.connections

import gcu.product.base.models.proxy.VpnModel
import gcu.product.gateway.api.VpnDefaultApi
import gcu.product.gateway.mappers.VpnEntityMapper.mapToVpnSequence
import io.reactivex.rxjava3.core.Single

class ConnectionGatewayImpl(private val vpnDefaultApi: VpnDefaultApi) : ConnectionGateway {
    override fun getDefaultProxyList(): Single<Sequence<VpnModel>> =
        vpnDefaultApi.getProxyList().map { response -> response.mapToVpnSequence() }
}