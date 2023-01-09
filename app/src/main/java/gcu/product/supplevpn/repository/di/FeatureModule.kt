package gcu.product.supplevpn.repository.di

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gcu.product.supplevpn.repository.features.utils.Constants

@Module
@InstallIn(ViewModelComponent::class)
internal class FeatureModule {

    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Provides
    fun providePackageManager(@ApplicationContext context: Context): PackageManager = context.packageManager

    @Provides
    fun provideImageFlagsRequestBuilder(@ApplicationContext context: Context): ImageRequest.Builder =
        ImageRequest.Builder(context)
            .crossfade(false)
            .transformations(RoundedCornersTransformation(32f))
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
}