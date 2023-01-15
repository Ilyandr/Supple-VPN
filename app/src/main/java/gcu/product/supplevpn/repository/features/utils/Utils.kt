package gcu.product.supplevpn.repository.features.utils

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import de.blinkt.openvpn.OpenVpnApi
import gcu.product.gateway.Constants.VPN_API_LOGIN
import gcu.product.gateway.Constants.VPN_API_PASSWORD
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.usecase.database.applications.ApplicationsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal object Utils {

    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    infix fun PackageManager.isSystemApp(app: ApplicationInfo) = try {
        val targetPkgInfo: PackageInfo = getPackageInfo(
            app.packageName, PackageManager.GET_SIGNATURES
        )
        val sys: PackageInfo = getPackageInfo(
            "android", PackageManager.GET_SIGNATURES
        )
        targetPkgInfo.signatures != null && sys.signatures.first() == targetPkgInfo.signatures.first()
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

    @Suppress("DEPRECATION")
    inline fun PackageManager.requireApplicationList(
        applicationsUseCase: ApplicationsUseCase,
        crossinline afterAction: (List<ApplicationEntity?>) -> Unit
    ) =
        applicationsUseCase.requireApps().singleRequest { localeList ->
            CoroutineScope(Dispatchers.IO).launch {
                with(localeList.associate { it.name to it.isEnabled }) {
                    afterAction.invoke(getInstalledApplications(PackageManager.GET_META_DATA).map { singleInfo ->
                        if (!isSystemApp(singleInfo)) {
                            val label = getApplicationLabel(singleInfo).toString()
                            if (label.length <= 16) {
                                ApplicationEntity(
                                    name = label,
                                    imagePath = singleInfo.packageName,
                                    isEnabled = this[label] ?: false
                                ).apply { applicationsUseCase.insertApp(this).simpleRequest() }
                            } else null
                        } else null
                    })
                }
            }
        }

    internal infix fun Context.launchVpnService(item: ConnectionEntity?) = item?.run {
        OpenVpnApi.startVpn(
            this@launchVpnService,
            ovpnConfigData,
            countryShort,
            VPN_API_LOGIN,
            VPN_API_PASSWORD
        )
    }

    internal fun Context.requireLaunchedApps() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) return
        val mUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val stats =
            mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 10, currentTime)
        if (stats != null) {
            var lastUsedAppTime: Long = 0
            for (usageStats in stats) {
                if (usageStats.lastTimeUsed > lastUsedAppTime) {
                    Log.e("data", usageStats.packageName)
                    lastUsedAppTime = usageStats.lastTimeUsed
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    inline fun <reified T : Any> Single<T>.singleRequest(
        noinline faultAction: ((Throwable) -> Unit)? = null,
        crossinline successAction: (T) -> Unit,
    ) {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successAction.invoke(it) }, { error -> faultAction?.invoke(error) })
    }

    @SuppressLint("CheckResult")
    inline fun Completable.simpleRequest(
        noinline faultAction: ((Throwable) -> Unit)? = null,
        crossinline successAction: unitAction = {}
    ) {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successAction.invoke() }, { error -> faultAction?.invoke(error) })
    }

    internal infix fun Context.showToast(@StringRes messageId: Int) =
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()

    internal inline fun CoroutineScope.actionWithDelay(delay: Long = 500L, crossinline action: unitAction) =
        launch(Dispatchers.IO) { delay(delay); withContext(Dispatchers.Main) { action.invoke() } }
}