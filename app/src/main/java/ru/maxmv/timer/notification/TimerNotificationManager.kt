package ru.maxmv.timer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.maxmv.timer.R

class TimerNotificationManager(
    private val context: Context
) {

    private val channelId = "timer_channel"

    init {
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Timer Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    fun getNotification(minutesLeft: Int): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Таймер")
            .setContentText("Осталось ${getMinutesText(minutesLeft)} до завершения")
            .setSmallIcon(R.drawable.ic_timer)
            .setOngoing(true)
            .build()
    }

    fun getFinishedNotification(): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Таймер завершён")
            .setContentText("Время вышло")
            .setSmallIcon(R.drawable.ic_timer)
            .setAutoCancel(true)
            .build()
    }

    private fun getMinutesText(n: Int): String {
        val absN = n % 100
        val lastDigit = absN % 10

        val word = when {
            absN in 11..14 -> "минут"
            lastDigit == 1 -> "минута"
            lastDigit in 2..4 -> "минуты"
            else -> "минут"
        }

        return "$n $word"
    }
}
