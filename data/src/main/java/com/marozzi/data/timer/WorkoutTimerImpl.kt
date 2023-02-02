package com.marozzi.data.timer

import com.marozzi.data.di.DefaultDispatcher
import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import com.marozzi.domain.timer.WorkoutTimer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


class WorkoutTimerImpl @Inject constructor(@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) :
    WorkoutTimer {

    private val _timerSequence = mutableListOf<Timer>()
    override val timerSequence: List<Timer>
        get() = _timerSequence

    private var _timerSequenceReps = 1
    override val timerSequenceReps: Int
        get() = _timerSequenceReps

    private val _currentTimerFlow = MutableStateFlow(CurrentTimer())
    override val currentTimerFlow: StateFlow<CurrentTimer>
        get() = _currentTimerFlow

    private val _isRunning = MutableStateFlow(false)
    override val isRunning: StateFlow<Boolean>
        get() = _isRunning

    private var pauseTimer = false
    private var processorJob: Job? = null

    override fun add(timer: Timer) {
        _timerSequence.add(timer)
    }

    override fun add(vararg timers: Timer) {
        _timerSequence.addAll(timers)
    }

    override fun remove(timer: Timer) {
        _timerSequence.indexOfLast { it == timer }
            .takeIf { it != -1 }
            ?.let {
                _timerSequence.removeAt(it)
            }
    }

    override fun removeByType(timer: Timer) {
        _timerSequence.indexOfLast { it::class.java == timer::class.java }
            .takeIf { it != -1 }
            ?.let {
                _timerSequence.removeAt(it)
            }
    }

    override fun remove() {
        _timerSequence.removeLastOrNull()
    }

    override fun replace(timer: Timer) {
        _timerSequence.indexOfLast { it::class == timer::class }.let {
            if (it == -1) {
                _timerSequence.add(timer)
            } else {
                _timerSequence.set(it, timer)
            }
        }
    }

    override fun increaseReps() {
        _timerSequenceReps++
    }

    override fun decreaseReps() {
        if (_timerSequenceReps > 1)
            _timerSequenceReps--
    }

    override fun start() {
        require(timerSequence.isNotEmpty()) {
            "Please add at least one timer before start"
        }
        stop()
        processorJob = CoroutineScope(defaultDispatcher).launch {
            _isRunning.update { true }
            val workoutDuration = (timerSequence.sumOf {
                it.duration.inWholeMilliseconds
            } * timerSequenceReps).milliseconds
            var remainingWorkoutTime = workoutDuration
            for (i in 1..timerSequenceReps) {
                val iterator = timerSequence.iterator()
                while (iterator.hasNext()) {
                    val timer = iterator.next()
                    var timerToRun = _currentTimerFlow.updateAndGet {
                        it.copy(
                            currentTimer = timer,
                            currentDuration = timer.duration,
                            currentPercent = 0,
                            timerDuration = workoutDuration,
                            timerRemaining = remainingWorkoutTime,
                            timerSequenceReps = timerSequenceReps,
                            timerSequenceNumber = i
                        )
                    }
                    while (timerToRun.currentDuration > 0.seconds) {
                        if (pauseTimer) {
                            _isRunning.update { false }
                            delay(500.milliseconds)
                        } else {
                            _isRunning.update { true }
                            delay(1.seconds)
                            if (!pauseTimer) {
                                val currentDuration = timerToRun.currentDuration - 1.seconds
                                val currentPercent =
                                    100 - (currentDuration / timerToRun.currentTimer.duration) * 100.0
                                remainingWorkoutTime -= 1.seconds
                                timerToRun = _currentTimerFlow.updateAndGet {
                                    it.copy(
                                        currentDuration = currentDuration,
                                        currentPercent = currentPercent.toInt(),
                                        timerRemaining = remainingWorkoutTime
                                    )
                                }
                            }
                        }
                        println("timerToRun: $timerToRun")
                    }
                    _currentTimerFlow.update {
                        it.copy(
                            timerCompleted = it.timerCompleted.toMutableList().apply { add(timer) })
                    }
                }
            }
            _isRunning.update { false }
        }
    }

    override fun stop() {
        processorJob?.cancel()
        _currentTimerFlow.tryEmit(CurrentTimer())
        pauseTimer = false
        _isRunning.update { false }
    }

    override fun startOrPause() {
        pauseTimer = !pauseTimer
    }

    override fun clear() {
        stop()
        _timerSequence.clear()
        _timerSequenceReps = 1
        _currentTimerFlow.update { (CurrentTimer()) }
    }

}