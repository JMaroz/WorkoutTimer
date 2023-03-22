package com.marozzi.workout.timer.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marozzi.desing.theme.SPACE_LOW
import com.marozzi.desing.theme.SPACE_MEDIUM
import com.marozzi.desing.ui.RoundSurface
import com.marozzi.desing.ui.TitleLarge
import com.marozzi.desing.ui.TitleSmall
import com.marozzi.desing.ui.roundBorder
import com.marozzi.domain.timer.CurrentTimer
import com.marozzi.domain.timer.Timer
import com.marozzi.workout.timer.app.R
import com.marozzi.workout.timer.app.ui.data.TimerViewModel
import com.marozzi.workout.timer.app.ui.utils.viewModel
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration.Companion.seconds


@Preview(showBackground = true, device = Devices.DESKTOP)
@Composable
private fun TimerPageTest() {
    MaterialTheme {
        TimerPage(modifier = Modifier.fillMaxSize(), timerViewModel = viewModel)
    }
}

/**
 * Composable function for show the timer and it's content
 */
@Composable
fun TimerPage(modifier: Modifier, timerViewModel: TimerViewModel) {
    Column(
        modifier = modifier
    ) {
        TimerInfoAndTimelineSection(
            modifier = Modifier.fillMaxWidth(),
            timerViewModel = timerViewModel
        )
        TimerAndControllerSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SPACE_MEDIUM),
            timerViewModel = timerViewModel
        )
    }
}

@Composable
private fun TimerInfoAndTimelineSection(modifier: Modifier, timerViewModel: TimerViewModel) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)) {
        val currentTimerState = timerViewModel.currentTimer.collectAsState()
        val totalTimerState = timerViewModel.workoutTimerTotalRepsAndDuration.collectAsState()
        Column(
            modifier = Modifier
                .size(width = 180.dp, height = 150.dp)
                .roundBorder(
                    borderColor = MaterialTheme.colorScheme.surface.copy(alpha = .6f),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val duration = if (currentTimerState.value.timerRemaining > 0.seconds) {
                currentTimerState.value.timerRemaining
            } else {
                totalTimerState.value.second
            }
            val title = remember(key1 = duration) {
                val seconds = duration.inWholeSeconds
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secondsFiltered = (seconds % 60)
                return@remember when {
                    hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, secondsFiltered)
                    minutes > 0 -> String.format("%02d:%02d", minutes, secondsFiltered)
                    else -> seconds.toString()
                }
            }
            TitleLarge(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                upperCase = true,
                isNumber = true,
                maxLines = 1,
            )
            TitleSmall(
                text = stringResource(id = R.string.remaining_time),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
                upperCase = true,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        Column(
            modifier = Modifier
                .size(width = 180.dp, height = 150.dp)
                .roundBorder(
                    borderColor = MaterialTheme.colorScheme.surface.copy(alpha = .6f),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = if (currentTimerState.value.timerRemaining > 0.seconds) {
                "${currentTimerState.value.timerSequenceNumber}/${currentTimerState.value.timerSequenceReps}"
            } else {
                "0/${totalTimerState.value.first}"
            }
            TitleLarge(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                upperCase = true,
                isNumber = true
            )
            TitleSmall(
                text = stringResource(id = R.string.time_rounds),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
                upperCase = true,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        TimerTimeline(
            workoutTimerWithRepsFlow = timerViewModel.workoutTimerWithReps,
            currentTimerFlow = timerViewModel.currentTimer
        )
    }
}

@Composable
private fun TimerTimeline(
    workoutTimerWithRepsFlow: StateFlow<List<Timer>>,
    currentTimerFlow: StateFlow<CurrentTimer>,
) {
    val timers = workoutTimerWithRepsFlow.collectAsState().value
    val currentTimer = currentTimerFlow.collectAsState().value
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .roundBorder(
                borderColor = MaterialTheme.colorScheme.surface.copy(alpha = .6f),
                backgroundColor = MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACE_LOW)
    ) {
        Spacer(modifier = Modifier.width(SPACE_MEDIUM))
        timers.forEachIndexed { index, timer ->
            val percent = when {
                currentTimer.timerCompleted.getOrNull(index) != null -> 100
                index == currentTimer.timerCompleted.size -> currentTimer.currentPercent
                else -> 0
            }.toFloat() / 100
            if (timer is Timer.Work) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .weight(timer.duration.inWholeSeconds.toFloat())
                        .clip(RoundedCornerShape(4.dp)),
                    progress = percent,
                    color = Color.Yellow,
                    trackColor = Color.LightGray
                )
            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .weight(timer.duration.inWholeSeconds.toFloat())
                        .clip(RoundedCornerShape(4.dp)),
                    progress = percent,
                    color = Color.White,
                    trackColor = Color.LightGray
                )
            }
        }
        Spacer(modifier = Modifier.width(SPACE_MEDIUM))
    }
}

@Composable
private fun TimerAndControllerSection(modifier: Modifier, timerViewModel: TimerViewModel) {
    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)
        ) {
            PlayPause(timerViewModel.isRunning) { timerViewModel.startOrPauseTimer() }
            StopBox { timerViewModel.stopTimer() }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM, Alignment.Bottom),
            ) {
                BluetoothBox { timerViewModel.toggleBluetoothPanel() }
                VolumeBox { timerViewModel.toggleVolumePanel() }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-50).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentTimerState = timerViewModel.currentTimer.collectAsState()
            val txt = when (currentTimerState.value.currentTimer) {
                is Timer.Prepare -> "00"
                else -> currentTimerState.value.currentDurationFormatted
            }
            val color = when (currentTimerState.value.currentTimer) {
                is Timer.Work -> Color.Yellow
                is Timer.Rest -> Color.White
                else -> Color.White.copy(alpha = .6f)
            }
            Text(
                text = txt.toUpperCase(Locale.current),
                color = color,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 480.sp,
                    fontFeatureSettings = "tnum"
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            TitleLarge(
                text = when (currentTimerState.value.currentTimer) {
                    is Timer.Work -> stringResource(id = R.string.time_current_work)
                    is Timer.Rest -> stringResource(id = R.string.time_current_rest)
                    else -> stringResource(id = R.string.time_current_work)
                },
                color = color,
                upperCase = true,
                modifier = Modifier.offset(y = (-100).dp)
            )
        }
    }
}

@Composable
private fun VolumeBox(onClick: () -> Unit) {
    RoundSurface(
        modifier = Modifier
            .size(80.dp),
        borderShape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ico_volume),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "Volume",
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 19.dp)
        )
    }
}

@Composable
private fun BluetoothBox(onClick: () -> Unit) {
    RoundSurface(
        modifier = Modifier
            .size(80.dp),
        borderShape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ico_bluetooth),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "Bluetooth",
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 19.dp)
        )
    }
}

@Composable
private fun StopBox(onClick: () -> Unit) {
    RoundSurface(
        modifier = Modifier
            .size(80.dp),
        borderShape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ico_player_stop),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "Stop",
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 19.dp)
        )
    }
}

@Composable
private fun PlayPause(isRunning: StateFlow<Boolean>, onClick: () -> Unit) {
    val state = isRunning.collectAsState()
    RoundSurface(
        modifier = Modifier
            .size(80.dp),
        borderShape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Image(
            painter = if (state.value)
                painterResource(id = R.drawable.ico_player_pause)
            else
                painterResource(id = R.drawable.ico_player_play),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "Play/Pause",
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 19.dp)
        )
    }
}

