package com.example.geoplay


data class Song(
    val title: String,
    val artists: ArrayList<String>,
    val imageUrl: String,
    val playbackUrl: String?
)
