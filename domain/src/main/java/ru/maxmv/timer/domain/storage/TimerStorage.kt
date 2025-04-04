package ru.maxmv.timer.domain.storage

interface TimerStorage {
    fun saveEndTime(endTime: Long)
    fun clearEndTime()
    fun getEndTime(): Long
    fun getRemainingMinutes(): Long
}
