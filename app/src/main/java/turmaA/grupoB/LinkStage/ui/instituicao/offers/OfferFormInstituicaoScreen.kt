package turmaA.grupoB.LinkStage.ui.instituicao.offers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import turmaA.grupoB.LinkStage.ui.admin.AdminMentor
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.aluno.apply.skillCategories
import turmaA.grupoB.LinkStage.ui.common.ConfirmationDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageButton
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import turmaA.grupoB.LinkStage.viewmodel.OfferFormViewModel

private val categoryOptions = skillCategories.keys.sorted()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferFormInstituicaoScreen(
    offerId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: OfferFormViewModel = viewModel(),
) {
    val currentStep = viewModel.currentStep

    // Validation errors
    var titleError by rememberSaveable { mutableStateOf(false) }
    var categoryError by rememberSaveable { mutableStateOf(false) }
    var descriptionError by rememberSaveable { mutableStateOf(false) }
    var locationError by rememberSaveable { mutableStateOf(false) }
    var deadlineError by rememberSaveable { mutableStateOf(false) }

    var showPublishDialog by remember { mutableStateOf(false) }

    if (showPublishDialog) {
        ConfirmationDialog(
            title = "Publicar oferta?",
            body = "A oferta ficará visível para os estudantes da comunidade LinkStage.",
            confirmLabel = "Publicar",
            onConfirm = {
                showPublishDialog = false
                viewModel.createOffer()
                navController.navigate(InstituicaoRoutes.offerSuccessRoute(offerId)) {
                    popUpTo(InstituicaoRoutes.offerFormRoute(offerId)) { inclusive = true }
                }
            },
            onDismiss = { showPublishDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        // Header
        OfferFormHeader(onBack = { navController.popBackStack() })

        // Stepper
        OfferFormStepper(currentStep = currentStep)

        Spacer(modifier = Modifier.height(4.dp))

        // Step title + subtitle
        val (stepTitle, stepSubtitle) = when (currentStep) {
            0 -> "Detalhes" to "Preencha os detalhes da oferta de estágio."
            1 -> "Requisitos e Logística" to "Indique os requisitos que o candidato deve cumprir."
            else -> "Rever Oferta" to "Reveja os dados antes de publicar."
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
            label = "offer_step",
        ) { step ->
            when (step) {
                0 -> StepDetails(
                    viewModel = viewModel,
                    titleError = titleError,
                    categoryError = categoryError,
                    descriptionError = descriptionError,
                )
                1 -> StepRequirements(
                    viewModel = viewModel,
                    locationError = locationError,
                    deadlineError = deadlineError,
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
            LinkStageButton(
                text = if (currentStep == 2) "Publicar oferta" else "Seguinte",
                onClick = {
                    when (currentStep) {
                        0 -> {
                            titleError = viewModel.title.isBlank()
                            categoryError = viewModel.category.isBlank()
                            descriptionError = viewModel.description.isBlank()
                            if (!titleError && !categoryError && !descriptionError) {
                                viewModel.currentStep = 1
                            }
                        }
                        1 -> {
                            locationError = viewModel.location.isBlank()
                            deadlineError = viewModel.deadline.isBlank() || !viewModel.isValidDate(viewModel.deadline)
                            if (!locationError && !deadlineError) {
                                viewModel.currentStep = 2
                            }
                        }
                        2 -> {
                            showPublishDialog = true
                        }
                    }
                },
                height = 50.dp,
            )

            TextButton(
                onClick = {
                    if (currentStep == 0) {
                        navController.popBackStack()
                    } else {
                        viewModel.currentStep = currentStep - 1
                    }
                },
            ) {
                Text("Voltar", color = DarkGrey, fontSize = 13.sp)
            }
        }
    }
}

// region Header

@Composable
private fun OfferFormHeader(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = DarkBlue,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Ofertas",
                color = DarkBlue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkBlue.copy(alpha = 0.06f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGrey),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "RECRUITMENT SUITE",
                    color = LightBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cria uma nova oferta de estágio",
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Atraia talento académico de topo definindo uma função clara e impactante. A sua oferta será visível para os estudantes da comunidade LinkStage.",
                    color = DarkGrey,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                )
            }
        }
    }
}

// endregion

// region Stepper

