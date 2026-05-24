package com.ghost.legion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ghost.legion.presentation.screen.settings.SettingsScreen
import com.ghost.legion.presentation.screen.terminal.TerminalScreen
import com.ghost.legion.presentation.screen.terminal.TerminalViewModel
import com.ghost.legion.presentation.screen.worldboard.WorldBoardScreen
import com.ghost.legion.presentation.screen.worldboard.WorldBoardViewModel

sealed class LegionRoute(val route: String) {
    data object Terminal : LegionRoute("terminal")
    data object WorldBoard : LegionRoute("world_board")
    data object Settings : LegionRoute("settings")
}

@Composable
fun LegionNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = LegionRoute.Terminal.route
    ) {
        composable(LegionRoute.Terminal.route) {
            val viewModel: TerminalViewModel = hiltViewModel()
            TerminalScreen(
                viewModel = viewModel,
                onNavigateToWorldBoard = {
                    navController.navigate(LegionRoute.WorldBoard.route)
                },
                onNavigateToSettings = {
                    navController.navigate(LegionRoute.Settings.route)
                }
            )
        }

        composable(LegionRoute.WorldBoard.route) {
            val viewModel: WorldBoardViewModel = hiltViewModel()
            WorldBoardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(LegionRoute.Settings.route) {
            val viewModel: com.ghost.legion.presentation.screen.settings.SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
