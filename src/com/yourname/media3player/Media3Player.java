package com.yourname.media3player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import java.util.HashMap;
import java.util.Map;

@DesignerComponent(
        version = 1,
        description = "Custom Player with Header Support for Kodular and App Inventor",
        category = ComponentCategory.EXTENSION,
        nonVisible = false
)
@SimpleObject(external = true)
public class Media3Player extends AndroidViewComponent {

    private final VideoView videoView;
    private final Context context;
    private String userAgent = "Mozilla/5.0 (Linux; Android 10; Mobile)";
    private String referer = "";

    public Media3Player(ComponentContainer container) {
        super(container);
        this.context = container.$context();
        videoView = new VideoView(context);
    }

    @Override
    public View getView() {
        return videoView;
    }

    // Set Custom User-Agent
    @SimpleProperty(description = "Set Custom User Agent string")
    public void UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @SimpleProperty(description = "Get Custom User Agent string")
    public String UserAgent() {
        return this.userAgent;
    }

    // Set Custom Referer Header
    @SimpleProperty(description = "Set Custom Referer Header URL")
    public void Referer(String referer) {
        this.referer = referer;
    }

    @SimpleProperty(description = "Get Custom Referer Header URL")
    public String Referer() {
        return this.referer;
    }

    // Main Play Function
    @SimpleFunction(description = "Play Stream with Headers")
    public void PlayStream(final String url) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", userAgent);
            
            if (referer != null && !referer.trim().isEmpty()) {
                headers.put("Referer", referer);
            }

            Uri videoUri = Uri.parse(url);
            videoView.setVideoURI(videoUri, headers);
            
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Pause Control
    @SimpleFunction(description = "Pause Video Stream")
    public void Pause() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    // Resume Control
    @SimpleFunction(description = "Resume Video Stream")
    public void Resume() {
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
        }
    }

    // Stop Control
    @SimpleFunction(description = "Stop Video Stream")
    public void Stop() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
