package com.example.geoplay.RecommendedMusic

import java.io.Serializable


data class Song(
    val title: String,
    val artists: ArrayList<String>,
    val imageUrl: String,
    val playbackUrl: String?
) : Serializable
