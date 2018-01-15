package com.example.saka.moviepreview.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.saka.moviepreview.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayYoutubeTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube_trailer);

        final String developerKey = getString(R.string.DEVELOPER_KEY);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(developerKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    String getTrailerKey = getIntent().getStringExtra("movie_trailer_key");
                    youTubePlayer.loadVideo(getTrailerKey);
                }
                Log.d("app", "onInitializationSuccess");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("app", "onInitializationFailure: " + youTubeInitializationResult.toString());
            }
        });
    }

}
