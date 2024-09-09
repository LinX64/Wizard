package io.github.linx64.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.linx64.linechart.ChartDataPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import wizard.sample.composeapp.generated.resources.Res

@Serializable
data class JsonResponse(
    val data: List<JsonDataPoint>
)

@Serializable
data class JsonDataPoint(
    val priceUsd: String,
    val time: Long,
    val date: String
)

val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChartDataLoader(onDataLoaded: (List<ChartDataPoint>) -> Unit) {
    var bytes by remember { mutableStateOf(ByteArray(0)) }

    LaunchedEffect(Unit) {
        try {
            bytes = Res.readBytes("files/bitcoin.json")
            val jsonString = bytes.decodeToString()
            val jsonResponse = json.decodeFromString<JsonResponse>(jsonString)
            val chartDataPoints = jsonResponse.data.map { jsonPoint ->
                ChartDataPoint(
                    price = jsonPoint.priceUsd,
                    timestamp = jsonPoint.time
                )
            }
            onDataLoaded(chartDataPoints)
        } catch (e: Exception) {
            println("Error parsing JSON: ${e.message}")
            // Handle the error appropriately
        }
    }
}