package ru.maxmv.timer.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

import ru.maxmv.timer.domain.storage.TimerStorage
import ru.maxmv.timer.domain.usecase.ObserveTimerUseCase
import ru.maxmv.timer.notification.TimerNotificationManager

import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService : Service() {

    @Inject
    lateinit var observeTimerUseCase: ObserveTimerUseCase

    @Inject
    lateinit var timerStorage: TimerStorage

    @Inject
    lateinit var timerNotificationManager: TimerNotificationManager

    private var serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            1,
            timerNotificationManager.getNotification(timerStorage.getRemainingMinutes().toInt())
        )

        serviceScope.launch {
            try {
                withTimeoutOrNull(5_000L) {
                    observeTimerUseCase().collect {
                        if (timerStorage.getEndTime() - System.currentTimeMillis() <= 0) {
                            val doneNotification =
                                timerNotificationManager.getFinishedNotification()
                            val manager =
                                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                            manager.notify(2, doneNotification)
                            vibrate(this@TimerForegroundService)
                            stopSelf()
                        } else {
                            val notification =
                                timerNotificationManager.getNotification(
                                    timerStorage.getRemainingMinutes().toInt()
                                )
                            val manager =
                                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                            manager.notify(1, notification)
                        }
                    }
                } ?: startFallbackTimer()
            } catch (e: Exception) {
                startFallbackTimer()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startFallbackTimer() {
        serviceScope.launch {
            while (isActive) {
                val minutesLeft = timerStorage.getRemainingMinutes()
                val millsLeft = timerStorage.getEndTime() - System.currentTimeMillis()
                val notification = if (millsLeft > 0L) {
                    timerNotificationManager.getNotification(minutesLeft.toInt())
                } else {
                    timerNotificationManager.getFinishedNotification()
                }
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(1, notification)
                if (millsLeft <= 0) {
                    vibrate(this@TimerForegroundService)
                    stopSelf()
                    break
                }
                delay(60_000L)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        } else {
            null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(500)
        }
    }


    companion object {
        private const val EXTRA_REMAINING_MINUTES = "EXTRA_REMAINING_MINUTES"

        fun createIntent(context: Context, remainingMinutes: Long): Intent {
            return Intent(context, TimerForegroundService::class.java).apply {
                putExtra(EXTRA_REMAINING_MINUTES, remainingMinutes)
            }
        }
    }
}
