package turmaA.grupoB.LinkStage.ui.aluno.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.orientador.InternshipEvaluation
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

private val sampleResultEvaluation = InternshipEvaluation(
    internshipId = "int1",
    schoolMentorName = "Prof. Carvalho",
    schoolMentorObservation = "O aluno demonstrou um excelente desempenho ao longo do estágio, com particular destaque na capacidade de resolver problemas de forma criativa.",
    schoolMentorGrade = "16",
    companyMentorName = "Ana Costa",
    companyMentorObservation = "Boa integração na equipa. Mostrou iniciativa e capacidade de trabalho autónomo.",
    companyMentorGrade = "17",
    finalGrade = 16.5f,
    isCompleted = true,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipResultScreen(
    internshipId: String,
    navController: NavController,
) {
    val evaluation = sampleResultEvaluation

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Resultado do Estágio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkBlue,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            )
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Internship header card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF212121)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("V", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Designer de Produto", color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Viana S.T.Arts", color = LightBlue, fontSize = 14.sp)
                        Text("Concluído", color = LightBlue, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Final grade card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkBlue),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Nota Final",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${evaluation.finalGrade}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "em 20 valores",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // School mentor evaluation
            EvaluationCard(
                mentorName = evaluation.schoolMentorName,
                mentorRole = "Orientador Escolar",
                observation = evaluation.schoolMentorObservation,
                grade = evaluation.schoolMentorGrade,
            )

            // Company mentor evaluation (if exists)
            if (evaluation.companyMentorName.isNotEmpty()) {
                EvaluationCard(
                    mentorName = evaluation.companyMentorName,
                    mentorRole = "Orientador de Empresa",
                    observation = evaluation.companyMentorObservation,
                    grade = evaluation.companyMentorGrade,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Fade2)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Voltar ao Início",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EvaluationCard(
    mentorName: String,
    mentorRole: String,
    observation: String,
    grade: String,
) {
    val initials = mentorName.split(" ")
        .filter { it.isNotEmpty() }
        .map { it.first() }
        .take(2)
        .joinToString("")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LightBlue),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(mentorName, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(mentorRole, color = DarkGrey, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text("$grade valores", color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = BorderGrey)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Observações", color = DarkGrey, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(observation, color = DarkBlue, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun InternshipResultScreenPreview() {
    MaterialTheme {
        InternshipResultScreen(internshipId = "int1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun InternshipResultScreenLandscapePreview() {
    MaterialTheme {
        InternshipResultScreen(internshipId = "int1", navController = rememberNavController())
    }
}
