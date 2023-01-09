package gcu.product.supplevpn.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.api.ProxyDefaultApi
import gcu.product.gateway.api.ProxyPremiumApi
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_DEFAULT
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_PREMIUM
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
internal class ApiModule {

    @Provides
    fun provideDefaultProxyApi(@Named(RETROFIT_DEFAULT) retrofit: Retrofit): ProxyDefaultApi =
        retrofit.create(ProxyDefaultApi::class.java)

    @Provides
    fun providePremiumProxyApi(@Named(RETROFIT_PREMIUM) retrofit: Retrofit): ProxyPremiumApi =
        retrofit.create(ProxyPremiumApi::class.java)
}