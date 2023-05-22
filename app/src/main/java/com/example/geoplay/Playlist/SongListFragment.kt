package com.example.geoplay.Playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.geoplay.R

class SongListFragment : Fragment() {
    private lateinit var songListView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)
        songListView = view.findViewById(R.id.songListView)

        var songs: MutableList<SongFirebase>?
        val activity = requireActivity() as PlaylistActivity
        activity.loadPlaylistSongs { s ->
            songs = s
            songs?.sortBy { it.song.title }
            val adapter = songs?.let { SongListAdapter(requireContext(), it) }
            songListView.adapter = adapter
        }

        return view
    }
}
