package ru.maxmv.timer.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.maxmv.timer.domain.model.TimerState

class MockTimerRepository : TimerRepository {

    private val timerFlow = MutableStateFlow(TimerState(0, false))

    override fun startTimer(durationMillis: Long) {
        timerFlow.value = TimerState(durationMillis, true)
    }

    override fun stopTimer() {
        timerFlow.value = timerFlow.value.copy(isRunning = false)
    }

    override fun resetTimer() {
        timerFlow.value = TimerState(0, false)
    }

    override fun observeTimer(): Flow<TimerState> = timerFlow
}
