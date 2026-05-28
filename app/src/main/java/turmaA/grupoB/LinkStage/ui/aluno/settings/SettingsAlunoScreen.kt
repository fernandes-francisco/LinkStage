package turmaA.grupoB.LinkStage.ui.aluno.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageButton
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.common.ValidationItem
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import turmaA.grupoB.LinkStage.viewmodel.SettingsViewModel

// region Data models

data class LoggedUser(
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
)

// endregion

@Composable
fun SettingsAlunoScreen(
    onLogout: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    settingsViewModel: SettingsViewModel = viewModel(),
) {
    val user by settingsViewModel.user.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                showLogoutDialog = false
                settingsViewModel.logout()
                onLogout()
            },
            onDismiss = { showLogoutDialog = false },
        )
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { newPassword ->
                // Lógica de update
                showPasswordDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState()),
    ) {
        CommonTopBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Configurações",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile card
        SettingsCard(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(user = user, size = 48)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                        ),
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkGrey,
                    )
                }
            }

            HorizontalDivider(color = BorderGrey)

            SettingsRowItem(
                label = "Políticas de Privacidade",
                onClick = {
                    uriHandler.openUri("https://www.google.com") // Substituir pelo link real
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings section
        SettingsSectionHeader(title = "Configurações")

        SettingsCard(modifier = Modifier.padding(horizontal = 20.dp)) {
            SettingsRowItem(
                icon = Icons.Outlined.Settings,
                label = "Alterar Palavra-Passe",
                onClick = { showPasswordDialog = true },
            )

            HorizontalDivider(
                color = BorderGrey,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            SettingsRowItem(
                icon = Icons.Outlined.Settings,
                label = "Notificações",
                onClick = onNotificationsClick,
            )

            HorizontalDivider(
                color = BorderGrey,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            // Language toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Idioma",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f),
                )
                LanguageToggle(
                    selectedLang = currentLanguage,
                    onSelect = { settingsViewModel.changeLanguage(it) },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App version section
        SettingsSectionHeader(title = "Versão da APP")

        SettingsCard(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "V1.0.0 - Android 36 / Kotlin / Supabase",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGrey,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout button
        LinkStageButton(
            text = "Terminar Sessão",
            onClick = { showLogoutDialog = true },
            modifier = Modifier.padding(horizontal = 20.dp),
            height = 52.dp,
            brush = SolidColor(Red)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun UserAvatar(user: LoggedUser, size: Int = 48) {
    val initials = user.name
        .split(" ")
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(brush = Fade1),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size / 3).sp,
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(brush = Fade1),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = LightBlue,
            ),
        )
    }
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        content = content,
    )
}

@Composable
private fun SettingsRowItem(
    label: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = DarkBlue,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = DarkGrey,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun LanguageToggle(
    selectedLang: String,
    onSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, BorderGrey, RoundedCornerShape(8.dp)),
    ) {
        listOf("PT", "EN").forEach { lang ->
            val isSelected = lang == selectedLang
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (isSelected) DarkBlue else Color.Transparent)
                    .clickable { onSelect(lang) }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = lang,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    ),
                    color = if (isSelected) Color.White else DarkGrey,
                )
            }
        }
    }
}

@Composable
private fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    LinkStageDialog(
        title = "Terminar Sessão",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmText = "Terminar",
        dismissText = "Cancelar",
        content = {
            Text(
                text = "Tens a certeza que queres terminar sessão?",
                color = DarkGrey,
            )
        }
    )
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val hasNumber by remember { derivedStateOf { password.any { it.isDigit() } } }
    val hasUpperAndLower by remember {
        derivedStateOf {
            password.any { it.isUpperCase() } && password.any { it.isLowerCase() }
        }
    }
    val hasMinLength by remember { derivedStateOf { password.length >= 8 } }
    val isPasswordValid by remember { derivedStateOf { hasNumber && hasUpperAndLower && hasMinLength } }
    val isConfirmValid by remember { derivedStateOf { confirmPassword == password && confirmPassword.isNotEmpty() } }

    val isEnabled = isPasswordValid && isConfirmValid

    LinkStageDialog(
        onDismiss = onDismiss,
        title = "Alterar Palavra-passe",
        onConfirm = { onConfirm(password) },
        confirmText = "Atualizar",
        dismissText = "Cancelar",
        confirmEnabled = isEnabled,
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column {
                    Text("Nova Palavra-passe", style = MaterialTheme.typography.labelMedium, color = DarkGrey)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        singleLine = true
                    )
                }

                Column {
                    Text("Confirmar Palavra-passe", style = MaterialTheme.typography.labelMedium, color = DarkGrey)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        singleLine = true,
                        isError = confirmPassword.isNotEmpty() && !isConfirmValid
                    )
                }

                if (password.isNotEmpty()) {
                    Column {
                        ValidationItem(text = "Incluir um número", isValid = hasNumber)
                        ValidationItem(text = "Incluir maiúsculas e minúsculas", isValid = hasUpperAndLower)
                        ValidationItem(text = "Mínimo 8 caracteres", isValid = hasMinLength)
                    }
                }
            }
        }
    )
}

// endregion

@Preview(showSystemUi = true)
@Composable
fun SettingsAlunoScreenPreview() {
    MaterialTheme {
        SettingsAlunoScreen()
    }
}
