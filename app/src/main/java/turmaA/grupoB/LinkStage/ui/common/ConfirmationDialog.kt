package turmaA.grupoB.LinkStage.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Red

@Composable
fun ConfirmationDialog(
    title: String,
    body: String,
    confirmLabel: String = "Confirmar",
    confirmColor: Color = DarkBlue,
    isDanger: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (isDanger) Red else DarkBlue,
                fontSize = 18.sp,
            )
        },
        text = {
            Text(
                text = body,
                color = DarkGrey,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDanger) Red else confirmColor,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(confirmLabel, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text("Cancelar", fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
    )
}
