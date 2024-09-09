package io.github.linx64.linechart

import androidx.compose.runtime.Stable

@Stable
data class ChartDataPoint(
    val price: String,
    val timestamp: Long,
)
