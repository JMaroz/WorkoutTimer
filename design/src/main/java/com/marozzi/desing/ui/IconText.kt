package com.marozzi.desing.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.marozzi.desing.theme.SPACE_LOW

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    @DrawableRes iconResId: Int,
    iconColorFilter: Color = Color.Unspecified,
    iconSize: TextUnit = 23.sp,
    iconVerticalAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.TextTop,
    spaceSize: Dp = SPACE_LOW,
) {
    Text(
        text = buildAnnotatedString {
            appendInlineContent("icon", "icon")
            appendInlineContent("space", "space")
            append(text)
        },
        inlineContent = mapOf(
            Pair("icon", InlineTextContent(
                Placeholder(
                    width = iconSize,
                    height = iconSize,
                    placeholderVerticalAlign = iconVerticalAlign
                )
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = iconColorFilter
                )
            }
            ),
            Pair(
                "space", InlineTextContent(
                    Placeholder(
                        width = spaceSize.value.sp,
                        height = spaceSize.value.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {

                }
            ),
        ),
        color = color,
        style = style,
        modifier = modifier
    )
}
