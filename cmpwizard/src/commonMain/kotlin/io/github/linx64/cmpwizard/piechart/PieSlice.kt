package io.github.linx64.cmpwizard.piechart

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class PieSlice(
    val value: Float,
    val color: Color,
    val label: String
)
