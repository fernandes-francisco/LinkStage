package turmaA.grupoB.LinkStage.ui.instituicao.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.aluno.home.ApplicationStatus
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferDetail
import turmaA.grupoB.LinkStage.ui.aluno.offers.ResponsibilityItem
import turmaA.grupoB.LinkStage.ui.common.CheckItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.common.ContentSectionColored
import turmaA.grupoB.LinkStage.ui.common.ConfirmationDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionApplication
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionApplications
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

private val sampleOfferDetail = OfferDetail(
    id = "1",
    title = "Designer de Produto",
    company = "ESTG-IPVC",
    logoInitial = "E",
    logoColor = Color(0xFF1565C0),
    location = "Porto, PT",
    duration = "6 Meses",
    type = "Tempo Inteiro",
    aboutCompany = "A ESTG-IPVC é uma escola superior de tecnologia e gestão que promove a inovação e o desenvolvimento regional.",
    responsibilities = listOf(
        "Realizar a prototipagem da aplicação web.",
        "Colaborar com a equipa, com o objetivo de cruzar competências.",
        "Desenvolver o sistema de criação de dashboards.",
    ),
    requirements = listOf(
        "Experiência com Figma e prototipagem interativa.",
        "Portfólio de UI para demonstração.",
        "Comunicação excelente, escrita e verbal, em inglês.",
    ),
    benefits = listOf("Passe de Transporte Público", "Programa de Mentoria", "Mercado Competitivo"),
    deadlineDays = 4,
    applicantsCount = 12,
)

@Composable
fun OfferDetailInstituicaoScreen(
    offerId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val offer = sampleOfferDetail
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showCloseDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showCloseDialog) {
        ConfirmationDialog(
            title = "Fechar oferta?",
            body = "A oferta ficará invisível para novos candidatos. Poderá reabri-la posteriormente.",
            confirmLabel = "Fechar",
            onConfirm = { showCloseDialog = false },
            onDismiss = { showCloseDialog = false },
        )
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            title = "Remover oferta?",
            body = "Esta ação é irreversível. Todas as candidaturas associadas serão removidas.",
            confirmLabel = "Remover",
            isDanger = true,
            onConfirm = {
                showDeleteDialog = false
                navController.popBackStack()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        // Fixed header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 12.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ofertas", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

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
                        .background(offer.logoColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(offer.logoInitial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(offer.title, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(offer.company, color = LightBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }

        LinkStageTabRow(
            tabs = listOf("Detalhes", "Candidaturas", "Gerir"),
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> DetailsTab(offer = offer)
                1 -> ApplicationsTab(navController = navController)
                2 -> ManageTab(
                    onEdit = { navController.navigate(InstituicaoRoutes.offerFormRoute(offer.id)) },
                    onClose = { showCloseDialog = true },
                    onDelete = { showDeleteDialog = true },
                )
            }
        }
    }
}

// region Tab 0 — Details

@Composable
private fun DetailsTab(offer: OfferDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        OfferMetaChips(offer = offer)

        Spacer(modifier = Modifier.height(16.dp))

        ContentSection(title = "Sobre a empresa") {
            Text(
                text = offer.aboutCompany,
                fontSize = 14.sp, color = DarkGrey, lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ContentSection(title = "Responsabilidades") {
            offer.responsibilities.forEach { item -> ResponsibilityItem(text = item) }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        ContentSectionColored(title = "Requisitos") {
            offer.requirements.forEach { item -> CheckItem(text = item) }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun OfferMetaChips(offer: OfferDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MetaChipSmall(label = "Localização", value = offer.location, modifier = Modifier.weight(1f))
        MetaChipSmall(label = "Duração", value = offer.duration, modifier = Modifier.weight(1f))
        MetaChipSmall(label = "Tipo", value = offer.type, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MetaChipSmall(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(label, fontSize = 11.sp, color = DarkGrey, textAlign = TextAlign.Center)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkBlue, textAlign = TextAlign.Center)
        }
    }
}

// endregion

// region Tab 1 — Applications

private val applicationStatusFilters = listOf("Todas", "Pendente", "Aceite", "Rejeitada")

@Composable
private fun ApplicationsTab(navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf("Todas") }

    val filtered = sampleInstitutionApplications.filter { app ->
        val matchesSearch = searchQuery.isEmpty() ||
            app.studentName.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "Pendente" -> app.status == ApplicationStatus.PENDING
            "Aceite" -> app.status == ApplicationStatus.ACCEPTED
            "Rejeitada" -> app.status == ApplicationStatus.REJECTED
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            placeholder = { Text("Pesquisar...", color = DarkGrey) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = DarkGrey) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = DarkBlue,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            ),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            applicationStatusFilters.forEach { filter ->
                val isSelected = filter == selectedFilter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            ),
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkBlue,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = DarkGrey,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true, selected = isSelected,
                        borderColor = BorderGrey, selectedBorderColor = Color.Transparent,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            EmptyStateCard("Nenhuma candidatura encontrada.")
        } else {
            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                items(filtered, key = { it.id }) { application ->
                    ApplicationListItem(
                        application = application,
                        onClick = { navController.navigate(InstituicaoRoutes.applicationDetailRoute(application.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationListItem(
    application: InstitutionApplication,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(avatarColors[application.studentAvatarColorIndex % avatarColors.size]),
                contentAlignment = Alignment.Center,
            ) {
                Text(application.studentAvatarInitials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(application.studentName, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Instituição: ${application.institution}", color = DarkGrey, fontSize = 12.sp)
            }
            ApplicationStatusBadge(application.status)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = DarkGrey, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ApplicationStatusBadge(status: ApplicationStatus) {
    val (label, color) = when (status) {
        ApplicationStatus.ACCEPTED -> "Aceite" to Color(0xFF4CAF50)
        ApplicationStatus.REJECTED -> "Recusado" to Red
        ApplicationStatus.PENDING -> "Pendente" to Color(0xFFF5C518)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp)
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Text(
            text = message, color = DarkGrey, fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(20.dp),
        )
    }
}

// endregion

// region Tab 2 — Manage

@Composable
private fun ManageTab(
    onEdit: () -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = onEdit,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Fade2, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Editar oferta", fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(DarkGrey)),
        ) {
            Icon(Icons.Outlined.Cancel, contentDescription = null, tint = DarkGrey, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Fechar oferta", color = DarkGrey, fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = onDelete,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Red.copy(alpha = 0.12f)),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(Red)),
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null, tint = Red, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Remover oferta", color = Red, fontWeight = FontWeight.SemiBold)
        }
    }
}

// endregion

// region Previews

@Preview(showSystemUi = true)
@Composable
private fun OfferDetailInstituicaoScreenPreview() {
    MaterialTheme {
        OfferDetailInstituicaoScreen(offerId = "1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun OfferDetailInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        OfferDetailInstituicaoScreen(offerId = "1", navController = rememberNavController())
    }
}

// endregion
