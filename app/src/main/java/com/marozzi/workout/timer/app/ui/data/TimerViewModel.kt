package com.marozzi.workout.timer.app.ui.data

import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration


interface TimerViewModel {

    /**
     * Flow to collect and manipulate when the user interact with the Timer Page
     */
    val workoutTimerWithReps: StateFlow<List<Timer>>

    /**
     * When user change the timers
     */
    val workoutTimerTotalRepsAndDuration: StateFlow<Pair<Int, Duration>>

    /**
     * Flow to listen when the timer is running or not
     */
    val isRunning: StateFlow<Boolean>

    /**
     * Flow to catch the current timer
     */
    val currentTimer : StateFlow<CurrentTimer>

    val selectedWorkTime : Timer.Work

    val selectedRestTime : Timer.Rest

    val selectedRepsCount : Int

    fun resetTimer()

    fun addOrRemoveWorkTime(duration: Duration, add: Boolean)

    fun replaceWorkTime(value: Duration)

    fun addOrRemoveRestTime(duration: Duration, add: Boolean)

    fun replaceRestTime(value: Duration)

    fun addOrRemoveRounds(add: Boolean)

    fun startTimer()

    fun stopTimer()

    fun startOrPauseTimer()

    fun toggleBluetoothPanel()

    fun toggleVolumePanel()

    fun setStandardTimer(value: Triple<String, Timer.Work, Timer.Rest>)
}