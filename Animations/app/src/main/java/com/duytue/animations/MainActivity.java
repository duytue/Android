package com.duytue.animations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public void fade(View view) {
        ImageView image = (ImageView) findViewById(R.id.homerImg);
        ImageView bart  = (ImageView) findViewById(R.id.bartImg);

        image.animate()
                .rotation(1800f)
                .scaleX(1f)
                .scaleY(1f)
                .translationXBy(1000f)
                .translationYBy(1000f)
                .setDuration(2000);

       // image.animate().translationXBy(-300f).setDuration(300);
       // image.animate().translationYBy(-300f).setDuration(300);
        //image.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);

        /*
        System.out.println(image.getAlpha());
        if (image.getAlpha() == 1.0f) {
            image.animate().alpha(0.0f).setDuration(250);
            bart.animate().alpha(1f).setDuration(1000);
        }
        else
        {
            bart.animate().alpha(0f).setDuration(250);
            image.animate().alpha(1f).setDuration(1000);
        }
        */
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.homerImg);
        image.setScaleX(0.3f);
        image.setScaleY(0.3f);
        image.setTranslationX(-1000f);
        image.setTranslationY(-1000f);
    }
}
