package io.github.linx64.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.linx64.components.ChartDataLoader
import io.github.linx64.linechart.ChartDataPoint
import io.github.linx64.linechart.BaseLineChart
import io.github.linx64.theme.Lemon

@Composable
internal fun InteractiveLineChartScreen(
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
            lightLineColor = Lemon.primary,
            lighterColor = Color.Black,
        )
    }
}

