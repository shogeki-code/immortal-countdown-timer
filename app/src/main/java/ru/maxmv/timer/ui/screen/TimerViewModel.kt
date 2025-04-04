package ru.maxmv.timer.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.maxmv.timer.domain.model.TimerState
import ru.maxmv.timer.domain.storage.TimerStorage
import ru.maxmv.timer.domain.usecase.ObserveTimerUseCase
import ru.maxmv.timer.domain.usecase.ResetTimerUseCase
import ru.maxmv.timer.domain.usecase.StartTimerUseCase
import ru.maxmv.timer.domain.usecase.StopTimerUseCase
import ru.maxmv.timer.service.TimerServiceLauncher
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val startTimerUseCase: StartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val resetTimerUseCase: ResetTimerUseCase,
    private val observeTimerUseCase: ObserveTimerUseCase,
    private val timerStorage: TimerStorage,
    private val timerServiceLauncher: TimerServiceLauncher
) : ViewModel() {

    private val _timerState = MutableStateFlow(TimerState(0, false))
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    init {
        viewModelScope.launch {
            observeTimerUseCase().collect {
                _timerState.value = it
            }
        }

        viewModelScope.launch {
            val endTime = timerStorage.getEndTime()
            val remaining = (endTime - System.currentTimeMillis()).coerceAtLeast(0L)
            if (remaining > 0L) {
                startTimerUseCase(remaining)
            }
        }
    }

    fun start(durationMillis: Long) {
        startTimerUseCase(durationMillis)
        timerServiceLauncher.launch()
        timerStorage.saveEndTime(System.currentTimeMillis() + durationMillis)
    }

    fun stop() {
        stopTimerUseCase()
        timerStorage.clearEndTime()
    }

    fun reset() {
        resetTimerUseCase()
        timerStorage.clearEndTime()
    }
}

