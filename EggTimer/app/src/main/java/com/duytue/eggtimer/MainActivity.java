package com.duytue.eggtimer;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    boolean isActive = false;
    CountDownTimer countDownTimer;
    VideoView videoView ;

    public void startTimer(View view){

        final SeekBar slider = (SeekBar)findViewById(R.id.seekBar);
        Button button = (Button)findViewById(R.id.button);

        if (!isActive) {
            button.setText("STOP");
            slider.setVisibility(View.INVISIBLE);
            isActive = true;

            final int totaltime = slider.getProgress();
            countDownTimer = new CountDownTimer(totaltime, 1000) {
                TextView min = (TextView) findViewById(R.id.minuteText);
                TextView sec = (TextView) findViewById(R.id.secondText);
                int curTime = totaltime + 100;

                public void onTick(long millisecondUntilDone) {
                    int minInt, secInt;
                    minInt = (curTime / 1000) / 60;
                    secInt = (curTime / 1000) % 60;
                    min.setText("0" + Integer.toString(minInt));
                    String secStr;
                    if (secInt == 0)
                        secStr = "00";
                    else if (secInt < 10)
                        secStr = "0" + Integer.toString(secInt);
                    else
                        secStr = Integer.toString(secInt);
                    sec.setText(secStr);

                    curTime -= 1000;
                }

                public void onFinish() {
                    sec.setText("00");
                    //mediaPlayer.start();
                    videoView.setVisibility(View.VISIBLE);
                    videoView.start();
                    Button button = (Button)findViewById(R.id.button);
                    button.setText("START");
                    slider.setVisibility(View.VISIBLE);
                    TextView min =  (TextView)findViewById(R.id.minuteText);
                    TextView sec = (TextView)findViewById(R.id.secondText);
                    min.setText("00");
                    sec.setText("30");
                    slider.setProgress(30000);
                    isActive = false;
                }
            }.start();
        }
        else if (button.getText().toString().equals("STOP"))
        {
            countDownTimer.cancel();
            button.setText("START");
            slider.setVisibility(View.VISIBLE);
            TextView min =  (TextView)findViewById(R.id.minuteText);
            TextView sec = (TextView)findViewById(R.id.secondText);
            min.setText("00");
            sec.setText("30");
            slider.setProgress(30000);
            isActive = false;
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.stopvideo);


        SeekBar slider = (SeekBar)findViewById(R.id.seekBar);
        slider.setMax(300000);
        slider.setProgress(30000);

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView min =  (TextView)findViewById(R.id.minuteText);
                TextView sec = (TextView)findViewById(R.id.secondText);
                int time = 0, minInt = 0, secInt = 0;
                minInt = (progress / 1000) / 60;
                secInt = (progress / 1000) % 60;
                min.setText("0" + Integer.toString(minInt));
                String secStr;
                if (secInt == 0)
                    secStr = "00";
                else if (secInt < 10)
                    secStr = "0" + Integer.toString(secInt);
                else
                    secStr = Integer.toString(secInt);
                sec.setText(secStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
