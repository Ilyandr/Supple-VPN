package gcu.product.gateway.database.applications

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import gcu.product.base.models.Constants.APPLICATIONS_ENTITY_NAME
import gcu.product.base.models.apps.ApplicationEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ApplicationsDao {

    @Insert(entity = ApplicationEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertApp(data: ApplicationEntity): Completable

    @Update(entity = ApplicationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateApp(data: ApplicationEntity): Completable

    @Query("SELECT * FROM $APPLICATIONS_ENTITY_NAME")
    fun requireApps(): Single<List<ApplicationEntity>>

    @Transaction
    @Query("DELETE FROM $APPLICATIONS_ENTITY_NAME")
    fun removeDatabase(): Completable
}