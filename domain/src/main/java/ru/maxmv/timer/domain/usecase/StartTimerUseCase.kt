package ru.maxmv.timer.domain.usecase

import ru.maxmv.timer.domain.repository.TimerRepository

class StartTimerUseCase(private val repository: TimerRepository) {
    operator fun invoke(durationMillis: Long) {
        repository.startTimer(durationMillis)
    }
}
