package turmaA.grupoB.LinkStage.ui.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.ui.common.LinkStageButton
import turmaA.grupoB.LinkStage.ui.common.LinkStageOutlinedButton
import turmaA.grupoB.LinkStage.ui.common.SuccessDialog
import turmaA.grupoB.LinkStage.ui.theme.*

@Composable
fun RegisterSkillsScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: (List<String>) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val mySkills = remember { mutableStateListOf<String>() }
    val otherSkills = remember { 
        mutableStateListOf(
            "Python", "Trabalho em grupo", "Gestão de Projetos", "IA",
            "C++", "Curiosidade intelectual", "Gestão de orçamento", 
            "Java", "Resolução de problemas", "Comunicação", "Escrita persuasiva"
        ) 
    }
    
    var showAddSkillDialog by remember { mutableStateOf(false) }
    var newSkillName by remember { mutableStateOf("") }
    
    var showSuccessDialog by remember { mutableStateOf(false) }

    SuccessDialog(
        message = "A sua conta foi criada com sucesso. Pode agora iniciar sessão.",
        show = showSuccessDialog,
        onConfirm = { onRegisterClick(mySkills.toList()) }
    )

    if (showAddSkillDialog) {
        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            confirmButton = {},
            title = {
                Column {
                    Text(
                        text = "Adicionar Skill",
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Adiciona uma nova habilidade técnica ou interpessoal para o teu perfil.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            text = {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skill",
                        color = DarkBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        value = newSkillName,
                        onValueChange = { newSkillName = it },
                        placeholder = { Text("Insere aqui a tua skill.", color = Color.Gray, fontSize = 14.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE0E0E0),
                            unfocusedContainerColor = Color(0xFFE0E0E0),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showAddSkillDialog = false }) {
                            Text("Cancelar", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        LinkStageButton(
                            text = "Guardar",
                            onClick = {
                                if (newSkillName.isNotBlank()) {
                                    mySkills.add(newSkillName.trim())
                                    newSkillName = ""
                                    showAddSkillDialog = false
                                }
                            },
                            modifier = Modifier.width(120.dp),
                            height = 40.dp
                        )
                    }
                }
            },
            containerColor = Color(0xFFF5F5F5),
            shape = RoundedCornerShape(16.dp)
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_link_stage2),
                contentDescription = "LinkStage Logo",
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Passo 2 de 2 - Definir skills",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Pesquisar skills", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD9D9D9).copy(alpha = 0.5f),
                    unfocusedContainerColor = Color(0xFFD9D9D9).copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // My Skills
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "As suas skills",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (mySkills.isEmpty()) {
                    Text(
                        text = "Ainda não adicionaste nenhuma skill.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mySkills.forEach { skill ->
                            SkillChip(
                                text = skill,
                                isSelected = true,
                                onClick = {
                                    mySkills.remove(skill)
                                    // Only add back to otherSkills if it was originally there or a standard skill
                                    // For simplicity, we can just add it back
                                    if (!otherSkills.contains(skill)) {
                                        otherSkills.add(skill)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Other Skills
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sugestões de skills",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    otherSkills.filter { it.contains(searchQuery, ignoreCase = true) }.forEach { skill ->
                        SkillChip(
                            text = skill,
                            isSelected = false,
                            onClick = {
                                otherSkills.remove(skill)
                                mySkills.add(skill)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { showAddSkillDialog = true },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, DarkBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue)
            ) {
                Text("Adicionar skill personalizada", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Buttons
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                LinkStageButton(
                    text = "Registar",
                    onClick = { showSuccessDialog = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinkStageOutlinedButton(
                    text = "Voltar",
                    onClick = onBackClick
                )
            }
        }
    }
}

@Composable
fun SkillChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) LightBlue.copy(alpha = 0.4f) else Color(0xFFD9D9D9).copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                color = if (isSelected) LightBlue else Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) DarkBlue else Color.Gray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterSkillsScreenPreview() {
    LinkStageTheme {
        RegisterSkillsScreen()
    }
}
