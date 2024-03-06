package com.mobis.media3.client.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.mobis.media3.client.Media3MainActivity

class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, exoPlayer)
            .setSessionActivity(
                PendingIntent.getActivity(this, 0,
                Intent(this, Media3MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE))
            .build()

        val mediaItem = MediaItem.fromUri("https://media.w3.org/2010/07/bunny/04-Death_Becomes_Fur.mp4")
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
        exoPlayer.release()
    }
}