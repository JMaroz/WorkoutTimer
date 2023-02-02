package com.marozzi.domain.timer

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Sealed interface for distinguish type of Timer to run
 */
sealed interface Timer {

    val duration: Duration

    data class Work(override val duration: Duration) : Timer

    data class Rest(override val duration: Duration) : Timer

    data class Prepare(override val duration: Duration = 3.seconds) : Timer

}

/**
 * List of common timer used into gym
 */
val presetTimer: List<Triple<String, Timer.Work, Timer.Rest>> = listOf(
    Triple("Custom", Timer.Work(45.seconds), Timer.Rest(15.seconds)),
    Triple("Hit", Timer.Work(40.seconds), Timer.Rest(20.seconds)),
    Triple("Tabata", Timer.Work(20.seconds), Timer.Rest(10.seconds)),
    Triple("Emom", Timer.Work(60.seconds), Timer.Rest(0.seconds)),
    Triple("Amrap", Timer.Work(60.seconds), Timer.Rest(30.seconds)),
)

/**
 * Class that container the running timer with other information
 * @param currentTimer current timer
 * @param currentDuration remaining duration of the current timer
 * @param currentPercent percent of competition of the current timer
 * @param timerCompleted list of all timer completed
 * @param timerDuration duration of the sum of the all timer * reps
 * @param timerRemaining remaining duration of the time duration
 * @param timerSequenceReps total number of the repetition of the sequence
 * @param timerSequenceNumber number of the current reps
 */
data class CurrentTimer(
    val currentTimer: Timer = Timer.Prepare(),
    var currentDuration: Duration = 0.seconds,
    var currentPercent: Int = 0,
    var timerCompleted: List<Timer> = emptyList(),
    val timerDuration: Duration = 0.seconds,
    var timerRemaining: Duration = 0.seconds,
    val timerSequenceReps: Int = 0,
    val timerSequenceNumber: Int = 0,
) {

    val currentDurationFormatted: String
        get() {
            val seconds = currentDuration.inWholeSeconds
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secondsFiltered = (seconds % 60)
            return when {
                hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, secondsFiltered)
                minutes > 0 -> String.format("%02d:%02d", minutes, secondsFiltered)
                else -> seconds.toString()
            }
        }
}

interface WorkoutTimer {

    /**
     * Represent all the timer that the user want to do
     */
    val timerSequence: List<Timer>

    /**
     * Number of repetitions of the timer sequence. Cannot be less than 1
     */
    val timerSequenceReps: Int

    /**
     * To listen the timer changes
     */
    val currentTimerFlow: StateFlow<CurrentTimer>

    /**
     * To know when the timer is running or not
     */
    val isRunning: StateFlow<Boolean>

    /**
     * Add a timer to the list
     */
    fun add(timer: Timer)

    /**
     * Add a timer to the list
     */
    fun add(vararg timers: Timer)

    /**
     * Remove the latest timer in the sequence with the same type and duration
     */
    fun remove(timer: Timer)

    /**
     * Remove the latest timer in the sequence matching the type and not the duration
     */
    fun removeByType(timer: Timer)

    /**
     * Remove the latest timer in the sequence
     */
    fun remove()

    /**
     * Replace last occurrence with the input value or add if not present
     */
    fun replace(timer: Timer)

    /**
     * Increase the number of the timer sequence repetitions
     */
    fun increaseReps()

    /**
     * Decrease the number of the timer sequence repetitions
     */
    fun decreaseReps()

    /**
     * Start the timer sequence
     */
    fun start()

    /**
     * Stop the current timer and clear the timers done list
     */
    fun stop()

    /**
     * Pause or resume the current timer
     */
    fun startOrPause()

    /**
     * Remove all timers
     */
    fun clear()

}