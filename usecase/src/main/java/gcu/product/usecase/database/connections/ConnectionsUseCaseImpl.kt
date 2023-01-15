package gcu.product.usecase.database.connections

import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.gateway.database.connections.ConnectionsDatabase

class ConnectionsUseCaseImpl(private val database: ConnectionsDatabase) : ConnectionsUseCase {

    override fun insert(data: ConnectionEntity) = database.requireConnectionsDao().insert(data)

    override fun requireConnections() = database.requireConnectionsDao().requireConnections()

    override fun removeDatabase() = database.requireConnectionsDao().removeDatabase()
}