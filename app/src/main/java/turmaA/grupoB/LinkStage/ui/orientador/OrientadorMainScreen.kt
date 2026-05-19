package turmaA.grupoB.LinkStage.ui.orientador

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import turmaA.grupoB.LinkStage.ui.orientador.chat.ChatOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.home.HomeOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.internships.InternshipsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.settings.SettingsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.students.StudentsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

private data class OrientadorTab(
    val title: String,
    val icon: ImageVector,
)

private val orientadorTabs = listOf(
    OrientadorTab("Overview", Icons.Outlined.Home),
    OrientadorTab("Alunos", Icons.Outlined.People),
    OrientadorTab("Estágios", Icons.Outlined.Work),
    OrientadorTab("Mensagens", Icons.Outlined.Chat),
    OrientadorTab("Definições", Icons.Outlined.Settings),
)

@Composable
fun OrientadorMainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = DarkBlue) {
                orientadorTabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LightBlue,
                            selectedTextColor = LightBlue,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                            indicatorColor = DarkBlue,
                        ),
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            0 -> HomeOrientadorScreen(modifier = modifier)
            1 -> StudentsOrientadorScreen(modifier = modifier)
            2 -> InternshipsOrientadorScreen(modifier = modifier)
            3 -> ChatOrientadorScreen(modifier = modifier)
            4 -> SettingsOrientadorScreen(modifier = modifier)
        }
    }
}