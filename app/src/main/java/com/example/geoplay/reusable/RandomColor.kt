package com.example.geoplay.reusable

import androidx.compose.ui.graphics.Color

fun loadRandomColor(): Color {
    val availableColors = arrayOf(
        Color.hsv(200F, 0.100F, 0.61F),
        Color.hsv(20F, 0.47F, 0.93F),
        Color.hsv(230F, 0.63F, 0.49F),
        Color.hsv(356F, 0.7F, 0.98F),
        Color.hsv(210F, 0.50F, 0.13F),
        Color.hsv(54F, 0.92F, 0.100F),
        Color.hsv(358F, 0.61F, 0.98F)
    )
    return availableColors[(availableColors.indices).random()]
}