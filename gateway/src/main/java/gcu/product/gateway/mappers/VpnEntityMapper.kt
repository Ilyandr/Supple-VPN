package gcu.product.gateway.mappers

import android.util.Base64
import gcu.product.base.models.proxy.VpnModel
import gcu.product.base.models.proxy.VpnTypeModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object VpnEntityMapper {

    private const val HOST_NAME = 0
    private const val IP_ADDRESS = 1
    private const val SCORE = 2
    private const val PING = 3
    private const val SPEED = 4
    private const val COUNTRY_LONG = 5
    private const val COUNTRY_SHORT = 6
    private const val VPN_SESSION = 7
    private const val UPTIME = 8
    private const val TOTAL_USERS = 9
    private const val TOTAL_TRAFFIC = 10
    private const val LOG_TYPE = 11
    private const val OPERATOR = 12
    private const val MESSAGE = 13
    private const val OVPN_CONFIG_DATA = 14
    private const val PORT_INDEX = 2
    private const val PROTOCOL_INDEX = 1

    private fun stringToServer(line: String): VpnModel {
        val vpn = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vpnModel = VpnModel(type = VpnTypeModel.Default)
        vpnModel.hostName = vpn[HOST_NAME]
        vpnModel.ipAddress = vpn[IP_ADDRESS]
        vpnModel.score = vpn[SCORE].toInt()
        vpnModel.ping = vpn[PING]
        vpnModel.speed = vpn[SPEED].toLong()
        vpnModel.countryLong = vpn[COUNTRY_LONG]
        vpnModel.countryShort = vpn[COUNTRY_SHORT]
        vpnModel.vpnSessions = vpn[VPN_SESSION].toLong()
        vpnModel.uptime = vpn[UPTIME].toLong()
        vpnModel.totalUsers = vpn[TOTAL_USERS].toLong()
        vpnModel.totalTraffic = vpn[TOTAL_TRAFFIC]
        vpnModel.logType = vpn[LOG_TYPE]
        vpnModel.operator = vpn[OPERATOR]
        vpnModel.message = vpn[MESSAGE]
        vpnModel.ovpnConfigData = String(
            Base64.decode(
                vpn[OVPN_CONFIG_DATA], Base64.DEFAULT
            )
        )
        val lines = vpnModel.ovpnConfigData!!.split("[\\r\\n]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        vpnModel.port = getPort(lines)
        vpnModel.protocol = getProtocol(lines)
        return vpnModel
    }

    fun String.mapToVpnEntity(): List<VpnModel> {
        val vpnEntities: MutableList<VpnModel> = mutableListOf()
        var `in`: InputStream? = null
        var reader: BufferedReader? = null
        try {
            `in` = byteInputStream()
            reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (!line!!.startsWith("*") && !line!!.startsWith("#")) {
                    vpnEntities.add(stringToServer(line!!))
                }
            }
        } catch (ignored: IOException) {
        } finally {
            try {
                reader?.close()
                `in`?.close()
            } catch (ignored: IOException) {
            }
        }
        return vpnEntities
    }

    private fun getPort(lines: Array<String>): Int {
        var port = 0
        for (line in lines) {
            if (!line.startsWith("#")) {
                if (line.startsWith("remote")) {
                    port = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[PORT_INDEX].toInt()
                    break
                }
            }
        }
        return port
    }

    private fun getProtocol(lines: Array<String>): String {
        var protocol = ""
        for (line in lines) {
            if (!line.startsWith("#")) {
                if (line.startsWith("proto")) {
                    protocol = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[PROTOCOL_INDEX]
                    break
                }
            }
        }
        return protocol
    }
}