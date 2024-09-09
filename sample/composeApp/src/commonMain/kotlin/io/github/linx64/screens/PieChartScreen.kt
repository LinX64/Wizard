package io.github.linx64.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.linx64.cmpwizard.piechart.BasePieChart
import io.github.linx64.cmpwizard.piechart.PieSlice

@Composable
fun PieChartRoute(modifier: Modifier = Modifier) {
    val slices = listOf(
        PieSlice(30f, Color(0xFFE57373), "Red"),
        PieSlice(40f, Color(0xFF81C784), "Green"),
        PieSlice(20f, Color(0xFF64B5F6), "Blue"),
        PieSlice(10f, Color(0xFFFFD54F), "Yellow")
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasePieChart(
            slices = slices,
            modifier = Modifier.size(300.dp),
            holeRatio = 0.6f,
            shadowColor = Color(0xFF9E9E9E)
        )
    }
}