package gcu.product.base.models.apps

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import gcu.product.base.models.Constants.APPLICATIONS_ENTITY_NAME

@Entity(APPLICATIONS_ENTITY_NAME)
@kotlinx.parcelize.Parcelize
data class ApplicationEntity(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "image_path") val imagePath: String?,
    @ColumnInfo(name = "is_enabled") var isEnabled: Boolean = false,
) : Parcelable
