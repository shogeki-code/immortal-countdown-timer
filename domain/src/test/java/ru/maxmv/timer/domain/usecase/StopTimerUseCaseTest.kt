package ru.maxmv.timer.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

import ru.maxmv.timer.domain.repository.MockTimerRepository

class StopTimerUseCaseTest {

    private lateinit var useCase: StopTimerUseCase
    private lateinit var mockRepo: MockTimerRepository

    @Before
    fun setup() {
        mockRepo = MockTimerRepository().apply { startTimer(60000) }
        useCase = StopTimerUseCase(mockRepo)
    }

    @Test
    fun `stop sets isRunning to false`() = runTest {
        useCase()

        val result = mockRepo.observeTimer().first()
        assertFalse(result.isRunning)
    }
}
