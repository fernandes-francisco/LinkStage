package turmaA.grupoB.LinkStage.ui.aluno.activity

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.repository.internship.InternshipRepository
import turmaA.grupoB.LinkStage.data.repository.internship.LocalActivityRepository
import turmaA.grupoB.LinkStage.data.room.AtDatabase
import turmaA.grupoB.LinkStage.ui.common.CheckItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.common.ContentSectionColored
import turmaA.grupoB.LinkStage.ui.common.LinkStageButton
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.common.SecondaryTopBar
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.Fade3
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipUiState
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipViewModel
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipViewModelFactory
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

private fun formatDateUppercase(date: LocalDate, context: Context): String {
    val monthNames = context.resources.getStringArray(R.array.months_short)
    return "${monthNames[date.monthValue - 1].uppercase()},${date.dayOfMonth}"
}

@Composable
fun ActivityDetailAlunoScreen(
    checkpointId: String,
    onBack: () -> Unit,
    activityLog: ActivityLog? = null,
    internshipViewModel: InternshipViewModel = viewModel(
        factory = InternshipViewModelFactory(
            InternshipRepository(),
            LocalActivityRepository(
                AtDatabase.getDatabase(LocalContext.current).atividadeDAO()
            ),
        )
    ),
) {
    val context = LocalContext.current
    val internshipUiState by internshipViewModel.uiState.collectAsState()

    LaunchedEffect(checkpointId, activityLog) {
        if (activityLog == null) {
            internshipViewModel.loadActivityLogById(checkpointId)
        }
    }

    val resolvedActivityLog = activityLog ?: when (val state = internshipUiState) {
        is InternshipUiState.SuccessActivity -> state.activityLogModel.toActivityLog()
        else -> null
    }

    if (resolvedActivityLog == null) {
        ActivityDetailState(
            message = when (val state = internshipUiState) {
                InternshipUiState.Idle,
                InternshipUiState.Loading -> stringResource(R.string.activity_detail_loading)

                is InternshipUiState.Error -> state.message
                else -> stringResource(R.string.activity_detail_not_found)
            },
            onBack = onBack,
        )
        return
    }

    var hasSubmitted by remember { mutableStateOf(resolvedActivityLog.hasSubmitted) }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val supportsSubmission = activityLog != null

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fileUri = uri
        fileName = uri?.lastPathSegment
    }

    val canSubmit = supportsSubmission && !hasSubmitted

    if (showConfirmDialog) {
        LinkStageDialog(
            title = stringResource(R.string.activity_submit_title),
            onConfirm = {
                hasSubmitted = true
                showConfirmDialog = false
            },
            onDismiss = { showConfirmDialog = false },
            confirmText = stringResource(R.string.activity_submit),
            dismissText = stringResource(R.string.dialog_cancel),
            content = {
                Text(
                    stringResource(R.string.activity_submit_message),
                    color = DarkGrey,
                    lineHeight = 22.sp,
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        SecondaryTopBar(title = stringResource(R.string.activity_detail_title), onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            // Header with logo and background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundLight)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(resolvedActivityLog.companyLogoColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            resolvedActivityLog.companyLogoInitial,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            resolvedActivityLog.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue
                        )
                        Text(
                            resolvedActivityLog.company,
                            fontSize = 13.sp,
                            color = LightBlue
                        )
                    }
                }
            }

            // Deadline row
            Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = LightBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.activity_delivery_date), fontSize = 13.sp, color = DarkGrey)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        formatDateUppercase(resolvedActivityLog.date, context),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Descrição
                ContentSection(title = stringResource(R.string.activity_description)) {
                    Text(
                        text = resolvedActivityLog.description,
                        fontSize = 14.sp,
                        color = DarkGrey,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Requisitos
                if (resolvedActivityLog.requirements.isNotEmpty()) {
                    ContentSectionColored(title = stringResource(R.string.activity_requirements)) {
                        resolvedActivityLog.requirements.forEach { req ->
                            CheckItem(text = req)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Anexos
                ContentSection(title = stringResource(R.string.activity_attachments)) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        if (hasSubmitted) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlue, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(R.string.activity_doc_submitted), fontSize = 13.sp, color = DarkBlue, modifier = Modifier.weight(1f))
                                Text(stringResource(R.string.activity_doc_status), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LightBlue)
                            }
                        } else if (fileUri != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlue, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fileName ?: stringResource(R.string.common_file),
                                        fontSize = 13.sp,
                                        color = DarkBlue,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                IconButton(onClick = { fileUri = null; fileName = null }) {
                                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.common_remove), tint = DarkGrey)
                                }
                            }
                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = canSubmit) { launcher.launch("*/*") },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderGrey),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        Icons.Outlined.Description,
                                        contentDescription = null,
                                        tint = DarkGrey,
                                        modifier = Modifier.size(40.dp),
                                    )
                                    Text(stringResource(R.string.activity_select_files), fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                                    Text(stringResource(R.string.apply_file_format), color = DarkGrey, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Submit button
            LinkStageButton(
                text = if (hasSubmitted) stringResource(R.string.activity_submitted) else stringResource(R.string.activity_submit),
                onClick = { if (canSubmit) showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                enabled = canSubmit,
                brush = if (hasSubmitted) Fade3 else Fade2
            )
    }
}

@Composable
private fun ActivityDetailState(
    message: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        SecondaryTopBar(title = stringResource(R.string.activity_detail_title), onBack = onBack)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = message,
                color = DarkGrey,
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

private fun ActivityLogModel.toActivityLog(): ActivityLog? {
    val parsedDate = runCatching { LocalDate.parse(activityDate) }.getOrNull()
        ?: return null
    val activityTitle = type?.takeIf { it.isNotBlank() } ?: description

    return ActivityLog(
        id = id,
        title = activityTitle,
        description = description,
        date = parsedDate,
        status = ActivityLogStatus.COMPLETED,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ActivityDetailScreenPreview() {
    MaterialTheme {
        ActivityDetailAlunoScreen(checkpointId = "2", onBack = {})
    }
}
