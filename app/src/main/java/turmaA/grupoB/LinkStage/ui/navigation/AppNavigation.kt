package turmaA.grupoB.LinkStage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.home.HomeAdminScreen
import turmaA.grupoB.LinkStage.ui.aluno.home.HomeAlunoScreen
import turmaA.grupoB.LinkStage.ui.instituicao.home.HomeInstituicaoScreen
import turmaA.grupoB.LinkStage.ui.orientador.home.HomeOrientadorScreen
import turmaA.grupoB.LinkStage.ui.auth.login.LoginScreen
import turmaA.grupoB.LinkStage.ui.auth.register.RegisterSelectionScreen
import turmaA.grupoB.LinkStage.ui.splash.SplashScreen
import turmaA.grupoB.LinkStage.ui.introSliders.IntroSlidersScreen

object Routes {
    const val SPLASH = "splash"
    const val INTRO_SLIDERS = "intro_sliders"
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
    startDestination: String = Routes.SPLASH,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.INTRO_SLIDERS) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.INTRO_SLIDERS) {
            IntroSlidersScreen(
                onFinish = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.INTRO_SLIDERS) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate(Routes.ALUNO_MAIN)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterSelectionScreen(
                onContinueClick = { _ ->
                    // For now, after profile selection, go to Login
                    navController.navigate(Routes.LOGIN)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ADMIN_MAIN) { HomeAdminScreen() }
        composable(Routes.ALUNO_MAIN) { HomeAlunoScreen() }
        composable(Routes.ORIENTADOR_MAIN) { HomeOrientadorScreen() }
        composable(Routes.INSTITUICAO_MAIN) { HomeInstituicaoScreen() }
    }
}
