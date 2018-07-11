package com.duytue.videodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {
    VideoView video;

    public void startVideo (View view) {
        video.start();
        ImageView button = (ImageView)findViewById(R.id.imageView);
        button.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video = (VideoView)findViewById(R.id.videoView);
        video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.csgo);
        //video.start();

        MediaController controller = new MediaController(this);
        controller.setAnchorView(video);

        video.setMediaController(controller);


    }
}
