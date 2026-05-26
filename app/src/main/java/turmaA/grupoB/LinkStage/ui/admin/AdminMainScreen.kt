package turmaA.grupoB.LinkStage.ui.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
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
import turmaA.grupoB.LinkStage.ui.admin.home.HomeAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.institutions.InstitutionsAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.settings.SettingsAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.students.StudentsAdminScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

private data class AdminTab(
    val title: String,
    val icon: ImageVector,
)

private val adminTabs = listOf(
    AdminTab("Overview", Icons.Outlined.Home),
    AdminTab("Alunos", Icons.Outlined.People),
    AdminTab("Instituições", Icons.Outlined.AccountBalance),
    AdminTab("Definições", Icons.Outlined.Settings),
)

@Composable
fun AdminMainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = DarkBlue) {
                adminTabs.forEachIndexed { index, tab ->
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
            0 -> HomeAdminScreen(modifier = modifier)
            1 -> StudentsAdminScreen(modifier = modifier)
            2 -> InstitutionsAdminScreen(modifier = modifier)
            3 -> SettingsAdminScreen(modifier = modifier)
        }
    }
}