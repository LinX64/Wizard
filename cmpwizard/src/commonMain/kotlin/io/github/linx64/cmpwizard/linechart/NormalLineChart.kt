package io.github.linx64.cmpwizard.linechart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.linx64.cmpwizard.util.ChartHelper.drawHighestPoint
import io.github.linx64.cmpwizard.util.ChartHelper.drawMinMaxLabels
import io.github.linx64.cmpwizard.util.ChartHelper.drawNormalChartLine
import io.github.linx64.cmpwizard.util.ChartState
import kotlinx.coroutines.delay

@Composable
public fun NormalLineChart(
    modifier: Modifier = Modifier,
    chartData: Set<ChartDataPoint>?,
    isInteractivityEnabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 200.dp,
    lighterColor: Color = MaterialTheme.colorScheme.surface,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    labelBackgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    emptyStateMessage: String = "No chart data available!"
) {
    val shouldShowEmptyState = chartData.isNullOrEmpty() && !isLoading
    var showNoDataMessage by remember { mutableStateOf(false) }

    LaunchedEffect(shouldShowEmptyState) {
        showNoDataMessage = false
        if (shouldShowEmptyState) {
            delay(3000)
            showNoDataMessage = true
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
            .then(modifier)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            chartData?.isNotEmpty() == true -> {
                DrawChart(
                    modifier = Modifier.align(Alignment.Center).height(height),
                    chartData = chartData.toList(),
                    isInteractivityEnabled = isInteractivityEnabled,
                    lightLineColor = lineColor,
                    labelColor = labelColor,
                    labelBackgroundColor = labelBackgroundColor,
                    textMeasurer = textMeasurer,
                )
            }

            else -> Unit
        }

        if (showNoDataMessage) {
            Text(
                text = emptyStateMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun DrawChart(
    modifier: Modifier = Modifier,
    chartData: List<ChartDataPoint>,
    isInteractivityEnabled: Boolean,
    lightLineColor: Color,
    labelColor: Color,
    labelBackgroundColor: Color,
    textMeasurer: TextMeasurer,
    highestPointColor: Color = MaterialTheme.colorScheme.primary
) {
    val chartState = rememberChartState(chartData)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isInteractivityEnabled) {
                if (isInteractivityEnabled) {
                    detectDragGestures(
                        onDragStart = { chartState.startInteraction() },
                        onDragEnd = { chartState.endInteraction() },
                        onDragCancel = { chartState.endInteraction() },
                        onDrag = { change, _ ->
                            val x = change.position.x
                            chartState.updateSelectedIndex((x / size.width) * (chartData.size - 1))
                        }
                    )
                }
            }
    ) {
        drawNormalChartLine(
            chartState = chartState,
            size = size,
            lineColor = lightLineColor
        )

        if (isInteractivityEnabled) {
            drawHighestPoint(
                chartData = chartData,
                highestPointColor = highestPointColor,
                chartState = chartState,
                size = size
            )

            drawMinMaxLabels(
                labelColor = labelColor,
                labelBackgroundColor = labelBackgroundColor,
                min = chartState.minPrice,
                max = chartState.maxPrice,
                textMeasurer = textMeasurer,
                size = size
            )
        }
    }
}

@Composable
private fun rememberChartState(chartData: List<ChartDataPoint>): ChartState = remember(chartData) {
    ChartState(chartData)
}
