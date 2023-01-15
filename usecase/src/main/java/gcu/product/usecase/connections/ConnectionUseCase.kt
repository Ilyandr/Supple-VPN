package gcu.product.usecase.connections

import gcu.product.base.models.proxy.VpnModel
import io.reactivex.rxjava3.core.Single

interface ConnectionUseCase {

    fun getDefaultProxyList(): Single<Sequence<VpnModel>>
}