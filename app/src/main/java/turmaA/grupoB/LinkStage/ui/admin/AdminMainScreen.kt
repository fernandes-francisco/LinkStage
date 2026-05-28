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
import turmaA.grupoB.LinkStage.ui.admin.home.HomeAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.institutions.InstitutionDetailAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.institutions.InstitutionsAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.settings.SettingsAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.students.MentorDetailAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.students.StudentDetailAdminScreen
import turmaA.grupoB.LinkStage.ui.admin.students.StudentsAdminScreen
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

object AdminRoutes {
    const val HOME = "admin_home"
    const val STUDENTS = "admin_students"
    const val STUDENT_DETAIL = "admin_student/{id}"
    const val MENTOR_DETAIL = "admin_mentor/{id}"
    const val INSTITUTIONS = "admin_institutions"
    const val INSTITUTION_DETAIL = "admin_institution/{id}"
    const val SETTINGS = "admin_settings"

    fun studentDetail(id: String) = "admin_student/$id"
    fun mentorDetail(id: String) = "admin_mentor/$id"
    fun institutionDetail(id: String) = "admin_institution/$id"
}

private data class AdminTab(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

private val adminTabs = listOf(
    AdminTab("Início", Icons.Outlined.Home, AdminRoutes.HOME),
    AdminTab("Alunos", Icons.Outlined.People, AdminRoutes.STUDENTS),
    AdminTab("Instituições", Icons.Outlined.AccountBalance, AdminRoutes.INSTITUTIONS),
    AdminTab("Definições", Icons.Outlined.Settings, AdminRoutes.SETTINGS),
)

@Composable
fun AdminMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = adminTabs.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = DarkBlue) {
                    adminTabs.forEach { tab ->
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
            startDestination = AdminRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AdminRoutes.HOME) {
                HomeAdminScreen(navController = navController)
            }
            composable(AdminRoutes.STUDENTS) {
                StudentsAdminScreen(navController = navController)
            }
            composable(
                route = AdminRoutes.STUDENT_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                StudentDetailAdminScreen(
                    studentId = id,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = AdminRoutes.MENTOR_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                MentorDetailAdminScreen(
                    mentorId = id,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(AdminRoutes.INSTITUTIONS) {
                InstitutionsAdminScreen(navController = navController)
            }
            composable(
                route = AdminRoutes.INSTITUTION_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                InstitutionDetailAdminScreen(
                    institutionId = id,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(AdminRoutes.SETTINGS) {
                SettingsAdminScreen(onLogout = onLogout)
            }
        }
    }
}
