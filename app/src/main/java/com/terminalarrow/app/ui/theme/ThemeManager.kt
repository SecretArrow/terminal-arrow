package com.terminalarrow.app.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import javax.inject.Inject
import javax.inject.Singleton

data class TerminalTheme(
    val name: String,
    val background: Color,
    val foreground: Color,
    val cursor: Color
)

@Singleton
class ThemeManager @Inject constructor() {
    val themes = listOf(
        TerminalTheme("Matrix", Color.Black, Color.Green, Color.White),
        TerminalTheme("Ocean", Color(0xFF001D3D), Color(0xFF90E0EF), Color(0xFFCAF0F8)),
        TerminalTheme("Classic", Color.White, Color.Black, Color.Gray),
        TerminalTheme("Dracula", Color(0xFF282A36), Color(0xFFF8F8F2), Color(0xFFFF79C6))
    )

    var currentTheme by mutableStateOf(themes[0])
    
    var fontSize by mutableStateOf(14)
    var fontFamily: FontFamily by mutableStateOf(FontFamily.Monospace)

    var fontFamilies = mutableStateMapOf<String, FontFamily>(
        "Monospace" to FontFamily.Monospace,
        "Serif" to FontFamily.Serif,
        "SansSerif" to FontFamily.SansSerif,
        "Cursive" to FontFamily.Cursive
    )

    fun addCustomFont(name: String, file: java.io.File) {
        try {
            val customFont = androidx.compose.ui.text.font.Font(file)
            val customFamily = FontFamily(customFont)
            fontFamilies[name] = customFamily
            fontFamily = customFamily
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
