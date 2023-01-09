package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.connections.ConnectionGateway
import gcu.product.usecase.connections.ConnectionUseCase
import gcu.product.usecase.connections.ConnectionUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
internal class UseCaseModule {

    @Provides
    @Reusable
    fun provideConnectionUseCase(gateway: ConnectionGateway): ConnectionUseCase = ConnectionUseCaseImpl(gateway)
}