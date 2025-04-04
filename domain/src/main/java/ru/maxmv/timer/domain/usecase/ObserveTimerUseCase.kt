package ru.maxmv.timer.domain.usecase

import kotlinx.coroutines.flow.Flow

import ru.maxmv.timer.domain.model.TimerState
import ru.maxmv.timer.domain.repository.TimerRepository

class ObserveTimerUseCase(private val repository: TimerRepository) {
    operator fun invoke(): Flow<TimerState> = repository.observeTimer()
}
