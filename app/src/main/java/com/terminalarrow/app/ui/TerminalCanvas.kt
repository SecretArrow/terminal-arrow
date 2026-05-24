package com.terminalarrow.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb
import com.terminalarrow.app.ui.theme.ThemeManager

@Composable
fun TerminalCanvas(
    output: String,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    val theme = themeManager.currentTheme
    val fontSize = themeManager.fontSize.toFloat()
    
    Canvas(modifier = modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                color = theme.foreground.toArgb()
                textSize = fontSize * density
                typeface = Typeface.MONOSPACE
                isAntiAlias = true
            }
            
            val lines = output.split("\n").takeLast(50) // High-perf rendering limited to view
            var y = paint.textSize
            lines.forEach { line ->
                canvas.nativeCanvas.drawText(line, 10f, y, paint)
                y += paint.fontSpacing
            }
        }
    }
}
