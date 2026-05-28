package turmaA.grupoB.LinkStage.ui.aluno.apply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.ApplyViewModel

val skillCategories = mapOf(
    "Tecnologia" to listOf("Python", "Java", "C++", "Kotlin", "React", "SQL", "IA", "Machine Learning"),
    "Gestão" to listOf("Gestão de Projetos", "Gestão de Orçamento", "Liderança", "Planeamento Estratégico"),
    "Comunicação" to listOf("Comunicação", "Escrita Persuasiva", "Apresentações", "Negociação"),
    "Interpessoal" to listOf("Trabalho em Grupo", "Resolução de Problemas", "Curiosidade Intelectual", "Adaptabilidade"),
    "Culinária" to listOf("Cozinha Portuguesa", "Pastelaria", "Gestão de Cozinha", "HACCP"),
    "Design" to listOf("Figma", "UI/UX", "Illustrator", "Photoshop"),
    "Mecânica" to listOf("Manutenção Industrial", "AutoCAD", "Soldadura", "Pneumática"),
    "Saúde" to listOf("Primeiros Socorros", "Cuidados de Saúde", "Anatomia", "Farmacologia"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditCvScreen(
    viewModel: ApplyViewModel,
    onBack: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddSkillDialog by remember { mutableStateOf(false) }
    var newSkillText by remember { mutableStateOf("") }

    if (showAddSkillDialog) {
        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            title = {
                Text("Adicionar Skill", fontWeight = FontWeight.Bold, color = DarkBlue)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Adiciona uma nova habilidade técnica ou interpessoal para o teu perfil.",
                        fontSize = 13.sp,
                        color = DarkGrey,
                    )
                    OutlinedTextField(
                        value = newSkillText,
                        onValueChange = { newSkillText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Skill") },
                        placeholder = { Text("Insere aqui a tua skill.", color = DarkGrey) },
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
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addSkill(newSkillText.trim())
                        newSkillText = ""
                        showAddSkillDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        newSkillText = ""
                        showAddSkillDialog = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
                ) {
                    Text("Cancelar")
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text("Editar CV", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar skills...", color = DarkGrey) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = DarkGrey) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LightBlue,
                    unfocusedBorderColor = BorderGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                singleLine = true,
            )

            // User skills
            val filteredUserSkills = if (searchQuery.isBlank()) viewModel.userSkills
            else viewModel.userSkills.filter { it.contains(searchQuery, ignoreCase = true) }

            if (filteredUserSkills.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("As suas habilidades", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 15.sp)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        filteredUserSkills.forEach { skill ->
                            FilterChip(
                                selected = true,
                                onClick = { viewModel.removeSkill(skill) },
                                label = { Text(skill, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LightBlue,
                                    selectedLabelColor = DarkBlue,
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = true,
                                    selectedBorderColor = Color.Transparent,
                                ),
                            )
                        }
                    }
                }
            }

            // Other skills
            Text("Outras habilidades", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 15.sp)

            if (searchQuery.isNotBlank()) {
                val allOtherSkills = skillCategories.values.flatten()
                    .filter { it !in viewModel.userSkills }
                    .filter { it.contains(searchQuery, ignoreCase = true) }

                if (allOtherSkills.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        allOtherSkills.forEach { skill ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.addSkill(skill) },
                                label = { Text(skill) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = BackgroundLight,
                                    labelColor = DarkGrey,
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = false,
                                    borderColor = BorderGrey,
                                ),
                            )
                        }
                    }
                } else {
                    Text("Sem resultados para \"$searchQuery\"", color = DarkGrey, fontSize = 13.sp)
                }
            } else {
                skillCategories.forEach { (category, skills) ->
                    val available = skills.filter { it !in viewModel.userSkills }
                    if (available.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(category, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = LightBlue)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                available.forEach { skill ->
                                    FilterChip(
                                        selected = false,
                                        onClick = { viewModel.addSkill(skill) },
                                        label = { Text(skill) },
                                        shape = RoundedCornerShape(20.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = BackgroundLight,
                                            labelColor = DarkGrey,
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = false,
                                            borderColor = BorderGrey,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Add custom skill button
            OutlinedButton(
                onClick = { showAddSkillDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBlue),
            ) {
                Text("Adicionar skill", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Save button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
        ) {
            Text("Guardar", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EditCvScreenPreview() {
    MaterialTheme {
        EditCvScreen(viewModel = ApplyViewModel(), onBack = {})
    }
}
