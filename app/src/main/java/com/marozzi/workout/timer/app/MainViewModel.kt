package com.marozzi.workout.timer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import com.marozzi.domain.timer.WorkoutTimer
import com.marozzi.workout.timer.data.TimerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workoutTimer: WorkoutTimer,
) : ViewModel(), TimerViewModel {

    private var soundFxJob: Job? = null

    private val _workoutTimerWithReps = MutableStateFlow(emptyList<Timer>())
    override val workoutTimerWithReps: StateFlow<List<Timer>> = _workoutTimerWithReps

    private val _workoutTimerTotalDuration = MutableStateFlow(Pair(0, 0.milliseconds))
    override val workoutTimerTotalRepsAndDuration = _workoutTimerTotalDuration

    override val isRunning: StateFlow<Boolean> = workoutTimer.isRunning
    override val currentTimer: StateFlow<CurrentTimer> = workoutTimer.currentTimerFlow

    override val selectedWorkTime: Timer.Work
        get() = workoutTimer
            .timerSequence
            .firstOrNull { it is Timer.Work } as? Timer.Work
            ?: Timer.Work(0.seconds)

    override val selectedRestTime: Timer.Rest
        get() = workoutTimer
            .timerSequence
            .firstOrNull { it is Timer.Rest } as? Timer.Rest
            ?: Timer.Rest(0.seconds)

    override val selectedRepsCount: Int
        get() = workoutTimer.timerSequenceReps

    init {
        resetTimer()
    }

    override fun resetTimer() {
        workoutTimer.clear()
        workoutTimer.add(Timer.Work(45.seconds), Timer.Rest(15.seconds))
        updateWorkoutTimerWithReps()
    }

    override fun addOrRemoveWorkTime(duration: Duration, add: Boolean) {
        val timer = Timer.Work(duration = duration)
        if (add) workoutTimer.add(timer)
        else workoutTimer.remove(timer)
        updateWorkoutTimerWithReps()
    }

    override fun addOrRemoveRestTime(duration: Duration, add: Boolean) {
        val timer = Timer.Rest(duration = duration)
        if (add) workoutTimer.add(timer)
        else workoutTimer.remove(timer)
        updateWorkoutTimerWithReps()
    }

    override fun replaceWorkTime(value: Duration) {
        if (value == 0.seconds) workoutTimer.removeByType(Timer.Work(value))
        else workoutTimer.replace(Timer.Work(value))
        updateWorkoutTimerWithReps()
    }

    override fun replaceRestTime(value: Duration) {
        if (value == 0.seconds) workoutTimer.removeByType(Timer.Rest(value))
        else workoutTimer.replace(Timer.Rest(value))
        updateWorkoutTimerWithReps()
    }

    override fun addOrRemoveRounds(add: Boolean) {
        if (add) workoutTimer.increaseReps()
        else workoutTimer.decreaseReps()
        updateWorkoutTimerWithReps()
    }

    private fun updateWorkoutTimerWithReps() {
        if (!workoutTimer.isRunning.value) {
            viewModelScope.launch(Dispatchers.IO) {
                val list = mutableListOf<Timer>()
                for (i in 1..workoutTimer.timerSequenceReps) {
                    list.addAll(workoutTimer.timerSequence)
                }
                _workoutTimerWithReps.emit(list)

                val timeDuration = list.sumOf {
                    it.duration.inWholeMilliseconds
                }.milliseconds
                _workoutTimerTotalDuration.update {
                    Pair(
                        workoutTimer.timerSequenceReps,
                        timeDuration
                    )
                }
            }
        }
    }

    private fun stopTimerInt() {
        soundFxJob?.cancel()
        workoutTimer.stop()
        updateWorkoutTimerWithReps()
    }

    override fun startTimer() {
        Analytics.logEvent("timer_start")
        stopTimerInt()
        soundFxJob = viewModelScope.launch(Dispatchers.IO) {
            workoutTimer.currentTimerFlow.collect {
                if (it.currentTimer !is Timer.Prepare) {
                    val durationDone = it.currentTimer.duration - it.currentDuration
                    when {
                        durationDone > (it.currentTimer.duration - 1.seconds) -> {
//                            play(CHANGE_STEP)
                        }

                        durationDone == (it.currentTimer.duration - 3.seconds) -> {
//                            play(COUNT_DOWN)
                        }
                    }
                }
            }
        }
        workoutTimer.start()
    }

    override fun stopTimer() {
        Analytics.logEvent("timer_stop")
        stopTimerInt()
    }

    override fun startOrPauseTimer() {
        Analytics.logEvent("timer_start_or_pause")
        workoutTimer.startOrPause()
    }

    override fun toggleBluetoothPanel() {
        Analytics.logEvent("timer_bluetooth")
    }

    override fun toggleVolumePanel() {
        Analytics.logEvent("timer_volume")
    }

    override fun setStandardTimer(value: Triple<String, Timer.Work, Timer.Rest>) {
        Analytics.logEvent(
            "timer_select_standard",
            listOf(
                Pair("item_id", value.first),
            )
        )
        replaceWorkTime(value.second.duration)
        replaceRestTime(value.third.duration)
    }
}