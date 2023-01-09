package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.api.ProxyDefaultApi
import gcu.product.gateway.api.ProxyPremiumApi
import gcu.product.gateway.connections.ConnectionGateway
import gcu.product.gateway.connections.ConnectionGatewayImpl

@Module
@InstallIn(ViewModelComponent::class)
internal class GatewayModule {

    @Provides
    @Reusable
    fun provideConnectionGateway(api1: ProxyDefaultApi, api2: ProxyPremiumApi): ConnectionGateway =
        ConnectionGatewayImpl(api1, api2)
}