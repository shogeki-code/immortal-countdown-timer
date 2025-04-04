package ru.maxmv.timer.data.storage

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.maxmv.timer.domain.storage.TimerStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerStorage {
    private val prefs = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)

    override fun saveEndTime(endTime: Long) {
        prefs.edit { putLong("timer_end_time", endTime) }
    }

    override fun clearEndTime() {
        prefs.edit { remove("timer_end_time") }
    }

    override fun getEndTime(): Long = prefs.getLong("timer_end_time", 0L)

    override fun getRemainingMinutes(): Long = (getEndTime() - System.currentTimeMillis()) / 60000

}
