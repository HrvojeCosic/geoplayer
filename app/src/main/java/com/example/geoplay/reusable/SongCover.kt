package com.example.geoplay.reusable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun SongCover(imageUrl: String) {
    val painter: Painter = rememberCoilPainter(request = imageUrl)
    Image(
        painter = painter,
        contentDescription = "Image from $imageUrl",
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(20.dp))
    )
}
