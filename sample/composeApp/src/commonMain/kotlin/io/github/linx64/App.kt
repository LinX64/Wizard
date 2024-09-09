package io.github.linx64

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.linx64.screens.HomeRoute
import io.github.linx64.screens.InteractiveLineChartScreen
import io.github.linx64.screens.NormalLineChartScreen
import io.github.linx64.screens.PieChartRoute
import io.github.linx64.screens.Screens.HomeScreen
import io.github.linx64.screens.Screens.InteractiveChartScreen
import io.github.linx64.screens.Screens.NormalLineChartScreen
import io.github.linx64.screens.Screens.PieChartScreen

@Composable
internal fun App() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar(currentRoute, navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeScreen.route,
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            addHomeScreen(navController)
            addInteractiveChartScreen()
            addNormalLineChartScreen()
            addPieChartScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(currentRoute: String?, navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = currentRoute?.uppercase() ?: "Unknown")
        },
        navigationIcon = {
            if (currentRoute != HomeScreen.route) {
                IconButton(
                    modifier = Modifier.padding(4.dp),
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

private fun NavGraphBuilder.addHomeScreen(navController: NavHostController) {
    composable(HomeScreen.route) {
        HomeRoute(
            onNavigateToLineChart = {
                navController.navigate(InteractiveChartScreen.route)
            },
            onNavigateToNormalLineChart = {
                navController.navigate(NormalLineChartScreen.route)
            },
            onNavigateToPieChart = {
                navController.navigate(PieChartScreen.route)
            }
        )
    }
}

private fun NavGraphBuilder.addInteractiveChartScreen() {
    composable(InteractiveChartScreen.route) {
        InteractiveLineChartScreen()
    }
}

private fun NavGraphBuilder.addNormalLineChartScreen() {
    composable(NormalLineChartScreen.route) {
        NormalLineChartScreen()
    }
}

private fun NavGraphBuilder.addPieChartScreen() {
    composable(PieChartScreen.route) {
        PieChartRoute()
    }
}