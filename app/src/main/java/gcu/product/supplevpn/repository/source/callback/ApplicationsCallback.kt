package gcu.product.supplevpn.repository.source.callback

import android.content.pm.PackageManager
import gcu.product.base.models.apps.ApplicationEntity

internal interface ApplicationsCallback {

    infix fun changeAppEnabled(item: ApplicationEntity)

    fun requirePackageManager(): PackageManager
}