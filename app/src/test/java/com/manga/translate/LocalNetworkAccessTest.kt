package com.manga.translate

import com.manga.translate.network.LocalNetworkAccess
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalNetworkAccessTest {
    @Test
    fun `detects loopback and private endpoints`() {
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://localhost:11434/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("https://127.0.0.1/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://192.168.1.5:8080/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("https://10.0.0.8/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://172.16.4.2/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://172.31.255.254/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://169.254.10.20/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://nas.local:8080/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://[::1]:8080/v1"))
        assertTrue(LocalNetworkAccess.isLocalEndpoint("http://[fd12:3456::1]/v1"))
    }

    @Test
    fun `ignores public endpoints and malformed input`() {
        assertFalse(LocalNetworkAccess.isLocalEndpoint("https://api.openai.com/v1"))
        assertFalse(LocalNetworkAccess.isLocalEndpoint("https://172.32.0.1/v1"))
        assertFalse(LocalNetworkAccess.isLocalEndpoint("https://192.169.0.1/v1"))
        assertFalse(LocalNetworkAccess.isLocalEndpoint("https://example.com"))
        assertFalse(LocalNetworkAccess.isLocalEndpoint(""))
        assertFalse(LocalNetworkAccess.isLocalEndpoint("not a url"))
    }
}
