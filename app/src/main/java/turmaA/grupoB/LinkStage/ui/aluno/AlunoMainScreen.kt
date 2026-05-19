package turmaA.grupoB.LinkStage.ui.aluno

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import turmaA.grupoB.LinkStage.ui.aluno.activity.RecentActivityAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.sampleConversations
import turmaA.grupoB.LinkStage.ui.aluno.home.HomeAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.offers.OffersAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.settings.SettingsAlunoScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

object AlunoRoutes {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val ACTIVITY = "activity"
    const val MESSAGES = "messages"
    const val SETTINGS = "settings"
    const val CHAT = "chat/{conversationId}"

    fun chatRoute(conversationId: String) = "chat/$conversationId"
}

private data class AlunoTab(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

private val alunoTabs = listOf(
    AlunoTab("Overview", Icons.Outlined.Home, AlunoRoutes.HOME),
    AlunoTab("Estágios", Icons.Outlined.Work, AlunoRoutes.DISCOVER),
    AlunoTab("Atividade", Icons.Outlined.History, AlunoRoutes.ACTIVITY),
    AlunoTab("Mensagens", Icons.AutoMirrored.Outlined.Chat, AlunoRoutes.MESSAGES),
    AlunoTab("Definições", Icons.Outlined.Settings, AlunoRoutes.SETTINGS),
)

@Composable
fun AlunoMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = alunoTabs.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = DarkBlue) {
                    alunoTabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == tab.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AlunoRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AlunoRoutes.HOME) {
                HomeAlunoScreen(navController = navController, homeViewModel = homeViewModel)
            }
            composable(AlunoRoutes.DISCOVER) {
                OffersAlunoScreen(
                    onOfferClick = { },
                )
            }
            composable(AlunoRoutes.ACTIVITY) {
                RecentActivityAlunoScreen(homeViewModel = homeViewModel)
            }
            composable(AlunoRoutes.MESSAGES) {
                ChatAlunoScreen(
                    onOpenChat = { conversationId ->
                        navController.navigate(AlunoRoutes.chatRoute(conversationId))
                    },
                )
            }
            composable(AlunoRoutes.SETTINGS) {
                SettingsAlunoScreen(onLogout = onLogout)
            }
            composable(
                route = AlunoRoutes.CHAT,
                arguments = listOf(navArgument("conversationId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val conversationId = backStackEntry.arguments?.getString("conversationId") ?: return@composable
                val conversation = sampleConversations.find { it.id == conversationId } ?: return@composable
                ChatScreen(
                    conversation = conversation,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}