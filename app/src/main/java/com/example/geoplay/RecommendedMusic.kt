package com.example.geoplay

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RecommendedMusic(songs: List<Song>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1A))
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Preporučeno",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(1.dp, 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }

        LazyColumn(
        ) {
            itemsIndexed(songs.chunked(2)) { _, songsChunk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(199.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    songsChunk.forEach { song ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp)
                        ) {
                            // Placeholder for the album cover
                            Box(
                                modifier = Modifier
                                    .size(149.dp)
                                    .background(Color.LightGray)
                                    .clickable { onSongClicked(context, song) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .height(65.dp)
                                        .background(Color.DarkGray)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(6.dp)
                                    ) {
                                        Text(
                                            text = song.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White,
                                        )
                                        Text(
                                            text = song.artist,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun onSongClicked(context: Context, song: Song) {
    val intent = Intent(context, SongPlayer::class.java)
    intent.putExtra("title", song.title)
    intent.putExtra("artist", song.artist)
    context.startActivity(intent)
}