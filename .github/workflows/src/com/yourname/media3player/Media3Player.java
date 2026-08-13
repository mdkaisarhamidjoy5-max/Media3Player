package com.wealrock.media3player;

import android.content.Context;
import android.view.View;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.ui.PlayerView;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;

import java.util.HashMap;
import java.util.Map;

@UnstableApi
@DesignerComponent(
        version = 1,
        description = "Media3 ExoPlayer with Custom Headers for HLS Streams",
        category = ComponentCategory.EXTENSION,
        nonVisible = false
)
@SimpleObject(external = true)
public class Media3Player extends AndroidViewComponent {

    private final Context context;
    private final PlayerView playerView;
    private ExoPlayer player;
    private String userAgent = "Mozilla/5.0 (Linux; Android 10; Mobile)";
    private String referer = "";

    public Media3Player(ComponentContainer container) {
        super(container);
        this.context = container.$context();
        
        // UI Player View
        playerView = new PlayerView(context);
        player = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(player);
    }

    @Override
    public View getView() {
        return playerView;
    }

    // Set Custom User-Agent
    @SimpleProperty(description = "Set Custom User Agent")
    public void UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    // Set Custom Referer
    @SimpleProperty(description = "Set Custom Referer Header")
    public void Referer(String referer) {
        this.referer = referer;
    }

    // Play Stream Function
    @SimpleFunction(description = "Play Stream with Headers")
    public void PlayStream(String url) {
        Map<String, String> headers = new HashMap<>();
        if (!referer.isEmpty()) {
            headers.put("Referer", referer);
        }

        DefaultHttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                .setUserAgent(userAgent)
                .setDefaultRequestProperties(headers)
                .setAllowCrossProtocolRedirects(true);

        HlsMediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url));

        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();
    }

    // Pause Stream
    @SimpleFunction(description = "Pause Video")
    public void Pause() {
        if (player != null) player.pause();
    }

    // Stop Stream
    @SimpleFunction(description = "Stop Video")
    public void Stop() {
        if (player != null) player.stop();
    }
}

