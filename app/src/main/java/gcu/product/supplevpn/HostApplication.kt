package gcu.product.supplevpn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gcu.product.supplevpn.repository.features.utils.initOneSignal

@HiltAndroidApp
internal class HostApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initOneSignal()
    }
}