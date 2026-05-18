package turmaA.grupoB.LinkStage.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// LinkStage palette
val BlueDark = Color(0xFF0E1572)
val BlueLight = Color(0xFF30C9CD)
val BlueMid = Color(0xFF326B9B)
val RedAccent = Color(0xFFFF383C)
val GrayBorder = Color(0xFFD9D9D9)
val GrayDark = Color(0xFF737373)
val BackgroundLight = Color(0xFFF8F9FA)

// Fade 1 — 50/50 (decorative, headers)
val Fade1 = Brush.horizontalGradient(
    0f to BlueLight,
    1f to BlueDark,
)

// Fade 2 — teal → dark blue at 27% (confirmation buttons)
val Fade2 = Brush.horizontalGradient(
    0f to BlueLight,
    0.27f to BlueDark,
    1f to BlueDark,
)

// Fade 3 — dark blue → mid → teal (cards)
val Fade3 = Brush.horizontalGradient(
    0f to BlueDark,
    0.65f to BlueDark,
    0.89f to BlueMid,
    1f to BlueLight,
)