package ru.maxmv.timer.domain.usecase

import ru.maxmv.timer.domain.repository.TimerRepository

class ResetTimerUseCase(private val repository: TimerRepository) {
    operator fun invoke() {
        repository.resetTimer()
    }
}
