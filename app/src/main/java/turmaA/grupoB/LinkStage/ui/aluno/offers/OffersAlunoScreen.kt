package turmaA.grupoB.LinkStage.ui.aluno.offers

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BlueDark
import turmaA.grupoB.LinkStage.ui.theme.GrayBorder
import turmaA.grupoB.LinkStage.ui.theme.GrayDark
import turmaA.grupoB.LinkStage.ui.theme.RedAccent

// region Data models — replace with backend models later

data class OfferItem(
    val id: String,
    val title: String,
    val company: String,
    val type: String,
    val publishedAgo: String,
    val logoColor: Color,
    val logoInitial: String,
    val isFavourite: Boolean = false,
)

// endregion

// region Mock data — replace with ViewModel state

private val discoverFilters = listOf("Todas", "Remotas", "Tempo Inteiro", "Tecnologia")

private val sampleOffers = listOf(
    OfferItem("1", "Designer de Produto", "Continente – Tempo Inteiro", "Tempo Inteiro", "5h atrás", Color(0xFFE53935), "C"),
    OfferItem("2", "UI/UX Designer", "Viana S.T.Arts – Remoto", "Remoto", "2d atrás", Color(0xFF212121), "V", isFavourite = true),
    OfferItem("3", "Aprendiz de cozinha", "McDonald's – Tempo parcial", "Tempo Parcial", "5d atrás", Color(0xFFFFCC00), "M"),
    OfferItem("4", "Recepcionista", "B&B – Tempo Inteiro", "Tempo Inteiro", "1w atrás", Color(0xFF1565C0), "B"),
    OfferItem("5", "Dançarino", "Zé dos Cães – Noturno", "Noturno", "2w atrás", Color(0xFF37474F), "Z"),
)

// endregion

@Composable
fun OffersAlunoScreen(
    modifier: Modifier = Modifier,
    onOfferClick: (String) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredOffers = remember(selectedFilter, searchQuery) {
        sampleOffers.filter { offer ->
            val matchesFilter = selectedFilter == "Todas" ||
                offer.type.contains(selectedFilter, ignoreCase = true)
            val matchesSearch = searchQuery.isEmpty() ||
                offer.title.contains(searchQuery, ignoreCase = true) ||
                offer.company.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // Logo
        item {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "LINK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlueDark,
                    ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "STAGE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = BlueDark,
                    ),
                )
            }
        }

        // Title
        item {
            Text(
                text = "Descobre Oportunidades",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlueDark,
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
        }

        // Search bar + filter button
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Filter chips
        item {
            FilterChips(
                filters = discoverFilters,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Offer cards
        items(filteredOffers, key = { it.id }) { offer ->
            OfferCard(
                offer = offer,
                onClick = { onOfferClick(offer.id) },
            )
        }
    }
}

// region Components

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
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
            placeholder = { Text("Pesquisar...", color = GrayDark) },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = GrayDark,
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = GrayBorder,
                focusedBorderColor = BlueDark,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            ),
            singleLine = true,
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BlueDark)
                .clickable { },
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
                    selectedContainerColor = BlueDark,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = GrayDark,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = GrayBorder,
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
                        color = GrayDark,
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
                        tint = if (isFav) RedAccent else GrayDark,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = GrayDark,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Publicada ${offer.publishedAgo}",
                    style = MaterialTheme.typography.labelSmall,
                    color = GrayDark,
                )
            }
        }
    }
}

// endregion