package com.example.geoplay.Playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geoplay.R
import com.example.geoplay.RecommendedMusic.Song
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class PlaylistInfoFragment : Fragment() {
    private lateinit var tvSongCount: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_info, container, false)
        tvSongCount = view.findViewById(R.id.tvSongCount)

        val activity = requireActivity() as PlaylistActivity
        activity.loadPlaylistSongs { songs ->
            val songsSize = songs?.size ?: 0
            tvSongCount.text = "Pjesama: $songsSize"
        }

        return view
    }
}

