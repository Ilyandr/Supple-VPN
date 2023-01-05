package gcu.product.supplevpn.repository.entities

import android.graphics.drawable.Drawable

data class ApplicationEntity(
    val name: CharSequence?,
    val image: Drawable?,
    val isEnabled: Boolean = false,
    val isLaunched: Boolean = false
)