@Composable
private fun OfferFormStepper(currentStep: Int) {
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
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
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

// region Step 0 — Detalhes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepDetails(
    viewModel: OfferFormViewModel,
    titleError: Boolean,
    categoryError: Boolean,
    descriptionError: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Tipo de estágio (escola vs empresa)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Tipo de estágio")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = if (viewModel.isCompanyOffer) "Estágio em empresa" else "Estágio escolar",
                    color = DarkBlue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                )
                Switch(
                    checked = viewModel.isCompanyOffer,
                    onCheckedChange = { viewModel.isCompanyOffer = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = LightBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = BorderGrey,
                    ),
                )
            }
            Text(
                text = if (viewModel.isCompanyOffer)
                    "Estágios em empresa requerem um orientador escolar e um orientador da empresa."
                else
                    "Estágios escolares requerem apenas um orientador da instituição de ensino.",
                color = DarkGrey, fontSize = 12.sp,
            )
        }

        // Orientador escolar
        OfferDropdown(
            label = "Orientador escolar",
            value = viewModel.schoolMentorName,
            placeholder = "Escolha o orientador da instituição",
            options = sampleMentors.map { it.name },
            onOptionSelected = { name ->
                viewModel.schoolMentorName = name
                viewModel.schoolMentorId = sampleMentors.find { it.name == name }?.id ?: ""
            },
        )

        // Orientador da empresa (só visível se for estágio empresa)
        if (viewModel.isCompanyOffer) {
            OfferTextField(
                label = "Orientador da empresa",
                value = viewModel.companyMentorName,
                onValueChange = { viewModel.companyMentorName = it },
                placeholder = "Nome do orientador designado pela empresa",
            )
        }

        // Título
        OfferTextField(
            label = "Título",
            value = viewModel.title,
            onValueChange = { viewModel.title = it },
            placeholder = "Ex: Estágio de Engenheiro de Software",
            isError = titleError,
        )

        // Categoria
        OfferDropdown(
            label = "Categoria",
            value = viewModel.category,
            placeholder = "Escolha uma categoria",
            options = categoryOptions,
            onOptionSelected = { viewModel.category = it },
            isError = categoryError,
        )

        // Descrição
        OfferTextField(
            label = "Descrição",
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            placeholder = "Descreva a missão, a equipa e o que o estagiário irá aprender...",
            singleLine = false,
            minLines = 4,
            maxLines = 8,
            isError = descriptionError,
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

// endregion

// region Step 1 — Requisitos e Logística

@Composable
private fun StepRequirements(
    viewModel: OfferFormViewModel,
    locationError: Boolean,
    deadlineError: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Requisitos
        OfferTextField(
            label = "Requisitos da candidatura",
            value = viewModel.requirements,
            onValueChange = { viewModel.requirements = it },
            placeholder = "Indique competências, ferramentas ou formação académica necessária...",
            singleLine = false,
            minLines = 4,
            maxLines = 8,
        )

        // Local
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Local")
            OutlinedTextField(
                value = viewModel.location,
                onValueChange = { viewModel.location = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = DarkGrey,
                        modifier = Modifier.size(18.dp),
                    )
                },
                placeholder = { Text("Remoto ou nome da cidade", color = DarkGrey, fontSize = 14.sp) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (locationError) Red else LightBlue,
                    unfocusedBorderColor = if (locationError) Red else BorderGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = DarkBlue,
                    unfocusedTextColor = DarkBlue,
                ),
                singleLine = true,
                isError = locationError,
            )
            if (locationError) {
                Text("Este campo é obrigatório", color = Red, fontSize = 12.sp)
            }
        }

        // Data de fecho
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Data de fecho de candidaturas")
            OutlinedTextField(
                value = viewModel.deadline,
                onValueChange = { newValue ->
                    val digits = newValue.filter { it.isDigit() }
                    if (digits.length <= 8) {
                        val formatted = buildString {
                            digits.forEachIndexed { index, c ->
                                append(c)
                                if ((index == 1 || index == 3) && index < digits.lastIndex) {
                                    append('/')
                                }
                            }
                        }
                        viewModel.deadline = formatted
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = DarkGrey,
                        modifier = Modifier.size(18.dp),
                    )
                },
                placeholder = { Text("dd/mm/aaaa", color = DarkGrey, fontSize = 14.sp) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (deadlineError) Red else LightBlue,
                    unfocusedBorderColor = if (deadlineError) Red else BorderGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = DarkBlue,
                    unfocusedTextColor = DarkBlue,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = deadlineError,
            )
            if (deadlineError) {
                Text("Formato de data inválido (dd/mm/aaaa)", color = Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

// endregion

// region Step 2 — Rever

@Composable
private fun StepReview(viewModel: OfferFormViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        ReviewRow(label = "Título", value = viewModel.title)
        ReviewRow(
            label = "Tipo",
            value = if (viewModel.isCompanyOffer) "Empresa" else "Escolar",
        )
        ReviewRow(
            label = "Orientador escolar",
            value = viewModel.schoolMentorName.ifBlank { "Não atribuído" },
            valueColor = if (viewModel.schoolMentorName.isBlank()) DarkGrey else DarkBlue,
        )
        if (viewModel.isCompanyOffer) {
            ReviewRow(
                label = "Orientador empresa",
                value = viewModel.companyMentorName.ifBlank { "Não atribuído" },
                valueColor = if (viewModel.companyMentorName.isBlank()) DarkGrey else DarkBlue,
            )
        }
        ReviewRow(label = "Categoria", value = viewModel.category)
        ReviewRow(label = "Local", value = viewModel.location)
        ReviewRow(label = "Data de fecho", value = viewModel.deadline)
        ReviewRow(
            label = "Requisitos",
            value = if (viewModel.requirements.isNotBlank()) "Completo" else "Sem informação",
            valueColor = if (viewModel.requirements.isNotBlank()) LightBlue else DarkGrey,
        )
        ReviewRow(
            label = "Descrição",
            value = "Completo",
            valueColor = LightBlue,
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

// region Reusable form fields

@Composable
private fun OfferTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionLabel(label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = DarkGrey, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Red else LightBlue,
                unfocusedBorderColor = if (isError) Red else BorderGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue,
            ),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
        )
        if (isError) {
            Text("Este campo é obrigatório", color = Red, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OfferDropdown(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    isError: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionLabel(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(placeholder, color = DarkGrey, fontSize = 14.sp) },
                trailingIcon = {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = DarkGrey)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Red else LightBlue,
                    unfocusedBorderColor = if (isError) Red else BorderGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = DarkBlue,
                    unfocusedTextColor = DarkBlue,
                ),
                singleLine = true,
                isError = isError,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = DarkBlue) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
        if (isError) {
            Text("Este campo é obrigatório", color = Red, fontSize = 12.sp)
        }
    }
}

// endregion

// region Previews

@Preview(showSystemUi = true)
@Composable
private fun OfferFormInstituicaoScreenPreview() {
    MaterialTheme {
        OfferFormInstituicaoScreen(offerId = "new", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun OfferFormInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        OfferFormInstituicaoScreen(offerId = "new", navController = rememberNavController())
    }
}

// endregion
