package io.github.linx64.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.linx64.linechart.ChartDataPoint
import kotlin.math.roundToInt

class ChartState(chartData: List<ChartDataPoint>) {
    val prices = chartData.map { it.price.toFloat() }
    val maxPrice = prices.maxOrNull() ?: 0f
    val minPrice = prices.minOrNull() ?: 0f
    val highestIndex = prices.indexOfFirst { it == maxPrice }

    var selectedIndex by mutableStateOf(highestIndex)
        private set
    var isInteracting by mutableStateOf(false)
        private set

    fun startInteraction() {
        isInteracting = true
    }

    fun endInteraction() {
        isInteracting = false
        selectedIndex = highestIndex
    }

    fun updateSelectedIndex(position: Float) {
        selectedIndex = position.roundToInt().coerceIn(0, prices.lastIndex)
    }
}