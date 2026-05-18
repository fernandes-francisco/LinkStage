package turmaA.grupoB.LinkStage.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val DarkBlue = Color(0xFF0E1572)
val LightBlue = Color(0xFF30C9CD)
val Red = Color(0xFFFF383C)
val BorderGrey = Color(0xFFD9D9D9)
val DarkGrey = Color(0xFF737373)
val MediumBlue = Color(0xFF326B9B)

val Fade1 = Brush.horizontalGradient(
    colors = listOf(LightBlue, DarkBlue)
)

val Fade2 = Brush.horizontalGradient(
    colorStops = arrayOf(
        0.0f to LightBlue,
        0.27f to DarkBlue,
        1.0f to DarkBlue
    )
)

val Fade3 = Brush.horizontalGradient(
    colorStops = arrayOf(
        0.0f to DarkBlue,
        0.65f to DarkBlue,
        0.89f to MediumBlue,
        1.0f to LightBlue
    )
)