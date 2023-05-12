package com.example.geoplay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class SongPlayer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val songTitle = intent.getStringExtra("title")
        val songArtist = intent.getStringExtra("artist")
        setContent {
            Column(
                modifier = Modifier
                    .background(Color(0xFF1B1B1A))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .size(600.dp)
                            .padding(64.dp)
                            .align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                                .size(260.dp)
                                .align(Alignment.Center)
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = songTitle!!,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                )
                                Text(
                                    text = songArtist!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                )
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Previous",
                                        tint = Color.White,
                                        modifier = Modifier.size(47.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(47.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Previous",
                                        tint = Color.White,
                                        modifier = Modifier.size(47.dp)
                                    )
                                }
                            }
                        }
                    }
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .align(Alignment.TopStart)
                            .clickable { onBackClicked(this@SongPlayer) },
                        tint = Color.White
                    )
                }
            }
        }
    }
}

fun onBackClicked(context: Context) {
    val intent = Intent(context, SongPlayer::class.java)
    context.startActivity(intent)
}