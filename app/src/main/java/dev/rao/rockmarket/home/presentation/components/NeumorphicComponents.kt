package dev.rao.rockmarket.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.rao.rockmarket.home.presentation.theme.NeuColors
import dev.rao.rockmarket.home.presentation.theme.NeuDimensions


@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NeuDimensions.cornerRadius,
    elevation: Dp = NeuDimensions.shadowRadius,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(elevation)
            .then(modifier)
    ) {
        val shape = RoundedCornerShape(cornerRadius)
        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    spotColor = NeuColors.shadowDark,
                    ambientColor = NeuColors.shadowLight,
                    clip = true
                )
                .background(
                    color = NeuColors.background,
                    shape = shape
                )
                .padding(NeuDimensions.padding)
        ) {
            content()
        }
    }
}

@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NeuDimensions.cornerRadius,
    elevation: Dp = NeuDimensions.shadowRadius,
    background: Color = NeuColors.background,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(elevation)
            .then(modifier)
    ) {
        val shape = RoundedCornerShape(cornerRadius)
        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    spotColor = NeuColors.shadowDark,
                    ambientColor = NeuColors.shadowLight,
                    clip = true
                )
                .background(
                    color = background,
                    shape = shape
                )
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(NeuDimensions.padding)
        ) {
            content()
        }
    }
}

@Composable
fun NeumorphicSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NeuDimensions.cornerRadius,
    elevation: Dp = NeuDimensions.shadowRadius,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(elevation)
            .then(modifier)
    ) {
        val shape = RoundedCornerShape(cornerRadius)
        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    spotColor = NeuColors.shadowDark,
                    ambientColor = NeuColors.shadowLight,
                    clip = true
                )
                .background(
                    color = NeuColors.background,
                    shape = shape
                )
        ) {
            content()
        }
    }
} 