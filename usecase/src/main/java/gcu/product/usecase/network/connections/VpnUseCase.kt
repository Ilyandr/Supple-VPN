package gcu.product.usecase.network.connections

import gcu.product.base.models.proxy.ConnectionEntity
import io.reactivex.rxjava3.core.Single

interface VpnUseCase {

    fun getDefaultProxyList(): Single<Sequence<ConnectionEntity>>
}