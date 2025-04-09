package dev.rao.rockmarket.home.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object NeuLightColors {
    val background = Color(0xFFE0E5EC)
    val shadowDark = Color(0xFFA3B1C6)
    val shadowLight = Color(0xFFFFFFFF)
    val text = Color(0xFF2D3436)
    val accent = Color(0xFF4267B2)
}

object NeuDarkColors {
    val background = Color(0xFF2D3436)
    val shadowDark = Color(0xFF1A1D1E)
    val shadowLight = Color(0xFF404B4D)
    val text = Color(0xFFE0E5EC)
    val accent = Color(0xFF5C89E6)
}

object NeuDimensions {
    val shadowRadius = 8.dp
    val shadowOffset = 4.dp
    val cornerRadius = 16.dp
    val padding = 16.dp
    val margin = 8.dp
}

val LocalNeuDimensions = staticCompositionLocalOf { NeuDimensions }

object NeuColors {
    var background: Color = NeuLightColors.background
        private set
    var shadowDark: Color = NeuLightColors.shadowDark
        private set
    var shadowLight: Color = NeuLightColors.shadowLight
        private set
    var text: Color = NeuLightColors.text
        private set
    var accent: Color = NeuLightColors.accent
        private set

    fun update(darkTheme: Boolean) {
        if (darkTheme) {
            background = NeuDarkColors.background
            shadowDark = NeuDarkColors.shadowDark
            shadowLight = NeuDarkColors.shadowLight
            text = NeuDarkColors.text
            accent = NeuDarkColors.accent
        } else {
            background = NeuLightColors.background
            shadowDark = NeuLightColors.shadowDark
            shadowLight = NeuLightColors.shadowLight
            text = NeuLightColors.text
            accent = NeuLightColors.accent
        }
    }
}

private val LightColorScheme = lightColorScheme(
    primary = NeuLightColors.accent,
    onPrimary = Color.White,
    secondary = Color(0xFF8BC34A),
    onSecondary = Color.White,
    tertiary = Color(0xFFFFC107),
    background = NeuLightColors.background,
    surface = NeuLightColors.background,
    error = Color(0xFFB00020)
)

private val DarkColorScheme = darkColorScheme(
    primary = NeuDarkColors.accent,
    onPrimary = Color.Black,
    secondary = Color(0xFFAED581),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFFD54F),
    background = NeuDarkColors.background,
    surface = NeuDarkColors.background,
    error = Color(0xFFCF6679)
)

@Composable
fun RockMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    NeuColors.update(darkTheme)

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalNeuDimensions provides NeuDimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}