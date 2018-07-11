package com.hcmus.duytue.askforname;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class getName extends AppCompatActivity {

    public void buttonClicked(View view)
    {
      final ImageView img = (ImageView) findViewById(R.id.imageView3);
        img.setTag(1);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if ((int)img.getTag() == 1) {
                    img.setImageResource(R.drawable.wolf);
                    img.setTag(2);
                }
                else {
                    img.setImageResource(R.drawable.owl);
                    img.setTag(1);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);
    }
}
