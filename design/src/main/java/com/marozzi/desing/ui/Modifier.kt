package com.marozzi.desing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.marozzi.desing.theme.SPACE_LOW

@Composable
fun Modifier.roundBorder(
    borderColor: Color = MaterialTheme.colorScheme.inversePrimary,
    borderWidth: Dp = 2.dp,
    borderRadius: Dp = SPACE_LOW,
    borderShape: Shape = RoundedCornerShape(borderRadius),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
): Modifier =
    border(width = borderWidth, color = borderColor, shape = borderShape)
        .padding(all = (borderWidth / 2))
        .background(color = backgroundColor, shape = borderShape)
        .clip(borderShape)
