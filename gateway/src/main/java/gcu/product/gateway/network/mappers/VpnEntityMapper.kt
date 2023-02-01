package gcu.product.gateway.network.mappers

import gcu.product.base.models.proxy.ConnectionEntity
import gcu.product.base.models.proxy.VpnModel
import gcu.product.base.models.proxy.VpnTypeModel
import saschpe.kase64.base64Decoded
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

    private fun stringToServer(line: String): VpnModel =
        line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().run {
            val vpnModel = VpnModel(type = VpnTypeModel.Default).apply {
                hostName = get(HOST_NAME)
                ipAddress = get(IP_ADDRESS)
                score = get(SCORE).toInt()
                ping = get(PING)
                speed = get(SPEED).toLong()
                countryLong = get(COUNTRY_LONG)
                countryShort = get(COUNTRY_SHORT)
                vpnSessions = get(VPN_SESSION).toLong()
                uptime = get(UPTIME).toLong()
                totalUsers = get(TOTAL_USERS).toLong()
                totalTraffic = get(TOTAL_TRAFFIC)
                logType = get(LOG_TYPE)
                operator = get(OPERATOR)
                message = get(MESSAGE)
                ovpnConfigData = get(OVPN_CONFIG_DATA).base64Decoded
            }
            val lines =
                vpnModel.ovpnConfigData!!.split("[\\r\\n]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            vpnModel.port = getPort(lines)
            vpnModel.protocol = getProtocol(lines)
            vpnModel
        }

    fun String.mapToVpnSequence(): Sequence<ConnectionEntity> {

        val vpnEntities: MutableList<ConnectionEntity> = mutableListOf()
        var inputStream: InputStream? = null
        var reader: BufferedReader? = null
        try {
            inputStream = byteInputStream()
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (!line!!.startsWith("*") && !line!!.startsWith("#")) {
                    vpnEntities.add(stringToServer(line!!).mapToConnectionEntity())
                }
            }
        } catch (ignored: IOException) {
        } finally {
            try {
                reader?.close()
                inputStream?.close()
            } catch (ignored: IOException) {
            }
        }
        vpnEntities.removeAll { (it.countryShort != "JP" && it.countryShort != "KR" && it.countryShort != "VN" && it.countryShort != "TH") || it.speed < 200 }
        return vpnEntities.asSequence()
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