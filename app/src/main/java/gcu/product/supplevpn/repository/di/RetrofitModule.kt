package gcu.product.supplevpn.repository.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.Constants.PROXY_API_HOST_DEFAULT
import gcu.product.gateway.Constants.PROXY_API_HOST_PREMIUM
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_DEFAULT
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_PREMIUM
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class RetrofitModule {

    @Provides
    fun provideOkHttpClient() = OkHttpClient()

    @Provides
    fun provideGsonConfig(): Gson = GsonBuilder().setLenient().create()

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Provides
    fun provideConverterFactory(config: Gson): Converter.Factory = GsonConverterFactory.create(config)

    @Provides
    @Named(RETROFIT_DEFAULT)
    fun provideDefaultRetrofit(
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(PROXY_API_HOST_DEFAULT)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Named(RETROFIT_PREMIUM)
    fun providePremiumRetrofit(
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(PROXY_API_HOST_PREMIUM)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
}