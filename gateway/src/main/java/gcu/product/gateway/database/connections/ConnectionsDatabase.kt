package gcu.product.gateway.database.connections

import androidx.room.Database
import androidx.room.RoomDatabase
import gcu.product.base.models.proxy.ConnectionEntity

@Database(entities = [ConnectionEntity::class], exportSchema = false, version = 1)
abstract class ConnectionsDatabase : RoomDatabase() {

    abstract fun requireConnectionsDao(): ConnectionsDao
}