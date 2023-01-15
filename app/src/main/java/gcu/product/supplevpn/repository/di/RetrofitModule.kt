package gcu.product.supplevpn.repository.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gcu.product.gateway.Constants.VPN_API_HOST
import gcu.product.supplevpn.repository.features.utils.Constants.DEFAULT_REST_TIMEOUT
import gcu.product.supplevpn.repository.features.utils.Constants.RETROFIT_DEFAULT
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class RetrofitModule {

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_REST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_REST_TIMEOUT, TimeUnit.SECONDS)
        .callTimeout(DEFAULT_REST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_REST_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideGsonConfig(): Gson = GsonBuilder().setLenient().create()

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Provides
    @Named("gsonFactory")
    fun provideGsonConverterFactory(config: Gson): Converter.Factory = GsonConverterFactory.create(config)

    @Provides
    @Named("scalarsFactory")
    fun provideScalarsConverterFactory(): Converter.Factory = ScalarsConverterFactory.create()

    @Provides
    @Named(RETROFIT_DEFAULT)
    fun provideDefaultRetrofit(
        callAdapterFactory: CallAdapter.Factory,
        @Named("scalarsFactory") converterScalarsFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(VPN_API_HOST)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterScalarsFactory)
        .build()
}