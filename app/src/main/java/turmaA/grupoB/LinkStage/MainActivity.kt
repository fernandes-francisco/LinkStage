package turmaA.grupoB.LinkStage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import turmaA.grupoB.LinkStage.ui.navigation.AppNavigation
import turmaA.grupoB.LinkStage.ui.theme.LinkStageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkStageTheme {
                AppNavigation()
            }
        }
    }
}