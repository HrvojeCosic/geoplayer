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
import com.example.geoplay.ui.theme.SeekBar

class SongPlayer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val songTitle = intent.getStringExtra("title")
        val songArtists = intent.getStringExtra("artists")
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
                        // Album cover
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                                .size(210.dp)
                                .align(Alignment.TopCenter)
                        )

                        // Song info
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = songTitle!!,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                            )
                            Text(
                                text = songArtists!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                            )
                        }

                        // Controls
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(0.dp, 70.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SeekBar()
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