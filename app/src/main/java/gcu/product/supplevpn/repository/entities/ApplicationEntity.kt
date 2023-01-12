package gcu.product.supplevpn.repository.entities

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class ApplicationEntity(
    val name: CharSequence?,
    val imagePath: String?,
    val isEnabled: Boolean = false,
    val isLaunched: Boolean = false
) : Parcelable
