package io.github.linx64.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onNavigateToLineChart: () -> Unit,
    onNavigateToNormalLineChart: () -> Unit,
    onNavigateToPieChart: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onNavigateToLineChart) {
            Text(text = "Interactive Line Chart")
        }

        Button(onClick = onNavigateToNormalLineChart) {
            Text(text = "Normal Line Chart")
        }

        Button(onClick = onNavigateToPieChart) {
            Text(text = "Pie Chart")
        }
    }
}