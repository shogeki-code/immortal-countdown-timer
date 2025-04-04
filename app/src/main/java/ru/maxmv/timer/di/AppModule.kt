package ru.maxmv.timer.di

import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import ru.maxmv.timer.data.repository.TimerRepositoryImpl
import ru.maxmv.timer.data.service.CountdownTimerManager
import ru.maxmv.timer.data.storage.TimerStorageImpl
import ru.maxmv.timer.domain.repository.TimerRepository
import ru.maxmv.timer.domain.storage.TimerStorage
import ru.maxmv.timer.domain.usecase.ObserveTimerUseCase
import ru.maxmv.timer.domain.usecase.ResetTimerUseCase
import ru.maxmv.timer.domain.usecase.StartTimerUseCase
import ru.maxmv.timer.domain.usecase.StopTimerUseCase
import ru.maxmv.timer.notification.TimerNotificationManager
import ru.maxmv.timer.service.TimerServiceLauncher

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun provideCountdownTimerManager(
        scope: CoroutineScope,
    ): CountdownTimerManager =
        CountdownTimerManager(scope)

    @Provides
    @Singleton
    fun provideTimerRepository(
        manager: CountdownTimerManager
    ): TimerRepository = TimerRepositoryImpl(manager)

    @Provides
    fun provideStartTimerUseCase(repository: TimerRepository): StartTimerUseCase =
        StartTimerUseCase(repository)

    @Provides
    fun provideStopTimerUseCase(repository: TimerRepository): StopTimerUseCase =
        StopTimerUseCase(repository)

    @Provides
    fun provideResetTimerUseCase(repository: TimerRepository): ResetTimerUseCase =
        ResetTimerUseCase(repository)

    @Provides
    fun provideObserveTimerUseCase(repository: TimerRepository): ObserveTimerUseCase =
        ObserveTimerUseCase(repository)

    @Provides
    @Singleton
    fun provideTimerStorage(@ApplicationContext context: Context): TimerStorage {
        return TimerStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideTimerNotificationManager(
        @ApplicationContext context: Context,
    ): TimerNotificationManager {
        return TimerNotificationManager(context)
    }

    @Provides
    @Singleton
    fun provideTimerServiceLauncher(
        @ApplicationContext context: Context,
        timerStorage: TimerStorage
    ): TimerServiceLauncher {
        return TimerServiceLauncher(context, timerStorage)
    }
}
