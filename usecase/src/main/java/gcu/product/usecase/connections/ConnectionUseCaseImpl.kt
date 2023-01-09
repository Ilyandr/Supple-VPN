package gcu.product.usecase.connections

import gcu.product.gateway.connections.ConnectionGateway

class ConnectionUseCaseImpl(private val gateway: ConnectionGateway) : ConnectionUseCase {

    override fun getDefaultProxyList() = gateway.getDefaultProxyList()
    override fun getPremiumProxyList() = gateway.getPremiumProxyList()
}