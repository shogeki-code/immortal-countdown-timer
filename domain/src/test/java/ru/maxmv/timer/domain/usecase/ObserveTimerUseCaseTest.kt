package ru.maxmv.timer.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ru.maxmv.timer.domain.repository.MockTimerRepository

class ObserveTimerUseCaseTest {

    private lateinit var useCase: ObserveTimerUseCase
    private lateinit var mockRepo: MockTimerRepository

    @Before
    fun setup() {
        mockRepo = MockTimerRepository().apply { startTimer(90000) }
        useCase = ObserveTimerUseCase(mockRepo)
    }

    @Test
    fun `observe returns correct timer state`() = runTest {
        val result = useCase().first()
        assertEquals(90000L, result.remainingTimeMillis)
        assertEquals(true, result.isRunning)
    }
}
