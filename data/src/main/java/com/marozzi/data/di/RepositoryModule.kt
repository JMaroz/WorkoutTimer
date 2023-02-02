package com.marozzi.data.di

import com.marozzi.data.timer.WorkoutTimerImpl
import com.marozzi.domain.timer.WorkoutTimer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTimerWorker(timerWorkerImpl: WorkoutTimerImpl): WorkoutTimer

}