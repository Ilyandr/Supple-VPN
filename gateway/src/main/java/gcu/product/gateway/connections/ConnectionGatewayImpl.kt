package gcu.product.gateway.connections

import gcu.product.gateway.api.VpnDefaultApi
import gcu.product.gateway.mappers.VpnEntityMapper.mapToVpnEntity

class ConnectionGatewayImpl(private val vpnDefaultApi: VpnDefaultApi) : ConnectionGateway {
    override fun getDefaultProxyList() = vpnDefaultApi.getProxyList().map { response -> response.mapToVpnEntity() }
}