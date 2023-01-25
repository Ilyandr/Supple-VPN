package gcu.product.gateway.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.network.api.PaymentsApi
import gcu.product.gateway.network.api.VpnDefaultApi
import gcu.product.gateway.network.connections.VpnGateway
import gcu.product.gateway.network.connections.VpnGatewayImpl
import gcu.product.gateway.network.payments.PaymentsGateway
import gcu.product.gateway.network.payments.PaymentsGatewayImpl

@Module
@InstallIn(ViewModelComponent::class)
internal class GatewayModule {

    @Provides
    @Reusable
    fun provideConnectionGateway(api: VpnDefaultApi): VpnGateway = VpnGatewayImpl(api)

    @Provides
    @Reusable
    fun providePaymentsGateway(api: PaymentsApi): PaymentsGateway = PaymentsGatewayImpl(api)
}