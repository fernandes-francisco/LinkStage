package turmaA.grupoB.LinkStage.ui.aluno.apply

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import turmaA.grupoB.LinkStage.viewmodel.ApplyViewModel

// region Main Screen

@Composable
fun ApplyScreen(
    offerId: String,
    offerTitle: String = "UI/UX Designer",
    offerCompany: String = "Viana S.T.Arts",
    offerLogoInitial: String = "V",
    offerLogoColor: Color = Color(0xFF212121),
    viewModel: ApplyViewModel,
    onBack: () -> Unit,
    onNavigateToEditCv: () -> Unit,
    onSubmitSuccess: () -> Unit,
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var showCvIncompleteDialog by remember { mutableStateOf(false) }

    // Step 0 validation
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf(false) }
    var institutionError by remember { mutableStateOf(false) }
    var gpaError by remember { mutableStateOf(false) }

    if (showCvIncompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCvIncompleteDialog = false },
            title = {
                Text("CV incompleto.", fontWeight = FontWeight.Bold, color = Red)
            },
            text = {
                Text(
                    "Adiciona pelo menos uma skill ao teu cv para que possas continuar a candidatura.",
                    color = DarkGrey,
                    lineHeight = 22.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = { showCvIncompleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                ) {
                    Text("Voltar", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        // Header
        ApplyHeader(
            offerTitle = offerTitle,
            offerCompany = offerCompany,
            offerLogoInitial = offerLogoInitial,
            offerLogoColor = offerLogoColor,
            onBack = onBack,
        )

        // Stepper
        ApplyStepper(currentStep = currentStep)

        Spacer(modifier = Modifier.height(4.dp))

        // Step title + subtitle
        val (stepTitle, stepSubtitle) = when (currentStep) {
            0 -> "Informação Pessoal" to "Aqui podes editar a tua informação."
            1 -> "Informação Essencial" to "Completa com informação importantes."
            else -> "Rever Candidatura" to "Revê os teus dados para continuar."
        }
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = stepTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
            Text(text = stepSubtitle, fontSize = 13.sp, color = DarkGrey)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Step content
        AnimatedContent(
            targetState = currentStep,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                (slideInHorizontally { if (targetState > initialState) it else -it } + fadeIn())
                    .togetherWith(slideOutHorizontally { if (targetState > initialState) -it else it } + fadeOut())
            },
            label = "step",
        ) { step ->
            when (step) {
                0 -> StepPersonalInfo(
                    viewModel = viewModel,
                    nameError = nameError,
                    emailError = emailError,
                    phoneError = phoneError,
                    courseError = courseError,
                    institutionError = institutionError,
                    gpaError = gpaError,
                )
                1 -> StepEssentialInfo(
                    viewModel = viewModel,
                    onEditCv = onNavigateToEditCv,
                )
                2 -> StepReview(viewModel = viewModel)
            }
        }

        // Bottom bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    when (currentStep) {
                        0 -> {
                            nameError = viewModel.fullName.isBlank()
                            emailError = viewModel.email.isBlank()
                            phoneError = viewModel.phone.isBlank()
                            courseError = viewModel.course.isBlank()
                            institutionError = viewModel.institution.isBlank()
                            gpaError = viewModel.gpa.isBlank()
                            val valid = !nameError && !emailError && !phoneError &&
                                !courseError && !institutionError && !gpaError
                            if (valid) currentStep = 1
                        }
                        1 -> {
                            if (!viewModel.isCvComplete) {
                                showCvIncompleteDialog = true
                            } else {
                                currentStep = 2
                            }
                        }
                        2 -> {
                            viewModel.submitApplication()
                            onSubmitSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text(
                    text = if (currentStep == 2) "Submeter" else "Continuar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }

            TextButton(
                onClick = {
                    viewModel.saveAsDraft()
                    onBack()
                },
            ) {
                Text("Guardar como rascunho", color = DarkGrey, fontSize = 13.sp)
            }
        }
    }
}

// endregion

// region Header & Stepper

@Composable
private fun ApplyHeader(
    offerTitle: String,
    offerCompany: String,
    offerLogoInitial: String,
    offerLogoColor: Color,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = DarkBlue,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Finalizar Candidatura",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundLight)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(offerLogoColor),
                contentAlignment = Alignment.Center,
            ) {
                Text(offerLogoInitial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(offerTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                Text(offerCompany, fontSize = 13.sp, color = LightBlue)
            }
        }
    }
}

@Composable
private fun ApplyStepper(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 40.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0..2) {
            val isCompleted = i < currentStep
            val isActive = i == currentStep

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> LightBlue
                            isActive -> DarkBlue
                            else -> BorderGrey
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                } else {
                    Text(
                        "${i + 1}",
                        color = if (isActive) Color.White else DarkGrey,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            if (i < 2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(if (isCompleted) LightBlue else BorderGrey),
                )
            }
        }
    }
}

