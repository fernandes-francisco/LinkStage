package turmaA.grupoB.LinkStage.ui.aluno.offers

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import turmaA.grupoB.LinkStage.viewmodel.DiscoverViewModel

// region Data models

data class OfferItem(
    val id: String,
    val title: String,
    val company: String,
    val type: String,
    val publishedAgo: String,
    val logoColor: Color,
    val logoInitial: String,
    val isFavourite: Boolean = false,
    val duration: String = "",
    val area: String = "",
    val location: String = "",
    val deadline: String = "",
)

data class DiscoverFilters(
    val area: String = "",
    val location: String = "",
    val workModel: String = "",
    val duration: String = "",
    val deadline: String = "",
)

// endregion

private val topChipFilters = listOf("Todas", "Remotas", "Tempo Inteiro", "Tecnologia")

@Composable
fun OffersAlunoScreen(
    modifier: Modifier = Modifier,
    onOfferClick: (String) -> Unit = {},
    discoverViewModel: DiscoverViewModel = viewModel(),
) {
    val searchQuery by discoverViewModel.searchQuery.collectAsState()
    val filteredOffers by discoverViewModel.filteredOffers.collectAsState()
    val currentFilters by discoverViewModel.filters.collectAsState()
    val hasActiveFilters = currentFilters != DiscoverFilters()

    var showFilterModal by remember { mutableStateOf(false) }
    var selectedTopFilter by remember { mutableStateOf("Todas") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                LinkStageLogo()
            }
        }

        item {
            Text(
                text = "Descobre Oportunidades",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            SearchBarWithFilter(
                query = searchQuery,
                onQueryChange = { discoverViewModel.updateSearchQuery(it) },
                hasActiveFilters = hasActiveFilters,
                onFilterClick = { showFilterModal = true },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            FilterChips(
                filters = topChipFilters,
                selectedFilter = selectedTopFilter,
                onFilterSelected = { filter ->
                    selectedTopFilter = filter
                    val newWorkModel = when (filter) {
                        "Remotas" -> "Remoto"
                        "Tempo Inteiro" -> "Tempo Inteiro"
                        else -> ""
                    }
                    val newArea = if (filter == "Tecnologia") "Tecnologia" else ""
                    discoverViewModel.applyFilters(
                        if (filter == "Todas") DiscoverFilters()
                        else currentFilters.copy(workModel = newWorkModel, area = newArea)
                    )
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(filteredOffers, key = { it.id }) { offer ->
            OfferCard(
                offer = offer,
                onClick = { onOfferClick(offer.id) },
            )
        }
    }

    if (showFilterModal) {
        FilterModal(
            currentFilters = currentFilters,
            onApply = { filters ->
                discoverViewModel.applyFilters(filters)
                showFilterModal = false
            },
            onDismiss = { showFilterModal = false },
        )
    }
}

// region Filter Modal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDropdown(
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

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
                focusedBorderColor = LightBlue,
                unfocusedBorderColor = BorderGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue,
            ),
            singleLine = true,
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
}

@Composable
fun FilterModal(
    currentFilters: DiscoverFilters,
    onApply: (DiscoverFilters) -> Unit,
    onDismiss: () -> Unit,
) {
    var area by remember { mutableStateOf(currentFilters.area) }
    var location by remember { mutableStateOf(currentFilters.location) }
    var workModel by remember { mutableStateOf(currentFilters.workModel) }
    var duration by remember { mutableStateOf(currentFilters.duration) }
    var deadline by remember { mutableStateOf(currentFilters.deadline) }

    val workModelOptions = listOf("Tempo Inteiro", "Tempo Parcial", "Remoto", "Híbrido")
    val durationOptions = listOf("3 Meses", "6 Meses", "9 Meses", "12 Meses", "+12 Meses")
    val deadlineOptions = listOf("1 Semana", "2 Semanas", "1 Mês", "3 Meses", "Sem limite")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Filtros",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = DarkBlue,
                        )
                    }
                }

                // Campo 1 — Área de atuação
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Área de atuação")
                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = BorderGrey,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        singleLine = true,
                    )
                }

                // Campo 2 — Localização
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Localização")
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = BorderGrey,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        singleLine = true,
                    )
                }

                // Campo 3 — Modelo de trabalho
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Modelo de trabalho")
                    FilterDropdown(
                        value = workModel,
                        placeholder = "Seleciona um modelo.",
                        options = workModelOptions,
                        onOptionSelected = { workModel = it },
                    )
                }

                // Campo 4 — Duração
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Duração")
                    FilterDropdown(
                        value = duration,
                        placeholder = "Seleciona uma duração.",
                        options = durationOptions,
                        onOptionSelected = { duration = it },
                    )
                }

                // Campo 5 — Data Limite
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Data Limite")
                    FilterDropdown(
                        value = deadline,
                        placeholder = "Seleciona uma duração.",
                        options = deadlineOptions,
                        onOptionSelected = { deadline = it },
                    )
                }

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
                        border = BorderStroke(1.dp, DarkBlue),
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            onApply(
                                DiscoverFilters(
                                    area = area,
                                    location = location,
                                    workModel = workModel,
                                    duration = duration,
                                    deadline = deadline,
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    ) {
                        Text("Filtrar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterModalPreview() {
    MaterialTheme {
        FilterModal(
            currentFilters = DiscoverFilters(),
            onApply = {},
            onDismiss = {},
        )
    }
}

// endregion

// region Components

@Composable
private fun SearchBarWithFilter(
    query: String,
    onQueryChange: (String) -> Unit,
    hasActiveFilters: Boolean,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Pesquisar...", color = DarkGrey) },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = DarkGrey,
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BorderGrey,
                focusedBorderColor = DarkBlue,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            ),
            singleLine = true,
        )

        BadgedBox(
            badge = {
                if (hasActiveFilters) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Red),
                    )
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkBlue)
                    .clickable { onFilterClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.FilterList,
                    contentDescription = "Filtros",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun FilterChips(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
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
                    enabled = true,
                    selected = isSelected,
                    borderColor = BorderGrey,
                    selectedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun OfferCard(
    offer: OfferItem,
    onClick: () -> Unit,
) {
    var isFav by remember { mutableStateOf(offer.isFavourite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(offer.logoColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = offer.logoInitial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = offer.company,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                IconButton(
                    onClick = { isFav = !isFav },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFav) "Remover favorito" else "Adicionar favorito",
                        tint = if (isFav) Red else DarkGrey,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Publicada ${offer.publishedAgo}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkGrey,
                )

                if (offer.duration.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "• ${offer.duration}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkGrey,
                    )
                }
                if (offer.location.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "• ${offer.location}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkGrey,
                    )
                }
            }
        }
    }
}

// endregion