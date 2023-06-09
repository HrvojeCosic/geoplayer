package com.example.geoplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.geoplay.RecommendedMusic.RecommendedMusic
import com.example.geoplay.ui.theme.GeoPlayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoPlayTheme {
                RecommendedMusic(this)
            }
        }
    }
}