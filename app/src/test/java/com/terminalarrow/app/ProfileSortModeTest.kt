package com.terminalarrow.app

import com.terminalarrow.app.ui.ProfileSortMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileSortModeTest {

    @Test
    fun `has three values in expected order`() {
        val values = ProfileSortMode.values()
        assertEquals(3, values.size)
        assertEquals(ProfileSortMode.Recent, values[0])
        assertEquals(ProfileSortMode.Alphabetical, values[1])
        assertEquals(ProfileSortMode.Favorites, values[2])
    }

    @Test
    fun `valueOf round trip works for each variant`() {
        ProfileSortMode.values().forEach { mode ->
            assertEquals(mode, ProfileSortMode.valueOf(mode.name))
        }
    }
}
