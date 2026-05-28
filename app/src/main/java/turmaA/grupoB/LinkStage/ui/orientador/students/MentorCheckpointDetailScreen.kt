package turmaA.grupoB.LinkStage.ui.orientador.students

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLog
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.orientador.formatCheckpointDateLong
import turmaA.grupoB.LinkStage.ui.orientador.sampleMentorActivityLogs
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorCheckpointDetailScreen(
    checkpointId: String,
    navController: NavController,
) {
    val activityLog = sampleMentorActivityLogs.find { it.id == checkpointId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Aluno", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = DarkBlue)
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
            Text(
                text = activityLog.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Delivery date
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = LightBlue,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Data da entrega",
                        color = DarkGrey,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = formatCheckpointDateLong(activityLog.date),
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            ContentSection(title = "Descrição da entrega") {
                Text(
                    text = activityLog.description,
                    fontSize = 14.sp,
                    color = DarkGrey,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attachments
            if (activityLog.submittedFiles.isNotEmpty()) {
                ContentSection(title = "Anexos") {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        activityLog.submittedFiles.forEach { file ->
                            ExpandableFileRow(file = file)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MentorCheckpointDetailScreenPreview() {
    MaterialTheme {
        MentorCheckpointDetailScreen(checkpointId = "1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun MentorCheckpointDetailScreenLandscapePreview() {
    MaterialTheme {
        MentorCheckpointDetailScreen(checkpointId = "1", navController = rememberNavController())
    }
}
