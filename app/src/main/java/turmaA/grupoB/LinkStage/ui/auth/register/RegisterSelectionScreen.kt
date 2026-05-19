package turmaA.grupoB.LinkStage.ui.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.ui.theme.AdvisorPurple
import turmaA.grupoB.LinkStage.ui.theme.CompanyGreen
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LinkStageTheme
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue

@Composable
fun RegisterSelectionScreen(
    modifier: Modifier = Modifier,
    onContinueClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedProfile by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_link_stage2),
                contentDescription = "LinkStage Logo",
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Passo 1 de 2 - Seleciona o teu perfil",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileCard(
                title = "Estudante",
                description = listOf(
                    "Pesquisa de vagas",
                    "Candidaturas online",
                    "Registo de atividades",
                    "Relatório final"
                ),
                icon = Icons.Outlined.AutoStories,
                color = MediumBlue,
                isSelected = selectedProfile == "Estudante",
                onClick = { selectedProfile = "Estudante" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard(
                title = "Orientador",
                description = listOf("Monitorização de atividades"),
                icon = Icons.Outlined.Person,
                color = AdvisorPurple,
                isSelected = selectedProfile == "Orientador",
                onClick = { selectedProfile = "Orientador" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard(
                title = "Empresa",
                description = listOf(
                    "Publicar vagas",
                    "Gestão de candidatos",
                    "Acompanhamento de estágio",
                    "Avaliações"
                ),
                icon = Icons.Outlined.BusinessCenter,
                color = CompanyGreen,
                isSelected = selectedProfile == "Empresa",
                onClick = { selectedProfile = "Empresa" }
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                val isContinueEnabled = selectedProfile.isNotEmpty()
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .then(
                            if (isContinueEnabled) Modifier.background(Fade2) 
                            else Modifier.background(Color.LightGray.copy(alpha = 0.5f))
                        )
                ) {
                    Button(
                        onClick = { onContinueClick(selectedProfile) },
                        enabled = isContinueEnabled,
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues()
                    ) {
                        Text(
                            text = "Continuar",
                            color = if (isContinueEnabled) Color.White else Color.Gray.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Fade2),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MediumBlue
                    )
                ) {
                    Text(
                        text = "Voltar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    title: String,
    description: List<String>,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) color.copy(alpha = 0.05f) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(165.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            description.forEach { item ->
                Text(
                    text = "-$item",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterSelectionScreenPreview() {
    LinkStageTheme {
        RegisterSelectionScreen()
    }
}
