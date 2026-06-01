package turmaA.grupoB.LinkStage.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.AdminMainScreen
import turmaA.grupoB.LinkStage.ui.aluno.AlunoMainScreen
import turmaA.grupoB.LinkStage.ui.auth.login.LoginScreen
import turmaA.grupoB.LinkStage.ui.auth.register.RegisterScreen
import turmaA.grupoB.LinkStage.ui.auth.register.RegisterDataScreen
import turmaA.grupoB.LinkStage.ui.auth.register.RegisterSkillsScreen
import turmaA.grupoB.LinkStage.ui.auth.forgotpassword.ForgotPasswordScreen
import turmaA.grupoB.LinkStage.ui.auth.updatepassword.UpdatePasswordScreen
import turmaA.grupoB.LinkStage.ui.introSliders.IntroSlidersScreen
import turmaA.grupoB.LinkStage.ui.splash.SplashScreen
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoMainScreen
import turmaA.grupoB.LinkStage.ui.orientador.OrientadorMainScreen

object Routes {
    const val SPLASH = "splash"
    const val INTRO = "intro"
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val REGISTER_DATA = "auth/register-data/{profile}"
    const val REGISTER_SKILLS = "auth/register-skills"
    const val FORGOT_PASSWORD = "auth/forgot-password"
    const val UPDATE_PASSWORD = "auth/update-password"

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
        enterTransition = {
            fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.INTRO) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.INTRO) {
            IntroSlidersScreen(
                onFinish = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate(Routes.ORIENTADOR_MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onForgotPasswordClick = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onSendResetLinkClick = {
                    // Placeholder for logic handled by colleagues
                    // For UI flow:
                    navController.navigate(Routes.UPDATE_PASSWORD)
                }
            )
        }
        composable(Routes.UPDATE_PASSWORD) {
            UpdatePasswordScreen(
                onBackToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onUpdatePasswordClick = {
                    // Placeholder for logic handled by colleagues
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onContinueClick = { profile ->
                    navController.navigate("auth/register-data/$profile")
                }
            )
        }
        composable(
            route = Routes.REGISTER_DATA,
            arguments = listOf(
                androidx.navigation.navArgument("profile") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val profile = backStackEntry.arguments?.getString("profile") ?: ""
            RegisterDataScreen(
                selectedProfile = profile,
                onBackClick = {
                    navController.popBackStack()
                },
                onContinueClick = { data ->
                    if (profile == "Estudante") {
                        navController.navigate(Routes.REGISTER_SKILLS)
                    } else {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Routes.REGISTER_SKILLS) {
            RegisterSkillsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterClick = { skills ->
                    // Logic to be implemented by colleagues
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ADMIN_MAIN) {
            AdminMainScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ALUNO_MAIN) {
            AlunoMainScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ORIENTADOR_MAIN) {
            OrientadorMainScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.INSTITUICAO_MAIN) { InstituicaoMainScreen() }
    }
}