package com.marozzi.workout.timer.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marozzi.workout.timer.app.ui.data.TimerViewModel
import com.marozzi.workout.timer.app.ui.utils.DevicePosture
import com.marozzi.workout.timer.app.ui.utils.WorkoutContentType
import com.marozzi.workout.timer.app.ui.utils.WorkoutNavigationType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutApp(
    windowSize: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture,
    timerViewModel: TimerViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (windowSize == WindowWidthSizeClass.Expanded) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    NavigationDrawerContent(timerViewModel = timerViewModel)
                }
            }
        ) {
            WorkoutAppContent(
                showNavigationRail = false,
                timerViewModel = timerViewModel
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawerContent(
                        timerViewModel = timerViewModel,
                        onDrawerClicked = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            },
            drawerState = drawerState
        ) {
            WorkoutAppContent(
                showNavigationRail = true,
                timerViewModel = timerViewModel,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
fun WorkoutAppContent(
    showNavigationRail: Boolean,
    timerViewModel: TimerViewModel,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showNavigationRail) {
            WorkoutNavigationRail(onDrawerClicked = onDrawerClicked)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            TimerContent(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timerViewModel = timerViewModel
            )
        }
    }
}

@Composable
fun WorkoutAppBk(
    windowSize: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture,
    timerViewModel: TimerViewModel
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     *
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val navigationType: WorkoutNavigationType
    val contentType: WorkoutContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = WorkoutNavigationType.BOTTOM_NAVIGATION
            contentType = WorkoutContentType.TIMER_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = WorkoutNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                WorkoutContentType.TIMER_AND_CONTROL_PANEL
            } else {
                WorkoutContentType.TIMER_ONLY
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                WorkoutNavigationType.NAVIGATION_RAIL
            } else {
                WorkoutNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = WorkoutContentType.TIMER_AND_CONTROL_PANEL
        }

        else -> {
            navigationType = WorkoutNavigationType.BOTTOM_NAVIGATION
            contentType = WorkoutContentType.TIMER_ONLY
        }
    }

    WorkoutNavigationWrapperUI(
        navigationType = navigationType,
        contentType = contentType,
        timerViewModel = timerViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutNavigationWrapperUI(
    navigationType: WorkoutNavigationType,
    contentType: WorkoutContentType,
    timerViewModel: TimerViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (navigationType == WorkoutNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    NavigationDrawerContent(timerViewModel = timerViewModel)
                }
            }
        ) {
            WorkoutAppContentBk(
                navigationType = navigationType,
                contentType = contentType,
                timerViewModel = timerViewModel
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawerContent(
                        timerViewModel = timerViewModel,
                        onDrawerClicked = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            },
            drawerState = drawerState
        ) {
            WorkoutAppContentBk(
                navigationType = navigationType,
                contentType = contentType,
                timerViewModel = timerViewModel,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
fun WorkoutAppContentBk(
    navigationType: WorkoutNavigationType,
    contentType: WorkoutContentType,
    timerViewModel: TimerViewModel,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == WorkoutNavigationType.NAVIGATION_RAIL) {
            WorkoutNavigationRail(onDrawerClicked = onDrawerClicked)
        }
        if (contentType == WorkoutContentType.TIMER_AND_CONTROL_PANEL) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                TimerContent(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(66f),
                    timerViewModel = timerViewModel
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground)
                )
                TimerController(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(34f),
                    timerViewModel = timerViewModel
                )
            }
        } else {
            TimerContent(
                modifier = Modifier
                    .fillMaxSize(),
                timerViewModel = timerViewModel
            )
        }
    }
}

@Composable
@Preview
fun WorkoutNavigationRail(
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        NavigationRailItem(
            selected = false,
            onClick = onDrawerClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = ""/*stringResource(id = R.string.navigation_drawer)*/
                )
            }
        )
//        NavigationRailItem(
//            selected = true,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Inbox,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            }
//        )
//        NavigationRailItem(
//            selected = false,
//            onClick = {/*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Article,
//                    stringResource(id = R.string.tab_article)
//                )
//            }
//        )
//        NavigationRailItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = { Icon(imageVector = Icons.Outlined.Chat, stringResource(id = R.string.tab_dm)) }
//        )
//        NavigationRailItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Outlined.People,
//                    stringResource(id = R.string.tab_groups)
//                )
//            }
//        )
    }
}

//@Composable
//@Preview
//fun WorkoutBottomNavigationBar() {
//    NavigationBar(modifier = Modifier.fillMaxWidth()) {
//        NavigationBarItem(
//            selected = true,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Inbox,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Article,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Outlined.Chat,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = {
//                Icon(
//                    imageVector = Icons.Outlined.Videocam,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            }
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    onDrawerClicked: () -> Unit = {}
) {
    TimerController(modifier = modifier, timerViewModel = timerViewModel)
//    Column(
//        modifier
//            .wrapContentWidth()
//            .fillMaxHeight()
//            .background(MaterialTheme.colorScheme.inverseOnSurface)
//            .padding(24.dp)
//    ) {
//        Row(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = stringResource(id = R.string.app_name).uppercase(),
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.primary
//            )
//            IconButton(onClick = onDrawerClicked) {
//                Icon(
//                    imageVector = Icons.Default.MenuOpen,
//                    contentDescription = stringResource(id = R.string.navigation_drawer)
//                )
//            }
//        }
//
//        NavigationDrawerItem(
//            selected = selectedDestination == ReplyDestinations.INBOX,
//            label = {
//                Text(
//                    text = stringResource(id = R.string.tab_inbox),
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Inbox,
//                    contentDescription = stringResource(id = R.string.tab_inbox)
//                )
//            },
//            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
//            onClick = { /*TODO*/ }
//        )
//        NavigationDrawerItem(
//            selected = selectedDestination == ReplyDestinations.ARTICLES,
//            label = {
//                Text(
//                    text = stringResource(id = R.string.tab_article),
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Article,
//                    contentDescription = stringResource(id = R.string.tab_article)
//                )
//            },
//            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
//            onClick = { /*TODO*/ }
//        )
//        NavigationDrawerItem(
//            selected = selectedDestination == ReplyDestinations.DM,
//            label = {
//                Text(
//                    text = stringResource(id = R.string.tab_dm),
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Chat,
//                    contentDescription = stringResource(id = R.string.tab_dm)
//                )
//            },
//            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
//            onClick = { /*TODO*/ }
//        )
//        NavigationDrawerItem(
//            selected = selectedDestination == ReplyDestinations.GROUPS,
//            label = {
//                Text(
//                    text = stringResource(id = R.string.tab_groups),
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Article,
//                    contentDescription = stringResource(id = R.string.tab_groups)
//                )
//            },
//            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
//            onClick = { /*TODO*/ }
//        )
//    }
}