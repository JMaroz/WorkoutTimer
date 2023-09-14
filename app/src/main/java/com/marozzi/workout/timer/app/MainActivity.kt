package com.marozzi.workout.timer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.marozzi.workout.timer.app.theme.WorkoutTimerTheme
import com.marozzi.workout.timer.app.ui.WorkoutApp
import com.marozzi.workout.timer.app.ui.utils.DevicePosture
import com.marozzi.workout.timer.app.ui.utils._fakeTimerViewModel
import com.marozzi.workout.timer.app.ui.utils.isBookPosture
import com.marozzi.workout.timer.app.ui.utils.isSeparating
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setCorrectWindowMetrics()
        super.onCreate(savedInstanceState)

        /**
         * Flow of [DevicePosture] that emits every time there's a change in the windowLayoutInfo
         */
        val devicePostureFlow = WindowInfoTracker
            .getOrCreate(this)
            .windowLayoutInfo(this)
            .flowWithLifecycle(lifecycle)
            .map { layoutInfo ->
                val foldingFeature = layoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()
                when {
                    isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)

                    isSeparating(foldingFeature) -> DevicePosture.Separating(
                        foldingFeature.bounds,
                        foldingFeature.orientation
                    )

                    else -> DevicePosture.NormalPosture
                }
            }
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = DevicePosture.NormalPosture
            )

        setContent {
            WorkoutTimerTheme {
                val windowSize = calculateWindowSizeClass(this)
                val devicePosture = devicePostureFlow.collectAsState().value
                WorkoutApp(windowSize.widthSizeClass, devicePosture, viewModel)
            }
        }
    }

    private fun setCorrectWindowMetrics() {
        resources.displayMetrics.apply {
            density = .9f
            scaledDensity = .9f
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutAppPreview() {
    WorkoutTimerTheme {
        WorkoutApp(
            windowSize = WindowWidthSizeClass.Compact,
            foldingDevicePosture = DevicePosture.NormalPosture,
            timerViewModel = _fakeTimerViewModel
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun WorkoutAppPreviewTablet() {
    WorkoutTimerTheme {
        WorkoutApp(
            windowSize = WindowWidthSizeClass.Medium,
            foldingDevicePosture = DevicePosture.NormalPosture,
            timerViewModel = _fakeTimerViewModel
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun WorkoutAppPreviewDesktop() {
    WorkoutTimerTheme {
        WorkoutApp(
            windowSize = WindowWidthSizeClass.Expanded,
            foldingDevicePosture = DevicePosture.NormalPosture,
            timerViewModel = _fakeTimerViewModel
        )
    }
}