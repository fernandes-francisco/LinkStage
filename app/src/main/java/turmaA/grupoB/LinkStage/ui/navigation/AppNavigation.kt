package turmaA.grupoB.LinkStage.ui.navigation

/**
 * Definição das rotas de navegação da aplicação.
 * Cada perfil de utilizador tem as suas próprias rotas.
 */
object Routes {
    // Auth
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"

    // Admin
    const val ADMIN_HOME = "admin/home"
    const val ADMIN_OFFERS = "admin/offers"
    const val ADMIN_OFFER_DETAIL = "admin/offers/{offerId}"
    const val ADMIN_RECENT_ACTIVITY = "admin/activity"
    const val ADMIN_CHAT = "admin/chat"
    const val ADMIN_SETTINGS = "admin/settings"

    // Aluno
    const val ALUNO_HOME = "aluno/home"
    const val ALUNO_OFFERS = "aluno/offers"
    const val ALUNO_OFFER_DETAIL = "aluno/offers/{offerId}"
    const val ALUNO_RECENT_ACTIVITY = "aluno/activity"
    const val ALUNO_CHAT = "aluno/chat"
    const val ALUNO_SETTINGS = "aluno/settings"

    // Orientador
    const val ORIENTADOR_HOME = "orientador/home"
    const val ORIENTADOR_OFFERS = "orientador/offers"
    const val ORIENTADOR_OFFER_DETAIL = "orientador/offers/{offerId}"
    const val ORIENTADOR_RECENT_ACTIVITY = "orientador/activity"
    const val ORIENTADOR_CHAT = "orientador/chat"
    const val ORIENTADOR_SETTINGS = "orientador/settings"

    // Instituicao
    const val INSTITUICAO_HOME = "instituicao/home"
    const val INSTITUICAO_OFFERS = "instituicao/offers"
    const val INSTITUICAO_OFFER_DETAIL = "instituicao/offers/{offerId}"
    const val INSTITUICAO_RECENT_ACTIVITY = "instituicao/activity"
    const val INSTITUICAO_CHAT = "instituicao/chat"
    const val INSTITUICAO_SETTINGS = "instituicao/settings"
}
