package turmaA.grupoB.LinkStage.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = "Políticas de Privacidade",
                onBack = onBack
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            PrivacySection(
                title = "1. Introdução",
                content = "Bem-vindo ao LinkStage. A sua privacidade é importante para nós. Esta política explica como recolhemos, utilizamos e protegemos os seus dados pessoais de acordo com o Regulamento Geral sobre a Proteção de Dados (RGPD)."
            )

            PrivacySection(
                title = "2. Dados que recolhemos",
                content = "Recolhemos dados como o seu nome, endereço de e-mail, instituição de ensino e informações profissionais (no caso de estudantes) para permitir a utilização das funcionalidades da plataforma, como candidaturas a estágios e comunicação entre utilizadores."
            )

            PrivacySection(
                title = "3. Finalidade do Tratamento",
                content = "Os seus dados são utilizados para:\n• Gestão da conta de utilizador;\n• Facilitar o processo de recrutamento de estágios;\n• Melhorar a experiência na aplicação;\n• Cumprir obrigações legais."
            )

            PrivacySection(
                title = "4. Partilha de Dados",
                content = "Os seus dados pessoais apenas são partilhados com as entidades relevantes (Instituições e Orientadores) quando inicia uma candidatura ou interação na plataforma. Não vendemos os seus dados a terceiros."
            )

            PrivacySection(
                title = "5. Os Seus Direitos",
                content = "Nos termos do RGPD, tem o direito de:\n• Aceder aos seus dados;\n• Retificar informações incorretas;\n• Solicitar o apagamento dos seus dados;\n• Opor-se ao tratamento dos mesmos."
            )

            PrivacySection(
                title = "6. Contacto",
                content = "Para qualquer questão relacionada com a privacidade ou para exercer os seus direitos, os utilizadores podem contactar a coordenação do projeto através da respetiva Instituição de Ensino."
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Última atualização: Junho 2026",
                style = MaterialTheme.typography.bodySmall,
                color = DarkGrey,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        )
    }
}
