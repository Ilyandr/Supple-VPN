package gcu.product.usecase.database.applications

import gcu.product.base.models.apps.ApplicationEntity
import gcu.product.gateway.database.applications.ApplicationsDatabase

class ApplicationsUseCaseImpl(
    private val questDatabase: ApplicationsDatabase
) : ApplicationsUseCase {

    override fun insertApp(data: ApplicationEntity) = questDatabase.requireApplicationDao().insertApp(data)

    override fun updateApp(data: ApplicationEntity) = questDatabase.requireApplicationDao().updateApp(data)

    override fun requireApps() = questDatabase.requireApplicationDao().requireApps()

    override fun removeAll() = questDatabase.requireApplicationDao().removeDatabase()
}