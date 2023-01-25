package gcu.product.supplevpn.repository.source.callback

import coil.ImageLoader
import coil.request.ImageRequest

internal interface ImageCallback {

    fun requireImageRequest(): ImageRequest.Builder

    fun requireImageLoader(): ImageLoader
}