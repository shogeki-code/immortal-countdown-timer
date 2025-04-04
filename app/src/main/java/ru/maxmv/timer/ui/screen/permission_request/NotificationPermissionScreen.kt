package ru.maxmv.timer.ui.screen.permission_request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationPermissionScreen(
    onRequestPermission: () -> Unit,
    onGoToSettings: () -> Unit,
    permanentlyDenied: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Разрешите отправку уведомлений для корректной работы таймера.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (permanentlyDenied) {
            Button(onClick = onGoToSettings) {
                Text("Открыть настройки")
            }
        } else {
            Button(onClick = onRequestPermission) {
                Text("Разрешить")
            }
        }
    }
}
