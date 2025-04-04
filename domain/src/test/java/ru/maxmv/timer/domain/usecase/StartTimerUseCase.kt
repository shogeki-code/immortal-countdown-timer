package ru.maxmv.timer.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ru.maxmv.timer.domain.repository.MockTimerRepository

class StartTimerUseCaseTest {

    private lateinit var useCase: StartTimerUseCase
    private lateinit var mockRepo: MockTimerRepository

    @Before
    fun setup() {
        mockRepo = MockTimerRepository()
        useCase = StartTimerUseCase(mockRepo)
    }

    @Test
    fun `start sets timer state correctly`() = runTest {
        useCase(120_000)

        val result = mockRepo.observeTimer().first()
        assertEquals(120_000, result.remainingTimeMillis)
        assertEquals(true, result.isRunning)
    }
}