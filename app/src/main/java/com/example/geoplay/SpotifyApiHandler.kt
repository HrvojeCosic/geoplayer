package com.example.geoplay

import android.content.Context
import android.util.Log
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.SpotifySearchResult
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class SpotifyApiHandler {
    private val clientID = BuildConfig.SPOTIFY_CLIENT_ID
    private val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
    private val redirectUri = "com.example.geoplay://callback/"
    private var api: SpotifyAppApi? = null
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val connectionParams: ConnectionParams = ConnectionParams.Builder(clientID)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    fun connectToRemote(context: Context): SpotifyAppRemote? {
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d(context.toString(), "Connected!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
        return spotifyAppRemote
    }

    suspend fun buildSearchApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
    }

    suspend fun trackSearch(searchQuery: String): SpotifySearchResult {
        return api!!.search.searchAllTypes(searchQuery, 10, 1, market = Market.US)
    }
}