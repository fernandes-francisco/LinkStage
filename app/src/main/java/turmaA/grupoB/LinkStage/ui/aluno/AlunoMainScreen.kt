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
import androidx.compose.runtime.remember
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
import turmaA.grupoB.LinkStage.ui.auth.updatepassword.UpdatePasswordScreen
import turmaA.grupoB.LinkStage.ui.aluno.activity.RecentActivityAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.sampleConversations
import turmaA.grupoB.LinkStage.ui.aluno.home.HomeAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.notifications.NotificationsAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityDetailAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.apply.ApplyScreen
import turmaA.grupoB.LinkStage.ui.aluno.apply.ApplySuccessScreen
import turmaA.grupoB.LinkStage.ui.aluno.apply.EditCvScreen
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferDetailAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.offers.OffersAlunoScreen
import turmaA.grupoB.LinkStage.ui.aluno.settings.SettingsAlunoScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.ApplyViewModel
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

object AlunoRoutes {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val ACTIVITY = "activity"
    const val MESSAGES = "messages"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val CHAT = "chat/{conversationId}"
    const val OFFER_DETAIL = "offer_detail/{offerId}"
    const val APPLY = "apply/{offerId}"
    const val EDIT_CV = "edit_cv"
    const val APPLY_SUCCESS = "apply_success/{offerId}"
    const val ACTIVITY_DETAIL = "activity_detail/{checkpointId}"
    const val UPDATE_PASSWORD = "update_password"

    fun chatRoute(conversationId: String) = "chat/$conversationId"
    fun offerDetailRoute(offerId: String) = "offer_detail/$offerId"
    fun applyRoute(offerId: String) = "apply/$offerId"
    fun applySuccessRoute(offerId: String) = "apply_success/$offerId"
    fun activityDetailRoute(checkpointId: String) = "activity_detail/$checkpointId"
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
                    onOfferClick = { offerId ->
                        navController.navigate(AlunoRoutes.offerDetailRoute(offerId))
                    },
                )
            }
            composable(
                route = AlunoRoutes.OFFER_DETAIL,
                arguments = listOf(navArgument("offerId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val offerId = backStackEntry.arguments?.getString("offerId") ?: return@composable
                OfferDetailAlunoScreen(
                    offerId = offerId,
                    onBack = { navController.popBackStack() },
                    onApply = { id -> navController.navigate(AlunoRoutes.applyRoute(id)) },
                )
            }
            composable(
                route = AlunoRoutes.APPLY,
                arguments = listOf(navArgument("offerId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val offerId = backStackEntry.arguments?.getString("offerId") ?: return@composable
                val applyViewModel: ApplyViewModel = viewModel(backStackEntry)
                ApplyScreen(
                    offerId = offerId,
                    viewModel = applyViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToEditCv = { navController.navigate(AlunoRoutes.EDIT_CV) },
                    onSubmitSuccess = {
                        navController.navigate(AlunoRoutes.applySuccessRoute(offerId)) {
                            popUpTo(AlunoRoutes.applyRoute(offerId)) { inclusive = true }
                        }
                    },
                )
            }
            composable(AlunoRoutes.EDIT_CV) {
                val applyEntry = remember(it) {
                    navController.getBackStackEntry(AlunoRoutes.APPLY)
                }
                val applyViewModel: ApplyViewModel = viewModel(applyEntry)
                EditCvScreen(
                    viewModel = applyViewModel,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = AlunoRoutes.APPLY_SUCCESS,
                arguments = listOf(navArgument("offerId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val offerId = backStackEntry.arguments?.getString("offerId") ?: return@composable
                ApplySuccessScreen(
                    offerId = offerId,
                    onNavigateBack = {
                        navController.navigate(AlunoRoutes.DISCOVER) {
                            popUpTo(AlunoRoutes.HOME) { inclusive = false }
                        }
                    },
                )
            }
            composable(AlunoRoutes.ACTIVITY) {
                RecentActivityAlunoScreen(
                    homeViewModel = homeViewModel,
                    onActivityClick = { checkpointId ->
                        navController.navigate(AlunoRoutes.activityDetailRoute(checkpointId))
                    },
                )
            }
            composable(
                route = AlunoRoutes.ACTIVITY_DETAIL,
                arguments = listOf(navArgument("checkpointId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val checkpointId = backStackEntry.arguments?.getString("checkpointId") ?: return@composable
                ActivityDetailAlunoScreen(
                    checkpointId = checkpointId,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(AlunoRoutes.MESSAGES) {
                ChatAlunoScreen(
                    onOpenChat = { conversationId ->
                        navController.navigate(AlunoRoutes.chatRoute(conversationId))
                    },
                )
            }
            composable(AlunoRoutes.SETTINGS) {
                SettingsAlunoScreen(
                    onLogout = onLogout,
                    onNotificationsClick = {
                        navController.navigate(AlunoRoutes.NOTIFICATIONS)
                    }
                )
            }
            composable(AlunoRoutes.NOTIFICATIONS) {
                NotificationsAlunoScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AlunoRoutes.UPDATE_PASSWORD) {
                UpdatePasswordScreen(
                    onUpdatePasswordClick = { /* Lógica de update */ },
                    onBackToLogin = { navController.popBackStack() }
                )
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