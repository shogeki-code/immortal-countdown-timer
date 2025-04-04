package ru.maxmv.timer.data.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CountdownTimerManager(
    private val scope: CoroutineScope
) {
    private val _remainingTime = MutableStateFlow(0L)
    val remainingTime: StateFlow<Long> = _remainingTime

    private var duration = 0L
    private var startTime = 0L
    private var job: Job? = null

    fun start(durationMillis: Long) {
        duration = durationMillis
        startTime = System.currentTimeMillis()
        job?.cancel()

        _remainingTime.value = durationMillis

        job = scope.launch {
            while (isActive && _remainingTime.value > 0) {
                val elapsed = System.currentTimeMillis() - startTime
                val remainingTime = (duration - elapsed).coerceAtLeast(0)
                _remainingTime.value = remainingTime
                delay(1000L)
            }
        }
    }


    fun stop() {
        job?.cancel()
    }

    fun reset() {
        job?.cancel()
        _remainingTime.value = 0L
    }
}
