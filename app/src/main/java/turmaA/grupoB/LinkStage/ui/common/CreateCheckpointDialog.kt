package turmaA.grupoB.LinkStage.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import java.time.LocalDate

fun isValidDate(date: String): Boolean {
    val regex = Regex("""^\d{2}/\d{2}/\d{4}$""")
    if (!regex.matches(date)) return false
    return try {
        val parts = date.split("/")
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        if (month < 1 || month > 12 || day < 1 || year < 2000) return false
        LocalDate.of(year, month, day)
        true
    } catch (_: Exception) {
        false
    }
}

fun parseDate(dateText: String): LocalDate {
    val parts = dateText.split("/")
    return LocalDate.of(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
}

@Composable
fun CreateCheckpointDialog(
    onSave: (title: String, description: String, date: LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Criar Ponto de Entrega",
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = DarkBlue)
                    }
                }

                // Title field
                SectionLabel("Título")
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Relatório intercalar", color = DarkGrey, fontSize = 14.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    isError = titleError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (titleError) Red else LightBlue,
                        unfocusedBorderColor = if (titleError) Red else BorderGrey,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                )
                if (titleError) {
                    Text("Campo obrigatório.", color = Red, fontSize = 12.sp)
                }

                // Description field
                SectionLabel("Descrição")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Descreva o que deve ser entregue...", color = DarkGrey, fontSize = 14.sp) },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LightBlue,
                        unfocusedBorderColor = BorderGrey,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                )

                // Date field
                SectionLabel("Data limite")
                OutlinedTextField(
                    value = dateText,
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
                            dateText = formatted
                            dateError = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("dd/mm/aaaa", color = DarkGrey, fontSize = 14.sp) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = DarkGrey,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp),
                    isError = dateError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (dateError) Red else LightBlue,
                        unfocusedBorderColor = if (dateError) Red else BorderGrey,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                )
                if (dateError) {
                    Text("Data inválida. Use o formato dd/mm/aaaa.", color = Red, fontSize = 12.sp)
                }

                // Info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBlue.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, LightBlue.copy(alpha = 0.3f)),
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = LightBlue,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "O aluno será notificado e verá este ponto de entrega identificado como definido pelo orientador.",
                            color = DarkGrey,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Fade2),
                    ) {
                        Text("Cancelar", color = DarkBlue, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            var isValid = true
                            if (title.isBlank()) { titleError = true; isValid = false }
                            if (!isValidDate(dateText)) { dateError = true; isValid = false }
                            if (isValid) {
                                onSave(title, description, parseDate(dateText))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    ) {
                        Text("Criar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateCheckpointDialogPreview() {
    MaterialTheme {
        CreateCheckpointDialog(
            onSave = { _, _, _ -> },
            onDismiss = {},
        )
    }
}
