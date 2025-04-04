package ru.maxmv.timer.domain.repository

import kotlinx.coroutines.flow.Flow

import ru.maxmv.timer.domain.model.TimerState

interface TimerRepository {
    fun startTimer(durationMillis: Long)
    fun stopTimer()
    fun resetTimer()
    fun observeTimer(): Flow<TimerState>
}
