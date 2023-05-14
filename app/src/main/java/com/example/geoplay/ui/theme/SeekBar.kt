package com.example.geoplay.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun SeekBar(
) {
    val progress = 0F;
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Slider(
            value = progress,
            onValueChange = { value ->
                onProgressChange(value)
            },
            valueRange = 0f..100f
        )
    }
}

fun onProgressChange(it: Float) {

}
