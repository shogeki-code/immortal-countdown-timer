package ru.maxmv.timer.domain.model

data class TimerState(
    val remainingTimeMillis: Long,
    val isRunning: Boolean
)
