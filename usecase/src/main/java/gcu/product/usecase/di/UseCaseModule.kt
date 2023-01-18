package gcu.product.usecase.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gcu.product.gateway.Constants.APPLICATIONS_DATABASE_KEY
import gcu.product.gateway.Constants.CONNECTIONS_DATABASE_KEY
import gcu.product.gateway.database.applications.ApplicationsDatabase
import gcu.product.gateway.database.connections.ConnectionsDatabase
import gcu.product.gateway.network.connections.VpnGateway
import gcu.product.usecase.database.applications.ApplicationsUseCaseImpl
import gcu.product.usecase.database.applications.ApplicationsUseCase
import gcu.product.usecase.database.connections.ConnectionsUseCase
import gcu.product.usecase.database.connections.ConnectionsUseCaseImpl
import gcu.product.usecase.network.connections.VpnUseCase
import gcu.product.usecase.network.connections.VpnUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
class UseCaseModule {

    @Provides
    fun provideApplicationSource(database: ApplicationsDatabase): ApplicationsUseCase =
        ApplicationsUseCaseImpl(database)

    @Provides
    fun provideConnectionsSource(database: ConnectionsDatabase): ConnectionsUseCase =
        ConnectionsUseCaseImpl(database)

    @Provides
    @Reusable
    fun provideConnectionUseCase(gateway: VpnGateway): VpnUseCase = VpnUseCaseImpl(gateway)

    @Provides
    fun provideApplicationsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ApplicationsDatabase::class.java, APPLICATIONS_DATABASE_KEY).build()

    @Provides
    fun provideConnectionsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ConnectionsDatabase::class.java, CONNECTIONS_DATABASE_KEY).build()
}