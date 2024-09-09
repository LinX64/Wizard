package io.github.linx64.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens(val route: String) {

    @Serializable
    data object HomeScreen : Screens("home")

    @Serializable
    data object InteractiveChartScreen : Screens("interactive_chart")

    @Serializable
    data object NormalLineChartScreen : Screens("normal_line_chart")

    @Serializable
    data object PieChartScreen : Screens("pie_chart")
}