package io.github.linx64.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.linx64.cmpwizard.linechart.BaseLineChart
import io.github.linx64.cmpwizard.linechart.ChartDataPoint
import io.github.linx64.components.ChartDataLoader

@Composable
internal fun NormalLineChartScreen(
    modifier: Modifier = Modifier
) {
    var chartData by remember { mutableStateOf<List<ChartDataPoint>>(emptyList()) }
    ChartDataLoader { loadedData ->
        chartData = loadedData
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BaseLineChart(
            modifier = modifier,
            chartData = chartData.toSet(),
            height = 200.dp,
            isInteractivityEnabled = false,
            lighterColor = MaterialTheme.colorScheme.surface,
        )
    }
}

