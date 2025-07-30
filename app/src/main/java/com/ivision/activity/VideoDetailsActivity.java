package com.ivision.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ivision.R;
import com.ivision.utils.Common;

public class VideoDetailsActivity extends YouTubeBaseActivity {

    private String videoId = "";
    private Context context;
    private YouTubePlayerView playerView;
    private ImageView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        context = VideoDetailsActivity.this;

        playerView = findViewById(R.id.youTubePlayerView);
        cardView = findViewById(R.id.cvBack);

        if (getIntent() != null) {
            if (getIntent().hasExtra("videoId")) {
                videoId = getIntent().getStringExtra("videoId");
            }
        }

        Common.showProgressDialog(context, getString(R.string.buffering));

        playerView.initialize(String.valueOf(R.string.API_KEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Common.hideProgressDialog();
                youTubePlayer.loadVideo(videoId, 0);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Common.hideProgressDialog();
                Common.showToast("Video loaded failed");
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}