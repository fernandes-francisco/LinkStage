package turmaA.grupoB.LinkStage.ui.instituicao

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
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
import turmaA.grupoB.LinkStage.ui.instituicao.activity.RecentActivityInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.instituicao.chat.ChatInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.instituicao.home.HomeInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.instituicao.offers.OffersInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.instituicao.settings.SettingsInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

private data class InstituicaoTab(
    val title: String,
    val icon: ImageVector,
)

private val instituicaoTabs = listOf(
    InstituicaoTab("Overview", Icons.Outlined.Home),
    InstituicaoTab("Estágios", Icons.Outlined.Work),
    InstituicaoTab("Atividade", Icons.Outlined.History),
    InstituicaoTab("Mensagens", Icons.Outlined.Chat),
    InstituicaoTab("Definições", Icons.Outlined.Settings),
)

@Composable
fun InstituicaoMainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = DarkBlue) {
                instituicaoTabs.forEachIndexed { index, tab ->
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
            0 -> HomeInstituicaoScreen(modifier = modifier)
            1 -> OffersInstituicaoScreen(modifier = modifier)
            2 -> RecentActivityInstituicaoScreen(modifier = modifier)
            3 -> ChatInstituicaoScreen(modifier = modifier)
            4 -> SettingsInstituicaoScreen(modifier = modifier)
        }
    }
}