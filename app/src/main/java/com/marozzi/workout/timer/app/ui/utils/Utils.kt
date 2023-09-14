package com.marozzi.workout.timer.app.ui.utils

import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import com.marozzi.workout.timer.app.ui.data.TimerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal val _fakeTimerViewModel = object : TimerViewModel {

    override val workoutTimerWithReps: StateFlow<List<Timer>> = MutableStateFlow(emptyList())
    override val workoutTimerTotalRepsAndDuration: StateFlow<Pair<Int, Duration>> = MutableStateFlow(
        Pair(0, 0.seconds)
    )
    override val isRunning: StateFlow<Boolean> = MutableStateFlow(false)
    override val currentTimer: StateFlow<CurrentTimer> = MutableStateFlow(CurrentTimer())
    override val selectedWorkTime: Timer.Work = Timer.Work(0.seconds)
    override val selectedRestTime: Timer.Rest = Timer.Rest(0.seconds)
    override val selectedRepsCount: Int = 0

    override fun resetTimer() {
    }

    override fun addOrRemoveWorkTime(duration: Duration, add: Boolean) {
    }

    override fun replaceWorkTime(value: Duration) {
    }

    override fun addOrRemoveRestTime(duration: Duration, add: Boolean) {
    }

    override fun replaceRestTime(value: Duration) {
    }

    override fun addOrRemoveRounds(add: Boolean) {
    }

    override fun startTimer() {
    }

    override fun stopTimer() {
    }

    override fun startOrPauseTimer() {
    }

    override fun toggleBluetoothPanel() {
    }

    override fun toggleVolumePanel() {
    }

    override fun setStandardTimer(value: Triple<String, Timer.Work, Timer.Rest>) {
    }
}