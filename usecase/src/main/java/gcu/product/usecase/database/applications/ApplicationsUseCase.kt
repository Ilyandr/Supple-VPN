package gcu.product.usecase.database.applications

import gcu.product.base.models.apps.ApplicationEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ApplicationsUseCase {

    fun insertApp(data: ApplicationEntity): Completable

    fun updateApp(data: ApplicationEntity): Completable

    fun requireApps(): Single<List<ApplicationEntity>>

    fun removeAll(): Completable
}