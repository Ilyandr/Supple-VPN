package gcu.product.gateway.database.connections

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import gcu.product.base.models.Constants.CONNECTIONS_ENTITY_NAME
import gcu.product.base.models.proxy.ConnectionEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ConnectionsDao {

    @Insert(entity = ConnectionEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ConnectionEntity): Completable

    @Query("SELECT * FROM $CONNECTIONS_ENTITY_NAME")
    fun requireConnections(): Single<List<ConnectionEntity>>

    @Transaction
    @Query("DELETE FROM $CONNECTIONS_ENTITY_NAME")
    fun removeDatabase(): Completable
}