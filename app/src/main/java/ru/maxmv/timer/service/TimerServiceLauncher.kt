package ru.maxmv.timer.service

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.maxmv.timer.domain.storage.TimerStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerServiceLauncher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerStorage: TimerStorage
) {

    fun launch() {
        val intent = TimerForegroundService.createIntent(context, timerStorage.getRemainingMinutes())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
