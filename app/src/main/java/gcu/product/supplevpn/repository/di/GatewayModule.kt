package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.api.VpnDefaultApi
import gcu.product.gateway.connections.ConnectionGateway
import gcu.product.gateway.connections.ConnectionGatewayImpl

@Module
@InstallIn(ViewModelComponent::class)
internal class GatewayModule {

    @Provides
    @Reusable
    fun provideConnectionGateway(api: VpnDefaultApi): ConnectionGateway = ConnectionGatewayImpl(api)
}