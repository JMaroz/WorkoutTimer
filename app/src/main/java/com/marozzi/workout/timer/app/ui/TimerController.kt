package com.marozzi.workout.timer.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marozzi.desing.theme.SPACE_HIGH
import com.marozzi.desing.theme.SPACE_MEDIUM
import com.marozzi.desing.ui.RoundButton
import com.marozzi.desing.ui.RoundSurface
import com.marozzi.desing.ui.TextBody
import com.marozzi.desing.ui.TitleLarge
import com.marozzi.desing.ui.TitleMedium
import com.marozzi.desing.ui.TitleSmall
import com.marozzi.desing.ui.roundBorder
import com.marozzi.domain.timer.Timer
import com.marozzi.domain.timer.presetTimer
import com.marozzi.workout.timer.app.R
import com.marozzi.workout.timer.app.ui.data.TimerViewModel
import com.marozzi.workout.timer.app.ui.utils._fakeTimerViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Composable function to show the timer and it's controller
 */
@Composable
fun TimerController(modifier: Modifier, timerViewModel: TimerViewModel) {
    var selectedItem by remember {
        mutableStateOf(
            if (timerViewModel.selectedWorkTime.duration != 0.seconds) {
                presetTimer.first().copy(
                    second = timerViewModel.selectedWorkTime,
                    third = timerViewModel.selectedRestTime
                )
            } else {
                presetTimer.first()
            }
        )
    }
    LazyColumn(
        modifier = modifier,
        content = {
            item(key = "PresentTimer") {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SPACE_MEDIUM),
                    contentPadding = PaddingValues(all = SPACE_HIGH),
                    content = {
                        items(
                            items = presetTimer,
                            key = { it.first },
                            itemContent = {
                                PresetItem(it, selectedItem.first == it.first) {
                                    selectedItem = it
                                    timerViewModel.setStandardTimer(it)
                                }
                            },
                        )
                    },
                )
            }
            item(key = "Divider") {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground)
                )
            }
            item(key = "WorkController") {
                val updateValue = { value: Duration ->
                    timerViewModel.replaceWorkTime(value)
                    selectedItem = selectedItem.copy(first = "Custom", second = Timer.Work(value))

                }
                SectionController(
                    sectionTitle = stringResource(id = R.string.time_work_title),
                    value = selectedItem.second.duration,
                    onClickNegative = {
                        if (it > 5.seconds) {
                            updateValue(it - 5.seconds)
                        }
                    },
                    onClickPositive = {
                        if (it < 3595.seconds) {
                            updateValue(it + 5.seconds)
                        }
                    }
                )
            }
            item(key = "RestController") {
                val updateValue = { value: Duration ->
                    timerViewModel.replaceRestTime(value)
                    selectedItem = selectedItem.copy(first = "Custom", third = Timer.Rest(value))

                }
                SectionController(
                    sectionTitle = stringResource(id = R.string.time_rest_title),
                    value = selectedItem.third.duration,
                    onClickNegative = {
                        if (it > 0.seconds) {
                            updateValue(it - 5.seconds)
                        }
                    },
                    onClickPositive = {
                        if (it < 3.minutes) {

                            updateValue(it + 5.seconds)
                        }
                    }
                )
            }
            item(key = "RepsController") {
                RepsController(countInitial = timerViewModel.selectedRepsCount) {
                    timerViewModel.addOrRemoveRounds(it)
                }
            }
            item(key = "StartButton") {
                StartController {
                    timerViewModel.startTimer()
                }
            }
        },
    )

}


@Composable
private fun PresetItem(
    data: Triple<String, Timer.Work, Timer.Rest>,
    selected: Boolean,
    onClick: (Triple<String, Timer.Work, Timer.Rest>) -> Unit,
) {
    RoundSurface(
        onClick = { onClick(data) },
        modifier = Modifier.height(60.dp),
        borderColor = if (selected)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface.copy(alpha = .8f),
        backgroundColor = if (selected)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface.copy(alpha = .8f)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val txt = when (data.first) {
                "Custom" -> stringResource(id = R.string.custom)
                else -> "${data.first} ${data.second.duration.inWholeSeconds}-${data.third.duration.inWholeSeconds}"
            }
            TextBody(
                text = txt,
                color = if (selected)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
                upperCase = true
            )
        }
    }
}

@Composable
private fun SectionController(
    sectionTitle: String,
    value: Duration,
    onClickNegative: (value: Duration) -> Unit,
    onClickPositive: (value: Duration) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = SPACE_HIGH)
    ) {
        TitleMedium(
            text = sectionTitle,
            color = MaterialTheme.colorScheme.onBackground,
            upperCase = true,
            modifier = Modifier.padding(start = SPACE_HIGH),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoundSurface(
                onClick = { onClickNegative(value) },
                modifier = Modifier.size(width = 90.dp, height = 60.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.marozzi.design.R.drawable.ico_remove),
                        contentDescription = "Remove work Time",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 60.dp)
                    .roundBorder(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                        borderColor = Color.White.copy(alpha = .1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                TitleLarge(
                    text = "   ${
                        stringResource(
                            id = R.string.time_value,
                            value.inWholeSeconds
                        )
                    }",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            RoundSurface(
                onClick = {
                    onClickPositive(value)
                },
                modifier = Modifier.size(width = 90.dp, height = 60.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.marozzi.design.R.drawable.ico_add),
                        contentDescription = "Add work time",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

@Composable
private fun RepsController(countInitial: Int, callback: (add: Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = SPACE_HIGH)
    ) {
        TitleMedium(
            text = stringResource(id = R.string.time_rounds),
            color = Color.White,
            upperCase = true,
            modifier = Modifier.padding(start = SPACE_HIGH)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            var count by remember {
                mutableStateOf(countInitial)
            }
            RoundSurface(
                onClick = {
                    if (count > 1) {
                        count--
                        callback(false)
                    }
                },
                modifier = Modifier.size(width = 181.dp, height = 124.dp),
                backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                borderColor = Color.White.copy(alpha = .1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.marozzi.design.R.drawable.ico_remove),
                        contentDescription = "Remove work Time",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(width = 181.dp, height = 124.dp)
                    .roundBorder(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                        borderColor = MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                TitleLarge(
                    text = "x $count",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            RoundSurface(
                onClick = {
                    if (count < 60) {
                        count++
                        callback(true)
                    }
                },
                modifier = Modifier.size(width = 181.dp, height = 124.dp),
                backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                borderColor = Color.White.copy(alpha = .1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.marozzi.design.R.drawable.ico_add),
                        contentDescription = "Add work time",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

@Composable
private fun StartController(onClickStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = SPACE_HIGH)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = SPACE_HIGH)
                .background(color = Color.White.copy(alpha = .2f))
        )
        Spacer(modifier = Modifier.height(SPACE_HIGH))
        RoundButton(
            onClick = onClickStart,
            modifier = Modifier
                .size(45.dp)
                .padding(start = SPACE_HIGH, end = SPACE_HIGH)
        ) {
            TitleSmall(text = stringResource(id = R.string.time_start), upperCase = true)
        }
    }
}

@Preview(device = Devices.DESKTOP)
@Composable
private fun TestTimerController() {
    TimerController(modifier = Modifier.fillMaxSize(), timerViewModel = _fakeTimerViewModel)
}