package gcu.product.gateway.database.applications

import androidx.room.Database
import androidx.room.RoomDatabase
import gcu.product.base.models.apps.ApplicationEntity

@Database(entities = [ApplicationEntity::class], exportSchema = false, version = 1)
abstract class ApplicationsDatabase : RoomDatabase() {

    abstract fun requireApplicationDao(): ApplicationsDao
}