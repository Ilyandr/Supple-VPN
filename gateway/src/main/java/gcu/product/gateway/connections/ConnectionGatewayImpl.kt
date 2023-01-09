package gcu.product.gateway.connections

import gcu.product.gateway.api.ProxyDefaultApi
import gcu.product.gateway.api.ProxyPremiumApi
import gcu.product.gateway.mappers.mapDefaultProxyEntitiesList
import gcu.product.gateway.mappers.mapPremiumProxyEntitiesList

class ConnectionGatewayImpl(
    private val proxyDefaultApi: ProxyDefaultApi,
    private val proxyPremiumApi: ProxyPremiumApi
) : ConnectionGateway {

    override fun getDefaultProxyList() = proxyDefaultApi.getProxyList().map { it.string().mapDefaultProxyEntitiesList() }
    override fun getPremiumProxyList() = proxyPremiumApi.getProxyList().map { it.string().mapPremiumProxyEntitiesList() }
}