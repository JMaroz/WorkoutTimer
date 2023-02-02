package com.marozzi.workout.timer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import com.marozzi.domain.timer.WorkoutTimer
import com.marozzi.workout.timer.TimerController
import com.marozzi.workout.timer.TimerPage
import com.marozzi.workout.timer.app.theme.WorkoutTimerTheme
import com.marozzi.workout.timer.data.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTimerTheme {
                Row(modifier = Modifier.fillMaxSize()) {
                    TimerPage(
                        modifier = Modifier
                            .weight(1280f)
                            .fillMaxHeight(),
                        timerViewModel = viewModel
                    )
                    TimerController(
                        modifier = Modifier
                            .weight(640f)
                            .fillMaxHeight(),
                        timerViewModel = viewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.DESKTOP)
@Composable
fun GreetingPreview() {
    val timerViewModel = object : TimerViewModel {

        override val workoutTimerWithReps: StateFlow<List<Timer>> = MutableStateFlow(emptyList())
        override val workoutTimerTotalRepsAndDuration: StateFlow<Pair<Int, Duration>> =
            MutableStateFlow(Pair(1, 10.seconds))
        override val isRunning: StateFlow<Boolean> = MutableStateFlow(false)
        override val currentTimer: StateFlow<CurrentTimer> = MutableStateFlow(CurrentTimer())
        override val selectedWorkTime: Timer.Work = Timer.Work(30.seconds)
        override val selectedRestTime: Timer.Rest = Timer.Rest(10.seconds)
        override val selectedRepsCount: Int = 1

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
    WorkoutTimerTheme {
        TimerPage(modifier = Modifier.fillMaxSize(), timerViewModel)
    }
}