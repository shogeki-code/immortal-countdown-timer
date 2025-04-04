package ru.maxmv.timer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.maxmv.timer.ui.util.formatMillis


@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    onStartService: () -> Unit,
    onStopService: () -> Unit
) {
    val state by viewModel.timerState.collectAsState()

    var inputMinutes by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputMinutes,
            onValueChange = { inputMinutes = it },
            label = { Text("Минуты") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(
            text = formatMillis(state.remainingTimeMillis),
            style = MaterialTheme.typography.headlineLarge
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(enabled = !state.isRunning, onClick = {
                viewModel.start((inputMinutes.toLongOrNull() ?: 1L) * 60_000)
                onStartService()
            }) {
                Text("Старт")
            }

            Button(onClick = {
                onStopService()
                viewModel.stop()
            }) {
                Text("Стоп")
            }

            Button(onClick = {
                onStopService()
                viewModel.reset()
            }) {
                Text("Сброс")
            }
        }
    }
}
