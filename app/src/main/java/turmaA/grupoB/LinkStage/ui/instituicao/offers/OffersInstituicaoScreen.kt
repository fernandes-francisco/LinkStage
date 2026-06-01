package turmaA.grupoB.LinkStage.ui.instituicao.offers

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferItem
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

private val topChipFilters = listOf("Todas", "Remotas", "Tempo Inteiro", "Tecnologia")

private val sampleOffers = listOf(
    OfferItem("1", "Designer de Produto", "ESTG-IPVC", "Tempo Inteiro", "5h atras", Color(0xFF1565C0), "E", duration = "6 Meses", area = "Design", location = "Porto"),
    OfferItem("2", "UI/UX Designer", "ESTG-IPVC", "Remoto", "2d atras", Color(0xFF1565C0), "E", duration = "3 Meses", area = "Design", location = "Remoto"),
    OfferItem("3", "Programador Full-Stack", "ESTG-IPVC", "Remoto", "1w atras", Color(0xFF1565C0), "E", duration = "9 Meses", area = "Tecnologia", location = "Remoto"),
    OfferItem("4", "Recepcionista", "ESTG-IPVC", "Tempo Inteiro", "2w atras", Color(0xFF1565C0), "E", duration = "6 Meses", area = "Hotelaria", location = "Braga"),
)

@Composable
fun OffersInstituicaoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedTopFilter by rememberSaveable { mutableStateOf("Todas") }
    var offerToDelete by remember { mutableStateOf<OfferItem?>(null) }

    val filteredOffers = sampleOffers.filter { offer ->
        val matchesSearch = searchQuery.isEmpty() ||
            offer.title.contains(searchQuery, ignoreCase = true) ||
            offer.company.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedTopFilter) {
            "Remotas" -> offer.type.contains("Remoto", ignoreCase = true)
            "Tempo Inteiro" -> offer.type.contains("Tempo Inteiro", ignoreCase = true)
            "Tecnologia" -> offer.area.contains("Tecnologia", ignoreCase = true)
            else -> true
        }

        matchesSearch && matchesFilter
    }

    if (offerToDelete != null) {
        DeleteOfferDialog(
            offerTitle = offerToDelete!!.title,
            onConfirm = { offerToDelete = null },
            onDismiss = { offerToDelete = null },
        )
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(InstituicaoRoutes.offerFormRoute("new")) },
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar oferta")
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item { CommonTopBar() }

            item {
                Text(
                    text = "Gestão de Ofertas",
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
                    onQueryChange = { searchQuery = it },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                FilterChips(
                    filters = topChipFilters,
                    selectedFilter = selectedTopFilter,
                    onFilterSelected = { selectedTopFilter = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(filteredOffers, key = { it.id }) { offer ->
                InstitutionOfferCard(
                    offer = offer,
                    onClick = { navController.navigate(InstituicaoRoutes.offerDetailRoute(offer.id)) },
                    onEdit = { navController.navigate(InstituicaoRoutes.offerFormRoute(offer.id)) },
                    onDelete = { offerToDelete = offer },
                )
            }
        }
    }
}

@Composable
private fun SearchBarWithFilter(
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
            placeholder = { Text("Pesquisar...", color = DarkGrey) },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = DarkGrey)
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

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkBlue)
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
private fun InstitutionOfferCard(
    offer: OfferItem,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
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
                        fontSize = 18.sp,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.title,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = offer.company,
                        color = DarkGrey,
                        fontSize = 13.sp,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = LightBlue,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Apagar",
                            tint = Red,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            HorizontalDivider(
                color = BorderGrey,
                modifier = Modifier.padding(top = 8.dp),
            )

            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Publicada ${offer.publishedAgo}",
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
private fun DeleteOfferDialog(
    offerTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    LinkStageDialog(
        title = "Apagar Oferta?",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmText = "Apagar",
        dismissText = "Cancelar",
    ) {
        Text(
            text = "Tem a certeza que pretende apagar a oferta \"$offerTitle\"? Esta ação não pode ser desfeita.",
            color = DarkGrey,
            fontSize = 14.sp,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OffersInstituicaoScreenPreview() {
    MaterialTheme {
        OffersInstituicaoScreen(navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun OffersInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        OffersInstituicaoScreen(navController = rememberNavController())
    }
}
