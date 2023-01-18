package gcu.product.usecase.database.connections

import gcu.product.base.models.proxy.ConnectionEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ConnectionsUseCase {

    fun insert(data: ConnectionEntity): Completable

    fun requireConnections(): Single<List<ConnectionEntity>>

    fun removeDatabase(): Completable

    fun requireModel(key: String): Single<ConnectionEntity>
}