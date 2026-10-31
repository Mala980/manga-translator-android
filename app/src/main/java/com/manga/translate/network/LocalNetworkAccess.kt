package com.manga.translate.network

import java.net.URI

/**
 * Detects endpoints that Android classifies as local network traffic.
 *
 * Android 17 (API 37) requires `ACCESS_LOCAL_NETWORK` for these hosts even when the
 * URL uses HTTPS, so the settings entry must not be limited to `http://` URLs.
 */
object LocalNetworkAccess {
    fun isLocalEndpoint(endpoint: String): Boolean {
        val host = runCatching { URI(endpoint.trim()).host }.getOrNull()
            ?.trim()
            ?.removeSurrounding("[", "]")
            ?.lowercase()
            ?: return false
        if (host.isEmpty()) return false
        if (host == "localhost" || host.endsWith(".localhost") || host.endsWith(".local")) {
            return true
        }
        return if (host.contains(':')) isLocalIpv6(host) else isLocalIpv4(host)
    }

    private fun isLocalIpv4(host: String): Boolean {
        val octets = host.split('.').map { it.toIntOrNull() ?: return false }
        if (octets.size != 4 || octets.any { it !in 0..255 }) return false
        return octets[0] == 0 ||
            octets[0] == 10 ||
            octets[0] == 127 ||
            (octets[0] == 172 && octets[1] in 16..31) ||
            (octets[0] == 192 && octets[1] == 168) ||
            (octets[0] == 169 && octets[1] == 254)
    }

    private fun isLocalIpv6(host: String): Boolean {
        return host == "::1" ||
            host == "0:0:0:0:0:0:0:1" ||
            host.startsWith("fe80:") ||
            host.startsWith("fc") ||
            host.startsWith("fd") ||
            host.startsWith("::ffff:127.") ||
            host.startsWith("::ffff:10.") ||
            host.startsWith("::ffff:192.168.")
    }
}
