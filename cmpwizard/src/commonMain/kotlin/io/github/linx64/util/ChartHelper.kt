package io.github.linx64.util

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.linx64.linechart.ChartDataPoint
import io.github.linx64.ui.common.formatToPrice
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ChartHelper {
    fun DrawScope.drawChartLine(
        chartState: ChartState,
        size: Size,
        lighterColor: Color,
        lightLineColor: Color
    ) {
        val spacing = 10f
        val spacePerPoint = (size.width - spacing) / (chartState.prices.size - 1)

        val strokePath = Path().apply {
            val startValuePercentage =
                getValuePercentageForRange(chartState.prices[0], chartState.maxPrice, chartState.minPrice)
            moveTo(spacing, size.height * (1 - startValuePercentage))
            chartState.prices.forEachIndexed { index, price ->
                val valuePercentage = getValuePercentageForRange(price, chartState.maxPrice, chartState.minPrice)
                val x = spacing + index * spacePerPoint
                val y = size.height * (1 - valuePercentage)
                lineTo(x, y)
            }
        }

        val fillPath = Path().apply {
            addPath(strokePath)
            lineTo(size.width - spacing, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        // Draw gradient shadow
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lightLineColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

        // Draw the main line with horizontal gradient
        drawPath(
            path = strokePath,
            brush = Brush.horizontalGradient(
                colors = listOf(lighterColor, lightLineColor, lighterColor),
                startX = 0f,
                endX = size.width
            ),
            style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    fun DrawScope.drawNormalChartLine(
        chartState: ChartState,
        size: Size,
        lineColor: Color
    ) {
        val spacing = 10f
        val spacePerPoint = (size.width - spacing) / (chartState.prices.size - 1)

        val strokePath = Path().apply {
            val startValuePercentage =
                getValuePercentageForRange(chartState.prices[0], chartState.maxPrice, chartState.minPrice)
            moveTo(spacing, size.height * (1 - startValuePercentage))
            chartState.prices.forEachIndexed { index, price ->
                val valuePercentage = getValuePercentageForRange(price, chartState.maxPrice, chartState.minPrice)
                val x = spacing + index * spacePerPoint
                val y = size.height * (1 - valuePercentage)
                lineTo(x, y)
            }
        }

        val fillPath = Path().apply {
            addPath(strokePath)
            lineTo(size.width - spacing, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        // Draw gradient shadow
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor, Color.Transparent),
                endY = size.height - spacing
            )
        )

        // Draw the main line

        drawPath(
            path = strokePath,
            color = lineColor,
            style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    fun DrawScope.drawHighestPoint(
        chartData: List<ChartDataPoint>,
        highestPointColor: Color,
        chartState: ChartState,
        size: Size
    ) {
        val x = chartState.highestIndex * size.width / (chartData.size - 1)
        val y = size.height * (1 - getValuePercentageForRange(chartState.maxPrice, chartState.maxPrice, chartState.minPrice))
        drawCircle(
            color = highestPointColor,
            radius = 14f,
            center = Offset(x, y)
        )
    }

    fun DrawScope.drawSelectedPointLine(
        chartState: ChartState,
        dashedLineColor: Color,
        size: Size
    ) {
        val x = chartState.selectedIndex * size.width / (chartState.prices.size - 1)
        drawDashedLine(
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            color = dashedLineColor,
            strokeWidth = 2f
        )
    }

    fun DrawScope.drawMinMaxLabels(
        labelColor: Color,
        labelBackgroundColor: Color,
        min: Float,
        max: Float,
        textMeasurer: TextMeasurer,
        size: Size
    ) {
        val padding = 8.dp.toPx()
        val verticalOffset = 16.dp.toPx()
        val arrowTextSpacing = 4.dp.toPx()
        val textStyle = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            color = labelColor,
        )

        val minText = "$${formatToPrice(min.toDouble())}"
        val maxText = "$${formatToPrice(max.toDouble())}"

        val minTextLayout = textMeasurer.measure(minText, textStyle)
        val maxTextLayout = textMeasurer.measure(maxText, textStyle)

        val arrowSize = 16.dp.toPx()

        // Draw background rectangles for labels
        drawRoundRect(
            color = labelBackgroundColor,
            topLeft = Offset(0f, size.height - minTextLayout.size.height - padding * 2 - verticalOffset),
            size = Size(
                minTextLayout.size.width + padding * 2 + arrowSize + arrowTextSpacing,
                minTextLayout.size.height + padding * 2
            ),
            cornerRadius = CornerRadius(4.dp.toPx())
        )
        drawRoundRect(
            color = labelBackgroundColor,
            topLeft = Offset(size.width - maxTextLayout.size.width - padding * 2 - arrowSize - arrowTextSpacing, verticalOffset),
            size = Size(
                maxTextLayout.size.width + padding * 2 + arrowSize + arrowTextSpacing,
                maxTextLayout.size.height + padding * 2
            ),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Draw MIN label and arrow
        drawText(
            textMeasurer = textMeasurer,
            text = minText,
            topLeft = Offset(
                padding + arrowSize + arrowTextSpacing,
                size.height - minTextLayout.size.height - padding - verticalOffset
            ),
            style = textStyle
        )
        drawPath(
            path = Path().apply {
                moveTo(padding, size.height - minTextLayout.size.height / 2 - padding - verticalOffset)
                lineTo(padding + arrowSize / 2, size.height - padding - verticalOffset)
                lineTo(padding + arrowSize, size.height - minTextLayout.size.height / 2 - padding - verticalOffset)
                close()
            },
            color = Color.Red
        )

        // Draw MAX label and arrow
        drawText(
            textMeasurer = textMeasurer,
            text = maxText,
            topLeft = Offset(
                size.width - maxTextLayout.size.width - padding - arrowSize - arrowTextSpacing,
                padding + verticalOffset
            ),
            style = textStyle
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width - padding, padding + maxTextLayout.size.height / 2 + verticalOffset)
                lineTo(size.width - padding - arrowSize / 2, padding + verticalOffset)
                lineTo(size.width - padding - arrowSize, padding + maxTextLayout.size.height / 2 + verticalOffset)
                close()
            },
            color = Color.Green
        )
    }

    private fun DrawScope.drawDashedLine(
        start: Offset,
        end: Offset,
        color: Color,
        strokeWidth: Float,
        dashLength: Float = 10f,
        gapLength: Float = 10f
    ) {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
        val angle = kotlin.math.atan2(dy, dx)

        val cos = kotlin.math.cos(angle)
        val sin = kotlin.math.sin(angle)

        var startX = start.x
        var startY = start.y
        var remainingDistance = distance

        while (remainingDistance > 0) {
            val dashToDraw = minOf(dashLength, remainingDistance)
            val endX = startX + dashToDraw * cos
            val endY = startY + dashToDraw * sin

            drawLine(
                color = color,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth
            )

            startX = endX
            startY = endY
            remainingDistance -= dashToDraw

            if (remainingDistance > 0) {
                val gapToSkip = minOf(gapLength, remainingDistance)
                startX += gapToSkip * cos
                startY += gapToSkip * sin
                remainingDistance -= gapToSkip
            }
        }
    }

    fun formatTimestamp(timestamp: Long): String {
        val instant = Instant.fromEpochSeconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.monthNumber}/${dateTime.dayOfMonth}, ${dateTime.hour}:${dateTime.minute}"
    }

    private fun getValuePercentageForRange(value: Float, max: Float, min: Float): Float {
        return if (max - min == 0f) 0f else (value - min) / (max - min)
    }
}