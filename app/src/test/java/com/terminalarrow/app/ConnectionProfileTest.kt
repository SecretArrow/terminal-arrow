package com.terminalarrow.app

import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.ForwardingRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure data-class regression tests for ConnectionProfile. */
class ConnectionProfileTest {

    @Test
    fun `default values are sensible`() {
        val p = ConnectionProfile(name = "test", host = "h", port = 22, username = "u")
        assertEquals(0, p.id)
        assertEquals(22, p.port)
        assertEquals(null, p.password)
        assertEquals(null, p.keyPath)
        assertEquals("Default", p.group)
        assertEquals(emptyList<ForwardingRule>(), p.forwardingRules)
        assertEquals(false, p.isFavorite)
        assertEquals(0L, p.lastConnectedAt)
    }

    @Test
    fun `copy can flip favorite and update lastConnectedAt`() {
        val p = ConnectionProfile(name = "test", host = "h", port = 22, username = "u")
        val now = 1_700_000_000_000L
        val updated = p.copy(isFavorite = true, lastConnectedAt = now)
        assertEquals(true, updated.isFavorite)
        assertEquals(now, updated.lastConnectedAt)
        // Original is unchanged (immutability).
        assertEquals(false, p.isFavorite)
        assertEquals(0L, p.lastConnectedAt)
        assertNotEquals(p, updated)
    }

    @Test
    fun `equality is structural on all fields`() {
        val a = ConnectionProfile(name = "x", host = "h", port = 22, username = "u")
        val b = ConnectionProfile(name = "x", host = "h", port = 22, username = "u")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `forwarding rule equality holds`() {
        val r1 = ForwardingRule(type = "LOCAL", localPort = 8080, remoteHost = "x", remotePort = 80)
        val r2 = ForwardingRule(type = "LOCAL", localPort = 8080, remoteHost = "x", remotePort = 80)
        assertEquals(r1, r2)
        assertTrue(r1 == r2)
    }
}
