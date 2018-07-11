package com.duytue.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    int player = 0;
    int[] usedTiles = {2,2,2,2,2,2,2,2,2};
    int[][] winningCondition = {{0,1,2}, {3,4,5}, {6,7,8}, {0,4,8}, {2,4,6}, {0,3,6}, {1,4,7}, {2,5,8}};
    boolean finished = false;
    boolean draw = false;
    boolean gameActive = true;
    public void fadeIn(View view) {
        ImageView counter = (ImageView) view;

        int tag = Integer.parseInt(counter.getTag().toString());
        if (usedTiles[tag] == 2 && gameActive) {
            usedTiles[tag] = player;
            counter.setAlpha(0f);
            if (player == 0) {
                counter.setImageResource(R.drawable.yellow);
                counter.animate().alpha(1f).setDuration(200);
                player = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                counter.animate().alpha(1f).setDuration(200);
                player = 0;
            }
            for (int i = 0; i < 8; ++i)
            {
                if (usedTiles[winningCondition[i][0]] != 2 &&
                        usedTiles[winningCondition[i][0]] == usedTiles[winningCondition[i][1]] &&
                        usedTiles[winningCondition[i][1]] == usedTiles[winningCondition[i][2]])
                {
                    gameActive = false;
                    TextView winner = (TextView)findViewById(R.id.winMessage);

                    if (player == 0)
                        winner.setText("Red wins!");
                    else
                        winner.setText("Yellow wins!");

                    LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);

                    finished = true;
                }
            }
            if (!finished) {
                for (int i = 0; i < 9; ++i) {
                    if (usedTiles[i] == 2)
                        break;
                    if (i == 8)
                        draw = true;
                }
                if (draw) {
                    TextView winner = (TextView)findViewById(R.id.winMessage);
                    winner.setText("Draw!");
                    LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                }
            }


        }
    }
    public void playAgain(View view) {
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);

        player = 0;
        finished = false;
        draw = false;
        gameActive = true;
        for (int i = 0; i < 9; ++i)
            usedTiles[i] = 2;

        GridLayout grid = (GridLayout)findViewById(R.id.board);
        for (int i = 0; i< grid.getChildCount(); ++i)
        {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
