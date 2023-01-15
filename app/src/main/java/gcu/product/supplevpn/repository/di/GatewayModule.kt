package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.network.api.VpnDefaultApi
import gcu.product.gateway.network.connections.VpnGateway
import gcu.product.gateway.network.connections.VpnGatewayImpl

@Module
@InstallIn(ViewModelComponent::class)
internal class GatewayModule {

    @Provides
    @Reusable
    fun provideConnectionGateway(api: VpnDefaultApi): VpnGateway = VpnGatewayImpl(api)
}