package ru.maxmv.timer.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

import dagger.hilt.android.AndroidEntryPoint

import ru.maxmv.timer.service.TimerForegroundService
import ru.maxmv.timer.ui.screen.TimerScreen
import ru.maxmv.timer.ui.screen.permission_request.NotificationPermissionScreen
import ru.maxmv.timer.ui.theme.ImmortalTimerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Settings.canDrawOverlays(applicationContext)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }

        setContent {
            val context = LocalContext.current
            val permissionGranted = remember { mutableStateOf(checkNotificationPermission(context)) }
            var attempts by remember { mutableStateOf(0) }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                permissionGranted.value = granted
                if (!granted) attempts++
            }

            val settingsLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) {
                permissionGranted.value = checkNotificationPermission(context)
            }

            ImmortalTimerTheme {
                if (permissionGranted.value) {
                    TimerScreen(
                        onStartService = ::startTimerService,
                        onStopService = ::stopTimerService
                    )
                } else {
                    NotificationPermissionScreen(
                        onRequestPermission = {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        },
                        onGoToSettings = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            settingsLauncher.launch(intent)
                        },
                        permanentlyDenied = attempts >= 2
                    )
                }
            }
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun startTimerService() {
        val intent = Intent(this, TimerForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun stopTimerService() {
        val intent = Intent(this, TimerForegroundService::class.java)
        stopService(intent)
    }
}
