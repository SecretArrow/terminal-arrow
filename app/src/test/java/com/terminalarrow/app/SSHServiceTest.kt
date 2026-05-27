package com.terminalarrow.app

import android.content.Context
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.service.SSHService
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SSHServiceTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var dao: TerminalDao

    @Test
    fun testServiceInitialization() {
        val service = SSHService(context, dao)
        assertNotNull(service)
    }
}
