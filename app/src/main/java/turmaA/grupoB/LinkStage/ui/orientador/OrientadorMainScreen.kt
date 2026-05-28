package turmaA.grupoB.LinkStage.ui.orientador

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
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
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatScreen
import turmaA.grupoB.LinkStage.ui.orientador.students.MentorCheckpointDetailScreen
import turmaA.grupoB.LinkStage.ui.orientador.students.MentorStudentDetailScreen
import turmaA.grupoB.LinkStage.ui.aluno.chat.sampleConversations
import turmaA.grupoB.LinkStage.ui.orientador.chat.ChatOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.internships.MentorInternshipDetailScreen
import turmaA.grupoB.LinkStage.ui.orientador.home.HomeOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.internships.InternshipsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.settings.SettingsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.orientador.students.StudentsOrientadorScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

object OrientadorRoutes {
    const val HOME = "orientador_home"
    const val STUDENTS = "orientador_students"
    const val INTERNSHIPS = "orientador_internships"
    const val MESSAGES = "orientador_messages"
    const val SETTINGS = "orientador_settings"
    const val CHAT = "orientador_chat/{conversationId}"
    const val MENTOR_STUDENT_DETAIL = "mentor_student/{studentId}"
    const val MENTOR_CHECKPOINT_DETAIL = "mentor_checkpoint/{checkpointId}"
    const val MENTOR_INTERNSHIP_DETAIL = "mentor_internship/{internshipId}"

    fun chatRoute(conversationId: String) = "orientador_chat/$conversationId"
    fun mentorStudentDetail(studentId: String) = "mentor_student/$studentId"
    fun mentorCheckpointDetail(checkpointId: String) = "mentor_checkpoint/$checkpointId"
    fun mentorInternshipDetail(internshipId: String) = "mentor_internship/$internshipId"
}

private data class OrientadorTab(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

private val orientadorTabs = listOf(
    OrientadorTab("Início", Icons.Outlined.Home, OrientadorRoutes.HOME),
    OrientadorTab("Alunos", Icons.Outlined.People, OrientadorRoutes.STUDENTS),
    OrientadorTab("Estágios", Icons.Outlined.Work, OrientadorRoutes.INTERNSHIPS),
    OrientadorTab("Mensagens", Icons.AutoMirrored.Outlined.Chat, OrientadorRoutes.MESSAGES),
    OrientadorTab("Definições", Icons.Outlined.Settings, OrientadorRoutes.SETTINGS),
)

@Composable
fun OrientadorMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = orientadorTabs.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = DarkBlue) {
                    orientadorTabs.forEach { tab ->
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
            startDestination = OrientadorRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(OrientadorRoutes.HOME) {
                HomeOrientadorScreen(navController = navController)
            }
            composable(OrientadorRoutes.STUDENTS) {
                StudentsOrientadorScreen(navController = navController)
            }
            composable(OrientadorRoutes.INTERNSHIPS) {
                InternshipsOrientadorScreen(navController = navController)
            }
            composable(OrientadorRoutes.MESSAGES) {
                ChatOrientadorScreen(
                    onOpenChat = { conversationId ->
                        navController.navigate(OrientadorRoutes.chatRoute(conversationId))
                    },
                )
            }
            composable(OrientadorRoutes.SETTINGS) {
                SettingsOrientadorScreen(onLogout = onLogout)
            }
            composable(
                route = OrientadorRoutes.CHAT,
                arguments = listOf(navArgument("conversationId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val conversationId = backStackEntry.arguments?.getString("conversationId") ?: return@composable
                val conversation = sampleConversations.find { it.id == conversationId } ?: return@composable
                ChatScreen(
                    conversation = conversation,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = OrientadorRoutes.MENTOR_STUDENT_DETAIL,
                arguments = listOf(navArgument("studentId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getString("studentId") ?: return@composable
                MentorStudentDetailScreen(
                    studentId = studentId,
                    navController = navController,
                )
            }
            composable(
                route = OrientadorRoutes.MENTOR_CHECKPOINT_DETAIL,
                arguments = listOf(navArgument("checkpointId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val checkpointId = backStackEntry.arguments?.getString("checkpointId") ?: return@composable
                MentorCheckpointDetailScreen(
                    checkpointId = checkpointId,
                    navController = navController,
                )
            }
            composable(
                route = OrientadorRoutes.MENTOR_INTERNSHIP_DETAIL,
                arguments = listOf(navArgument("internshipId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val internshipId = backStackEntry.arguments?.getString("internshipId") ?: return@composable
                MentorInternshipDetailScreen(
                    internshipId = internshipId,
                    navController = navController,
                )
            }
        }
    }
}
