package ru.maxmv.timer.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import ru.maxmv.timer.data.service.CountdownTimerManager
import ru.maxmv.timer.domain.model.TimerState
import ru.maxmv.timer.domain.repository.TimerRepository

class TimerRepositoryImpl(
    private val timerManager: CountdownTimerManager
) : TimerRepository {

    override fun startTimer(durationMillis: Long) {
        timerManager.start(durationMillis)
    }

    override fun stopTimer() {
        timerManager.stop()
    }

    override fun resetTimer() {
        timerManager.reset()
    }

    override fun observeTimer(): Flow<TimerState> =
        timerManager.remainingTime.map { millis ->
            TimerState(remainingTimeMillis = millis, isRunning = millis > 0)
        }
}
