package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.network.api.VpnDefaultApi
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_DEFAULT
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
internal class ApiModule {

    @Provides
    fun provideDefaultProxyApi(@Named(RETROFIT_DEFAULT) retrofit: Retrofit): VpnDefaultApi =
        retrofit.create(VpnDefaultApi::class.java)
}