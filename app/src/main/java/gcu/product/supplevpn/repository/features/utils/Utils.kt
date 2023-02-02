package gcu.product.supplevpn.repository.features.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.content.ContextCompat
import de.blinkt.openvpn.OpenVpnApi
import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.base.models.other.LinkTextModel
import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.gateway.Constants.VPN_API_LOGIN
import gcu.product.gateway.Constants.VPN_API_PASSWORD
import gcu.product.supplevpn.repository.features.utils.Constants.APPLICATION_PATH
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
import java.util.Locale
import java.util.SortedMap
import java.util.TreeMap


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
                                    isEnabled = if (singleInfo.packageName == APPLICATION_PATH) true else this[label]
                                        ?: false
                                ).apply { applicationsUseCase.insertApp(this).simpleRequest() }
                            } else null
                        } else null
                    })
                }
            }
        }

    fun Context.launchVpnService(item: ConnectionEntity?, action: (Intent) -> Unit = {}) = item?.run {
        OpenVpnApi.startVpn(
            this@launchVpnService,
            ovpnConfigData,
            countryShort,
            VPN_API_LOGIN,
            VPN_API_PASSWORD,
            action
        )
    }

    fun Context.requireCurrentTask(): String? {
        var currentApp: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val time = System.currentTimeMillis()
            val appList = (getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager).queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000,
                time
            )
            if (appList != null && appList.isNotEmpty()) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in appList) {
                    mySortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (!mySortedMap.isEmpty()) {
                    currentApp = mySortedMap[mySortedMap.lastKey()]?.packageName
                }
            }
        } else {
            currentApp = (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .runningAppProcesses
                .first()
                .processName
        }
        return currentApp
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

    infix fun Context.showToast(@StringRes messageId: Int) =
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()

    inline fun CoroutineScope.actionWithDelay(delay: Long = 500L, crossinline action: unitAction) =
        launch(Dispatchers.IO) { delay(delay); withContext(Dispatchers.Main) { action.invoke() } }

    infix fun Context.checkGrantedPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    @Suppress("DEPRECATION")
    fun Context.isStatAccessPermissionSet() = try {
        packageManager.getApplicationInfo(packageName, 0).run {
            (getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager).checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, uid, packageName
            ) == AppOpsManager.MODE_ALLOWED
        }
    } catch (e: Exception) {
        false
    }

    infix fun Context.updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    @Composable
    infix fun createAnnotatedString(data: List<LinkTextModel>): AnnotatedString {
        return buildAnnotatedString {
            data.forEach { linkTextData ->
                if (linkTextData.tag != null && linkTextData.annotation != null) {
                    pushStringAnnotation(
                        tag = linkTextData.tag!!,
                        annotation = linkTextData.annotation!!,
                    )
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ) {
                        append(linkTextData.text)
                    }
                    pop()
                } else {
                    append(linkTextData.text)
                }
            }
        }
    }
}