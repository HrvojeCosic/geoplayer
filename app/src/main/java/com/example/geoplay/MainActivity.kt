package com.example.geoplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.geoplay.ui.theme.GeoPlayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoPlayTheme {
                RecommendedMusic(
                    listOf<Song>(
                        Song("song1", "artist1"),
                        Song("song2", "artist1"),
                        Song("song3", "artist2"),
                        Song("song4", "artist3"),
                        Song("song5", "artist2"),
                        Song("song6", "artist3")
                    )
                )
            }
        }
    }
}

data class Song(
    val title: String,
    val artist: String,
)