// endregion

// region Step 0 — Personal Info

@Composable
private fun StepPersonalInfo(
    viewModel: ApplyViewModel,
    nameError: Boolean,
    emailError: Boolean,
    phoneError: Boolean,
    courseError: Boolean,
    institutionError: Boolean,
    gpaError: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ApplyTextField(
            label = "Nome completo",
            value = viewModel.fullName,
            onValueChange = { viewModel.fullName = it },
            icon = Icons.Outlined.Person,
            isError = nameError,
        )
        ApplyTextField(
            label = "Email",
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            icon = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email,
            isError = emailError,
        )
        ApplyTextField(
            label = "Telemóvel",
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            icon = Icons.Outlined.Phone,
            keyboardType = KeyboardType.Phone,
            isError = phoneError,
        )
        ApplyTextField(
            label = "Curso",
            value = viewModel.course,
            onValueChange = { viewModel.course = it },
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            isError = courseError,
        )
        ApplyTextField(
            label = "Instituição",
            value = viewModel.institution,
            onValueChange = { viewModel.institution = it },
            icon = Icons.Outlined.School,
            isError = institutionError,
        )
        ApplyTextField(
            label = "Média atual (0-20)",
            value = viewModel.gpa,
            onValueChange = { viewModel.gpa = it },
            icon = Icons.Outlined.Grade,
            keyboardType = KeyboardType.Decimal,
            isError = gpaError,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ApplyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionLabel(label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(icon, contentDescription = null, tint = DarkGrey) },
            placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Red else LightBlue,
                unfocusedBorderColor = if (isError) Red else BorderGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
        )
        if (isError) {
            Text("Campo obrigatório", color = Red, fontSize = 12.sp)
        }
    }
}

// endregion

// region Step 1 — Essential Info

@Composable
private fun StepEssentialInfo(
    viewModel: ApplyViewModel,
    onEditCv: () -> Unit,
) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.motivationFile = uri
            viewModel.motivationFileName = uri.lastPathSegment ?: "ficheiro.pdf"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Personal Statement
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Declaração Pessoal")
            Text(
                "Explicação curta do porquê és a pessoa perfeita para o cargo.",
                fontSize = 12.sp,
                color = DarkGrey,
            )
            OutlinedTextField(
                value = viewModel.personalStatement,
                onValueChange = { viewModel.personalStatement = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LightBlue,
                    unfocusedBorderColor = BorderGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                minLines = 5,
            )
        }

        // Edit CV
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Editar CV")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderGrey, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable { onEditCv() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Editar CV", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkBlue)
                    Text(
                        "${viewModel.userSkills.size} skills adicionadas",
                        fontSize = 12.sp,
                        color = if (viewModel.isCvComplete) LightBlue else DarkGrey,
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = DarkGrey,
                )
            }
        }

        // Motivation Letter
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Carta de Motivação")
            if (viewModel.motivationFile != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightBlue, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            viewModel.motivationFileName,
                            fontSize = 13.sp,
                            color = DarkBlue,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.motivationFile = null
                            viewModel.motivationFileName = ""
                        },
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remover", tint = DarkGrey)
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { filePickerLauncher.launch("application/*") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGrey),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            Icons.Outlined.Description,
                            contentDescription = null,
                            tint = DarkGrey,
                            modifier = Modifier.size(48.dp),
                        )
                        Text("Selecionar Ficheiro", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                        Text("PDF, DOCX até 10MB", color = DarkGrey, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

// endregion

// region Step 2 — Review

@Composable
private fun StepReview(viewModel: ApplyViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        ReviewRow(label = "Nome", value = viewModel.fullName)
        ReviewRow(label = "E-mail", value = viewModel.email)
        ReviewRow(
            label = "Curso",
            value = "${viewModel.course} - ${viewModel.institution}",
        )
        ReviewRow(label = "Média", value = "${viewModel.gpa} valores")
        ReviewRow(
            label = "CV",
            value = if (viewModel.isCvComplete) "Completo" else "Incompleto",
            valueColor = if (viewModel.isCvComplete) LightBlue else Red,
        )
        ReviewRow(
            label = "Carta de Motivação",
            value = if (viewModel.isMotivationComplete) "Completo" else "Não anexada",
            valueColor = if (viewModel.isMotivationComplete) LightBlue else DarkGrey,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    valueColor: Color = DarkBlue,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = DarkGrey,
                modifier = Modifier.weight(0.4f),
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = valueColor,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(0.6f),
            )
        }
        HorizontalDivider(color = BorderGrey)
    }
}

// endregion

// region Preview

@Preview(showSystemUi = true)
@Composable
private fun ApplyScreenPreview() {
    MaterialTheme {
        ApplyScreen(
            offerId = "2",
            viewModel = ApplyViewModel(),
            onBack = {},
            onNavigateToEditCv = {},
            onSubmitSuccess = {},
        )
    }
}

// endregion
