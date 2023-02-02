package com.marozzi.desing.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.marozzi.desing.theme.SPACE_HIGH
import com.marozzi.desing.theme.SPACE_LOW
import com.marozzi.desing.theme.SPACE_MEDIUM

@Composable
fun RoundBox(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .8f),
    borderWidth: Dp = 2.dp,
    borderRadius: Dp = SPACE_LOW,
    borderShape: Shape = RoundedCornerShape(borderRadius),
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier.roundBorder(
            borderColor = borderColor,
            borderWidth = borderWidth,
            borderRadius = borderRadius,
            borderShape = borderShape,
            backgroundColor = backgroundColor
        ).then(modifier),
        contentAlignment = contentAlignment,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
    borderWidth: Dp = 2.dp,
    borderShape: Shape = RoundedCornerShape(SPACE_LOW),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.95f else 1f)

    Surface(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer(
                scaleX = sizeScale,
                scaleY = sizeScale
            ),
        color = backgroundColor,
        shape = borderShape,
        border = BorderStroke(width = borderWidth, color = borderColor),
        interactionSource = interactionSource,
        shadowElevation = shadowElevation,
        content = content,
    )
}

@Composable
fun RoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = RoundedCornerShape(SPACE_LOW),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.95f else 1f)

    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer(
            scaleX = sizeScale,
            scaleY = sizeScale
        ),
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Preview(
    widthDp = 300,
    heightDp = 500
)
@Composable
private fun RoundBoxTest() {
    LazyColumn(
        modifier = Modifier.height(1000.dp),
        verticalArrangement = Arrangement.spacedBy(SPACE_HIGH),
        contentPadding = PaddingValues(SPACE_HIGH)
    ) {
        item {
            RoundBox(modifier = Modifier.padding(all = SPACE_MEDIUM)) {
                TitleMedium(text = "x10", color = Color.White)
            }
        }
        item {
            RoundBox(modifier = Modifier.padding(all = SPACE_MEDIUM)) {
                TitleMedium(text = "Loop Band", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}