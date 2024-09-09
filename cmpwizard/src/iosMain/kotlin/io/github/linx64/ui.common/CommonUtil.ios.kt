package io.github.linx64.ui.common

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun formatToPrice(price: Double): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = 5u
    formatter.maximumFractionDigits = 5u
    formatter.numberStyle = NSNumberFormatterDecimalStyle
    return formatter.stringFromNumber(NSNumber(double = price)) ?: price.toString()
}