package gcu.product.gateway.mappers

import gcu.product.base.models.proxy.ProxyDefaultEntity
import gcu.product.base.models.proxy.ProxyPremiumEntity

fun String.mapDefaultProxyEntitiesList(): List<ProxyDefaultEntity> {
    var mutableString = this
    return mutableListOf<ProxyDefaultEntity>().apply {
        while (mutableString.contains("name")) {
            add(
                ProxyDefaultEntity(
                    address = mutableString.split("name")[1].toDefaultFormat(),
                    country = mutableString.split("country")[1].toDefaultFormat(),
                    connectionType = mutableString.split("type")[1].toDefaultFormat(),
                    speedMs = mutableString.split("speed")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toLong() },
                    dateChecked = mutableString.split("upd")[1].toDefaultFormat(),
                    addressType = mutableString.split("work")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toInt() }
                )
            )
            mutableString = mutableString.replaceFirst("name", "")
                .replaceFirst("country", "")
                .replaceFirst("type", "")
                .replaceFirst("speed", "")
                .replaceFirst("upd", "")
                .replaceFirst("work", "")
        }
    }
}

internal fun String.mapPremiumProxyEntitiesList(): List<ProxyPremiumEntity> {
    var mutableString = this
    return mutableListOf<ProxyPremiumEntity>().apply {
        while (mutableString.contains("id")) {
            add(
                ProxyPremiumEntity(
                    id = mutableString.split("id")[1].toDefaultFormat().toInt(),
                    version = mutableString.split("version")[1].toDefaultFormat(),
                    ip = mutableString.split("ip")[1].toDefaultFormat(),
                    host = mutableString.split("host")[1].toDefaultFormat(),
                    port = mutableString.split("port")[1].toDefaultFormat(),
                    user = mutableString.split("user")[1].toDefaultFormat(),
                    pass = mutableString.split("pass")[1].toDefaultFormat(),
                    type = mutableString.split("type")[1].toDefaultFormat(),
                    country = mutableString.split("country")[1].toDefaultFormat(),
                    date = mutableString.split("date")[1].toDefaultFormat(),
                    dateEnd = mutableString.split("date_end")[1].toDefaultFormat(),
                    unixtime = mutableString.split("unixtime")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toLong() },
                    unixtimeEnd = mutableString.split("unixtime_end")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toLong() },
                    descr = mutableString.split("descr")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toInt() },
                    active = mutableString.split("active")[1].toDefaultFormat()
                        .run { if (this.isEmpty()) 0 else this.toInt() },
                )
            )
            mutableString = mutableString.replaceFirst("id", "")
                .replaceFirst("version", "")
                .replaceFirst("ip", "")
                .replaceFirst("host", "")
                .replaceFirst("country", "")
                .replaceFirst("user", "")
                .replaceFirst("pass", "")
                .replaceFirst("type", "")
                .replaceFirst("date", "")
                .replaceFirst("date_end", "")
                .replaceFirst("unixtime", "")
                .replaceFirst("unixtime_end", "")
                .replaceFirst("descr", "")
                .replaceFirst("active", "")
        }
    }
}

internal fun String.toDefaultFormat() = split(",").first()
    .replace(":\"", "")
    .replace("\":", "")
    .replace("\"", "")
    .replace("}", "")
