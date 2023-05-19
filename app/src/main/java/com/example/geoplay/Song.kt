package com.example.geoplay

import java.io.Serializable


data class Song(
    val title: String,
    val artists: ArrayList<String>,
    val imageUrl: String,
    val playbackUrl: String?
) : Serializable
