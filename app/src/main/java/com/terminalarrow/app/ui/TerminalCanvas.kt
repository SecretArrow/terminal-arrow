package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.terminalarrow.app.ui.theme.ThemeManager

@Composable
fun TerminalCanvas(
    output: String,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier,
    onResize: (cols: Int, rows: Int) -> Unit = { _, _ -> }
) {
    val theme = themeManager.currentTheme
    val fontSize = themeManager.fontSize.sp
    val density = LocalDensity.current.density
    
    val listState = rememberLazyListState()
    
    LaunchedEffect(output.length) {
        if (output.isNotEmpty()) {
            listState.animateScrollToItem(Int.MAX_VALUE)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().onSizeChanged { size ->
            // Approximate char width for monospace
            val charWidth = (themeManager.fontSize * density * 0.6f).toInt()
            val lineHeight = (themeManager.fontSize * density * 1.2f).toInt()
            if (charWidth > 0 && lineHeight > 0) {
                onResize((size.width / charWidth), (size.height / lineHeight))
            }
        },
        state = listState
    ) {
        val rows = 50 // Limit lines for performance
        val lines = output.split("\n").takeLast(rows)
        items(lines) { line ->
            Text(
                text = parseANSIString(line, theme.foreground, theme.background),
                fontFamily = themeManager.fontFamily,
                fontSize = fontSize,
                color = theme.foreground
            )
        }
    }
}

fun parseANSIString(input: String, defaultForeground: Color, defaultBackground: Color): AnnotatedString {
    return buildAnnotatedString {
        val parts = input.split("[C:")
        if (parts.isEmpty()) return@buildAnnotatedString
        append(parts[0])
        for (i in 1 until parts.size) {
            val endIdx = parts[i].indexOf("]")
            if (endIdx != -1) {
                val code = parts[i].substring(0, endIdx)
                val text = parts[i].substring(endIdx + 1)
                
                var color = defaultForeground
                when (code) {
                    "30" -> color = Color.Black
                    "31" -> color = Color.Red
                    "32" -> color = Color.Green
                    "33" -> color = Color(0xFFFFA500) // Yellow
                    "34" -> color = Color.Blue
                    "35" -> color = Color.Magenta
                    "36" -> color = Color.Cyan
                    "37" -> color = Color.White
                    "0" -> color = defaultForeground
                }
                
                withStyle(style = SpanStyle(color = color)) {
                    append(text)
                }
            } else {
                append("[C:" + parts[i])
            }
        }
    }
}
