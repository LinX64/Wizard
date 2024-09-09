package io.github.linx64.piechart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
public fun BasePieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>,
    holeRatio: Float = 0.5f,
    shadowColor: Color = Color.Gray,
    chartLabel: String = "Pie Chart",
    chartLabelColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPieChart(
                slices = slices,
                holeRatio = holeRatio,
                shadowColor = shadowColor,
                labelColor = chartLabelColor,
                textMeasurer = textMeasurer
            )
        }
        Text(
            text = chartLabel,
            color = chartLabelColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun DrawScope.drawPieChart(
    slices: List<PieSlice>,
    holeRatio: Float,
    shadowColor: Color,
    labelColor: Color,
    backgroundBrushColors: List<Color> = listOf(Color(0xFFE0E0E0), Color(0xFFF5F5F5)),
    textMeasurer: TextMeasurer,
    spaceBetweenSlices: Float = 2f  // New parameter for space between slices
) {
    val total = slices.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f  // Start from the top

    // Draw background with gradient
    val backgroundBrush = Brush.radialGradient(
        colors = backgroundBrushColors,
        center = center,
        radius = size.minDimension / 2
    )
    drawCircle(brush = backgroundBrush)

    // Draw shadow
    for (i in slices.indices) {
        drawCircle(
            color = shadowColor.copy(alpha = 0.1f),
            radius = size.minDimension / 2 + i.dp.toPx(),
            style = Stroke(width = 2.dp.toPx())
        )
    }

    // Draw slices and labels
    slices.forEach { slice ->
        val sweepAngle = 360f * (slice.value / total)
        drawSlice(startAngle + spaceBetweenSlices / 2, sweepAngle - spaceBetweenSlices, slice.color, holeRatio)
        drawSliceLabel(
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            label = slice.label,
            color = labelColor,
            holeRatio = holeRatio,
            textMeasurer = textMeasurer
        )
        startAngle += sweepAngle
    }

    // Draw inner circle (hole)
    drawCircle(
        color = Color.White,
        radius = size.minDimension / 2 * holeRatio
    )
}

// Updated drawSlice function
private fun DrawScope.drawSlice(
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    holeRatio: Float
) {
    val padding = 5.dp.toPx()
    val sliceSize = Size(size.width - padding * 2, size.height - padding * 2)
    val sliceThickness = sliceSize.width * (1f - holeRatio) / 2

    // Draw main slice
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(padding, padding),
        size = sliceSize,
        style = Stroke(width = sliceThickness)
    )
}

private fun DrawScope.drawSliceLabel(
    startAngle: Float,
    sweepAngle: Float,
    label: String,
    holeRatio: Float,
    color: Color,
    textMeasurer: TextMeasurer
) {
    val radius = size.minDimension / 2
    val labelRadius = radius * (1f - holeRatio) * 0.9f + radius * holeRatio
    val angleRadians = (startAngle + sweepAngle / 2).toRadians()
    val x = center.x + (labelRadius * cos(angleRadians))
    val y = center.y + (labelRadius * sin(angleRadians))

    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = color
    )

    val textLayoutResult = textMeasurer.measure(label, textStyle)
    val textSize = textLayoutResult.size

    drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = textStyle,
        topLeft = Offset(x - textSize.width / 2, y - textSize.height / 2)
    )
}

private fun Float.toRadians(): Float = this * (PI.toFloat() / 180f)


