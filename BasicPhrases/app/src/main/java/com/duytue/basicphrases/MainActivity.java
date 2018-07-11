package com.duytue.basicphrases;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    public void playAudio (View view)
    {
        MediaPlayer mPlayer;
        //getId return a integer number
        int buttonTapped = view.getId();
        String ourID;

        //function se return id (nhu trong xml) cua view
        ourID = view.getResources().getResourceEntryName(buttonTapped);

        //lay id cua resource trong folder res, raw la folder chua (in res), ten package
        int resourceID = getResources().getIdentifier(ourID, "raw", "com.duytue.basicphrases");

        mPlayer = MediaPlayer.create(this, resourceID);
        mPlayer.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
