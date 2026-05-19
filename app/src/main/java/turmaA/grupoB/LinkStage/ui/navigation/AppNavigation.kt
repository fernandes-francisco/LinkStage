package turmaA.grupoB.LinkStage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.AdminMainScreen
import turmaA.grupoB.LinkStage.ui.aluno.AlunoMainScreen
import turmaA.grupoB.LinkStage.ui.auth.login.LoginScreen
import turmaA.grupoB.LinkStage.ui.auth.register.RegisterScreen
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoMainScreen
import turmaA.grupoB.LinkStage.ui.orientador.OrientadorMainScreen

object Routes {
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"

    const val ADMIN_MAIN = "admin"
    const val ALUNO_MAIN = "aluno"
    const val ORIENTADOR_MAIN = "orientador"
    const val INSTITUICAO_MAIN = "instituicao"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate(Routes.ALUNO_MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
            )
        }
        composable(Routes.ADMIN_MAIN) { AdminMainScreen() }
        composable(Routes.ALUNO_MAIN) {
            AlunoMainScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ORIENTADOR_MAIN) { OrientadorMainScreen() }
        composable(Routes.INSTITUICAO_MAIN) { InstituicaoMainScreen() }
    }
}