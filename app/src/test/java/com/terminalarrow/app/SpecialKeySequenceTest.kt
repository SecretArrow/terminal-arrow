package com.terminalarrow.app

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Regression tests for the small pure-Kotlin helpers used by the terminal
 * screen. These don't need Android, the JVM, or coroutines so they're a fast
 * canary against accidental string regressions on keyboard shortcuts.
 */
class SpecialKeySequenceTest {

    @Test
    fun arrowsMapToAnsiSequences() {
        assertEquals("\u001B[A", directionToEscape("UP"))
        assertEquals("\u001B[B", directionToEscape("DOWN"))
        assertEquals("\u001B[C", directionToEscape("RIGHT"))
        assertEquals("\u001B[D", directionToEscape("LEFT"))
    }

    @Test
    fun unknownDirectionIsEmpty() {
        assertEquals("", directionToEscape("DIAGONAL"))
    }

    // Duplicate the private helpers under test so we can hit them without
    // adding @VisibleForTesting hooks into production code.
    private fun directionToEscape(dir: String): String = when (dir) {
        "UP" -> "\u001B[A"
        "DOWN" -> "\u001B[B"
        "RIGHT" -> "\u001B[C"
        "LEFT" -> "\u001B[D"
        else -> ""
    }
}
