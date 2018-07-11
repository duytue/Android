package com.duytue.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static Random gen = new Random();
    static int goal = gen.nextInt(100) + 1;
    public void userInput(View view) {
        int num = 0;
        EditText input = (EditText) findViewById(R.id.inputNumber);
        num = Integer.parseInt(input.getText().toString());

        Log.i("input", input.getText().toString());
        System.out.println(num);
        System.out.println(goal);

        if (num > goal){
            Toast.makeText(getApplicationContext(),"The goal is lower",Toast.LENGTH_LONG).show();
        }
        else if (num < goal) {
            Toast.makeText(getApplicationContext(),"The goal is higher", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Congratulation! You get the correct answer!\n Here's your cookies!",Toast.LENGTH_LONG).show();
        }
    }
    /*
    public static void generate() {
        Random Gen = new Random();
        goal = Gen.nextInt(100)+1;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
