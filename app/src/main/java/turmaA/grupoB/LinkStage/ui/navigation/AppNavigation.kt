package turmaA.grupoB.LinkStage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.AdminMainScreen
import turmaA.grupoB.LinkStage.ui.aluno.AlunoMainScreen
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
    startDestination: String = Routes.ALUNO_MAIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.ADMIN_MAIN) { AdminMainScreen() }
        composable(Routes.ALUNO_MAIN) { AlunoMainScreen() }
        composable(Routes.ORIENTADOR_MAIN) { OrientadorMainScreen() }
        composable(Routes.INSTITUICAO_MAIN) { InstituicaoMainScreen() }
    }
}