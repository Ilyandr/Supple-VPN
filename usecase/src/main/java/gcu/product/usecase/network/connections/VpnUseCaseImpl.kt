package gcu.product.usecase.network.connections

import gcu.product.gateway.network.connections.VpnGateway

class VpnUseCaseImpl(private val gateway: VpnGateway) : VpnUseCase {

    override fun getDefaultProxyList() = gateway.getDefaultProxyList()
}