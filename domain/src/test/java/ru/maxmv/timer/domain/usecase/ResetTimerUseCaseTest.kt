package ru.maxmv.timer.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ru.maxmv.timer.domain.repository.MockTimerRepository

class ResetTimerUseCaseTest {

    private lateinit var useCase: ResetTimerUseCase
    private lateinit var mockRepo: MockTimerRepository

    @Before
    fun setup() {
        mockRepo = MockTimerRepository().apply { startTimer(30000) }
        useCase = ResetTimerUseCase(mockRepo)
    }

    @Test
    fun `reset sets remaining time to 0 and isRunning to false`() = runTest {
        useCase()

        val result = mockRepo.observeTimer().first()
        assertEquals(0L, result.remainingTimeMillis)
        assertEquals(false, result.isRunning)
    }
}
