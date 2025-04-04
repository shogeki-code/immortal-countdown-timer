package ru.maxmv.timer.domain.usecase

import ru.maxmv.timer.domain.repository.TimerRepository

class StopTimerUseCase(private val repository: TimerRepository) {
    operator fun invoke() {
        repository.stopTimer()
    }
}
