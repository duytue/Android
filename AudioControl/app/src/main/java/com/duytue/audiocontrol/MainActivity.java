package com.duytue.audiocontrol;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mPlayer;
    AudioManager audioManager;

    public void playAudio (View view) {
        mPlayer.start();
    }

    public void pauseAudio (View view) {
        mPlayer.pause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        //set audioManager to have the system audio service
        audioManager  = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //get maximum volume of the system so that we can set the app's volume not to be higher than that
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //get the current volume of the system
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        //Volume control
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSlider);

        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curVolume);

        volumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            //moi lan muon overide OnSeekBarChangeListener, phai overide ca 3 functions
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });





        //music Control
        final SeekBar musicControl = (SeekBar) findViewById(R.id.trackSlider);
        musicControl.setMax(mPlayer.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            //nghia la no se chay function run() vao giay thu 0, va update every second
            public void run() {
                musicControl.setProgress(mPlayer.getCurrentPosition());
            }
        }, 0, 1000);

        musicControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                    musicControl.setProgress(mPlayer.getCurrentPosition());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.start();
            }
        });
    }


}
