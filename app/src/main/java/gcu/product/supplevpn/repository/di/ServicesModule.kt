package gcu.product.supplevpn.repository.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gcu.product.supplevpn.repository.features.utils.Constants

@Module
@InstallIn(ViewModelComponent::class)
internal class ServicesModule {

    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Provides
    fun providePackageManager(@ApplicationContext context: Context) = context.packageManager
}