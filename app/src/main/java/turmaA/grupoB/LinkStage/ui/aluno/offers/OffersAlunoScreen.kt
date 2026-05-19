package turmaA.grupoB.LinkStage.ui.aluno.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
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
)

data class DiscoverFilters(
    val areas: List<String> = emptyList(),
    val location: String = "",
    val workModel: String? = null,
    val duration: String? = null,
)

// endregion

private val workModelOptions = listOf("Remoto", "Tempo Inteiro", "Tempo Parcial")
private val durationOptions = listOf("3 meses", "6 meses", "9 meses", "12 meses")
private val topChipFilters = listOf("Todas", "Remotas", "Tempo Inteiro", "Tecnologia")

@OptIn(ExperimentalMaterial3Api::class)
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

    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedTopFilter by remember { mutableStateOf("Todas") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "LINK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "STAGE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = DarkBlue,
                    ),
                )
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
                onFilterClick = { showFilterSheet = true },
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
                        "Tecnologia" -> null
                        else -> null
                    }
                    val newAreas = if (filter == "Tecnologia") listOf("Tecnologia") else currentFilters.areas
                    discoverViewModel.applyFilters(
                        currentFilters.copy(
                            workModel = newWorkModel,
                            areas = if (filter == "Todas") emptyList() else newAreas,
                        )
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

    if (showFilterSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            currentFilters = currentFilters,
            availableAreas = discoverViewModel.availableAreas,
            onDismiss = { showFilterSheet = false },
            onApply = { filters ->
                discoverViewModel.applyFilters(filters)
                showFilterSheet = false
            },
            onClear = {
                discoverViewModel.clearFilters()
                selectedTopFilter = "Todas"
                showFilterSheet = false
            },
        )
    }
}

// region Filter Bottom Sheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    sheetState: androidx.compose.material3.SheetState,
    currentFilters: DiscoverFilters,
    availableAreas: List<String>,
    onDismiss: () -> Unit,
    onApply: (DiscoverFilters) -> Unit,
    onClear: () -> Unit,
) {
    var selectedAreas by remember { mutableStateOf(currentFilters.areas) }
    var locationText by remember { mutableStateOf(currentFilters.location) }
    var selectedWorkModel by remember { mutableStateOf(currentFilters.workModel) }
    var selectedDuration by remember { mutableStateOf(currentFilters.duration) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Filtros",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    ),
                )
                TextButton(onClick = onClear) {
                    Text("Limpar", color = Red)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Area filter (multi-select)
            Text(
                "Área",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                availableAreas.forEach { area ->
                    val isSelected = area in selectedAreas
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedAreas = if (isSelected) selectedAreas - area
                            else selectedAreas + area
                        },
                        label = { Text(area) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BackgroundLight,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Location filter (text field)
            Text(
                "Localização",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = locationText,
                onValueChange = { locationText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: Porto, Remoto...", color = DarkGrey) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderGrey,
                    focusedBorderColor = DarkBlue,
                    unfocusedContainerColor = BackgroundLight,
                    focusedContainerColor = BackgroundLight,
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Work model filter (single-select)
            Text(
                "Modelo de Trabalho",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                workModelOptions.forEach { model ->
                    val isSelected = model == selectedWorkModel
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedWorkModel = if (isSelected) null else model
                        },
                        label = { Text(model) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BackgroundLight,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Duration filter (single-select)
            Text(
                "Duração",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                durationOptions.forEach { duration ->
                    val isSelected = duration == selectedDuration
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedDuration = if (isSelected) null else duration
                        },
                        label = { Text(duration) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BackgroundLight,
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

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    onApply(
                        DiscoverFilters(
                            areas = selectedAreas,
                            location = locationText,
                            workModel = selectedWorkModel,
                            duration = selectedDuration,
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text(
                    "Aplicar Filtros",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }
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