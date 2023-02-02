package com.marozzi.desing.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit

@OptIn(ExperimentalTextApi::class)
private fun getStyle(
    style: TextStyle,
    lineHeight: TextUnit?,
    brush: Brush?,
    isNumber: Boolean,
    textSize: TextUnit? = null,
): TextStyle {
    return style
        .let { if (lineHeight != null) it.copy(lineHeight = lineHeight) else it }
        .let { if (textSize != null) it.copy(fontSize = textSize) else it }
        .let { if (brush != null) it.copy(brush = brush) else it }
        .let { if (isNumber) it.copy(fontFeatureSettings = "tnum") else it }
}

@Composable
fun TitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit? = null,
    maxLines: Int = Int.MAX_VALUE,
    brush: Brush? = null,
    textAlign: TextAlign? = null,
    upperCase: Boolean = false,
    isNumber: Boolean = false,
    textSize: TextUnit? = null,
) = Text(
    text = if (upperCase) text.toUpperCase(Locale.current) else text,
    modifier = modifier,
    color = color,
    style = getStyle(MaterialTheme.typography.titleLarge, lineHeight, brush, isNumber, textSize),
    maxLines = maxLines,
    textAlign = textAlign
)

@Composable
fun TitleMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit? = null,
    maxLines: Int = Int.MAX_VALUE,
    brush: Brush? = null,
    textAlign: TextAlign? = null,
    upperCase: Boolean = false,
    isNumber: Boolean = false,
    textSize: TextUnit? = null,
) = Text(
    text = if (upperCase) text.toUpperCase(Locale.current) else text,
    modifier = modifier,
    color = color,
    style = getStyle(MaterialTheme.typography.titleMedium, lineHeight, brush, isNumber, textSize),
    maxLines = maxLines,
    textAlign = textAlign
)

@Composable
fun TitleSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit? = null,
    maxLines: Int = Int.MAX_VALUE,
    brush: Brush? = null,
    textAlign: TextAlign? = null,
    upperCase: Boolean = false,
    isNumber: Boolean = false,
    textSize: TextUnit? = null,
) = Text(
    text = if (upperCase) text.toUpperCase(Locale.current) else text,
    modifier = modifier,
    color = color,
    style = getStyle(MaterialTheme.typography.titleSmall, lineHeight, brush, isNumber, textSize),
    maxLines = maxLines,
    textAlign = textAlign
)

@Composable
fun TextBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit? = null,
    maxLines: Int = Int.MAX_VALUE,
    brush: Brush? = null,
    textAlign: TextAlign? = null,
    upperCase: Boolean = false,
    isNumber: Boolean = false,
    textSize: TextUnit? = null,
) = Text(
    text = if (upperCase) text.toUpperCase(Locale.current) else text,
    modifier = modifier,
    color = color,
    style = getStyle(MaterialTheme.typography.bodyMedium, lineHeight, brush, isNumber, textSize),
    maxLines = maxLines,
    textAlign = textAlign
)

@Composable
fun TextBodyMini(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit? = null,
    maxLines: Int = Int.MAX_VALUE,
    brush: Brush? = null,
    textAlign: TextAlign? = null,
    upperCase: Boolean = false,
    isNumber: Boolean = false,
    textSize: TextUnit? = null,
) = Text(
    text = if (upperCase) text.toUpperCase(Locale.current) else text,
    modifier = modifier,
    color = color,
    style = getStyle(MaterialTheme.typography.bodySmall, lineHeight, brush, isNumber, textSize),
    maxLines = maxLines,
    textAlign = textAlign
)