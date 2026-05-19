package turmaA.grupoB.LinkStage.ui.introSliders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LinkStageTheme
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.ui.theme.CompanyGreen

data class SliderData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color
)

@Composable
fun IntroSlidersScreen(
    onFinish: () -> Unit = {}
) {
    val sliders = remember {
        listOf(
            SliderData(
                "Bem-vindo ao LinkStage",
                "A plataforma que liga estudantes, orientadores e empresas para o sucesso profissional.",
                Icons.Default.AutoStories,
                MediumBlue
            ),
            SliderData(
                "Gira o teu percurso",
                "Acompanha candidaturas, regista atividades e mantém o foco nos teus objetivos.",
                Icons.Default.BusinessCenter,
                CompanyGreen
            ),
            SliderData(
                "Trabalho em equipa",
                "Comunicação facilitada entre todos os intervenientes do processo de estágio.",
                Icons.Default.Groups,
                MediumBlue
            ),
            SliderData(
                "Alcança o sucesso",
                "Conclui etapas, obtém reconhecimento e entra no mercado de trabalho com o pé direito.",
                Icons.Default.EmojiEvents,
                MediumBlue.copy(alpha = 0.8f)
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { sliders.size })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                SliderContent(sliders[page])
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .height(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(sliders.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) MediumBlue else Color.LightGray.copy(alpha = 0.5f)
                        val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .width(width)
                                .height(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (pagerState.currentPage < sliders.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Fade2, shape = RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = if (pagerState.currentPage == sliders.size - 1) "Começar" else "Seguinte",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onFinish) {
                    Text(
                        text = "Saltar",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SliderContent(data: SliderData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(data.iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IntroSlidersPreview() {
    LinkStageTheme {
        IntroSlidersScreen()
    }
}
