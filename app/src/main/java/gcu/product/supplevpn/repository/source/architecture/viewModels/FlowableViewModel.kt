package gcu.product.supplevpn.repository.source.architecture.viewModels

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import gcu.product.supplevpn.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeoutException

internal abstract class FlowableViewModel<T> : ViewModel(), InteractionViewModel<T> {

    @SuppressLint("CheckResult")
    override fun <T : Any> Single<T>.simpleRequest(successAction: (T) -> Unit) {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successAction.invoke(it) }, { error ->
                Log.e("err", error.stackTraceToString())
                faultAction(error) })
    }

    override fun handleError(error: Throwable) = when (error) {
        is TimeoutException -> R.string.message_timeout_exception
        is NetworkErrorException -> R.string.message_network_exception
        else -> R.string.message_unknown_exception
    }
}
