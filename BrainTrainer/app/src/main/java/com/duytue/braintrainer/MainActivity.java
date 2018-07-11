package com.duytue.braintrainer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    GridLayout gridLayout;
    TextView scoreView, timerView, notiView, questionView;
    Button restartButton;
    boolean gameIsActive = false;
    int time = 30;
    int totalScore = 0, totalQuestion = 0;
    int answer;

    public void startGame(View view) {
        gameIsActive = true;
        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setVisibility(View.INVISIBLE);

        answer = generateQuestion();
        changeChoices();
        ++totalQuestion;

        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.VISIBLE);
        scoreView = (TextView)findViewById(R.id.scoreView);
        scoreView.setVisibility(View.VISIBLE);
        timerView = (TextView)findViewById(R.id.timerView);
        timerView.setVisibility(View.VISIBLE);;
        questionView = (TextView)findViewById(R.id.questionView);
        questionView.setVisibility(View.VISIBLE);


        new CountDownTimer(30000 + 100, 1000) {
            public void onTick(long milliUntilDone) {
                timerView.setText(Integer.toString(time));
                time--;
            }

            public void onFinish() {
                //show score and stop game
                timerView.setText("0");
                restartButton.setVisibility(View.VISIBLE);
                notiView.setText("Congrats! You got " + Integer.toString(totalScore) + " correct answers!");
                notiView.setVisibility(View.VISIBLE);
                gameIsActive = false;
            }
        }.start();
    }

    public void changeQuestion(View view) {
        if (gameIsActive) {
            TextView cur = (TextView) view;
            //int tag = Integer.parseInt(cur.getTag().toString());
            //check answer,update score
            updateScore(cur);

            //change question and answers
            answer = generateQuestion();
            changeChoices();
            ++totalQuestion;
        }
    }

    public int generateQuestion() {
        Random randomGenerator = new Random();

        int first = randomGenerator.nextInt(50) + 1;
        int second = randomGenerator.nextInt(50) + 1;
        questionView = (TextView)findViewById(R.id.questionView);
        String question = Integer.toString(first) + " + " + Integer.toString(second);
        questionView.setText(question);

        return first + second;
    }

    public void changeChoices() {
        Random randomGenerator = new Random();
        int pos = randomGenerator.nextInt(4);

        //manually get the id XD
        int firstViewID = 2131427419;
        TextView temp;
        for (int i = 0; i< 4; ++i) {
            temp = (TextView) findViewById(firstViewID + i);
            if (i == pos) {
                temp.setText(Integer.toString(answer));
            }
            else {
                int randomNumber = randomGenerator.nextInt(100)+1;
                temp.setText(Integer.toString(randomNumber));
            }
        }
    }

    public void updateScore(TextView choice) {
        if (Integer.parseInt(choice.getText().toString()) == answer)
            ++totalScore;
        String score = Integer.toString(totalScore) + "/" + Integer.toString(totalQuestion);
        scoreView.setText(score);
    }

    public void restartGame(View view) {
        totalScore = 0;
        totalQuestion = 0;
        time = 30;
        scoreView.setText("0/0");
        timerView.setText("30");
        notiView = (TextView)findViewById(R.id.notiView);
        notiView.setVisibility(View.INVISIBLE);
        restartButton = (Button)findViewById(R.id.restartButton);
        restartButton.setVisibility(View.INVISIBLE);
        startGame(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.INVISIBLE);
        scoreView = (TextView)findViewById(R.id.scoreView);
        scoreView.setVisibility(View.INVISIBLE);
        timerView = (TextView)findViewById(R.id.timerView);
        timerView.setVisibility(View.INVISIBLE);
        notiView = (TextView)findViewById(R.id.notiView);
        notiView.setVisibility(View.INVISIBLE);
        restartButton = (Button)findViewById(R.id.restartButton);
        restartButton.setVisibility(View.INVISIBLE);
        questionView = (TextView)findViewById(R.id.questionView);
        questionView.setVisibility(View.INVISIBLE);

    }
}
