package com.duytue.minesweeper;

import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static GridLayout gridLayout;
    static Button restart;
    static int rowCount, colCount, bombNum;
    static int[][] bombMap;
    static boolean firstTile, gameActive;
    static Chronometer timerView;


    GameLogic gameLogic;
    int screenWidth, screenHeight;
    TextView bombView;
    boolean presetGame;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatherinfo);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        gameLogic = new GameLogic(this);
        presetGame = false;
    }

    public void startGameActivity(View view) {
        if (!presetGame) {
            rowCount = getRowCount();
            colCount = getColCount();
            if (rowCount < 5 || colCount < 5) {
                Toast.makeText(getApplicationContext(), "Both numbers must be at least 5!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rowCount > colCount) {
                int temp = rowCount;
                rowCount = colCount;
                colCount = temp;
            }
        }


        firstTile = true;
        gameActive = true;
        bombNum = (rowCount*colCount / 5) / 5 * 5;


        //insert handler here to make timer works

        setContentView(R.layout.activity_main);
        restart = (Button)findViewById(R.id.restartButton);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        timerView = (Chronometer) findViewById(R.id.timeView);
        bombView = (TextView)findViewById(R.id.bombView);
        bombView = (TextView)findViewById(R.id.bombView);
        bombView.setText(Integer.toString(bombNum));
        timerView.setBase(SystemClock.elapsedRealtime());

        generateGame(rowCount, colCount);
    }


    private int getRowCount() {

        EditText editText = (EditText)findViewById(R.id.rowCountView);
        String count = editText.getText().toString();
        if (count.equals(""))
            return 0;
        int row = Integer.parseInt(editText.getText().toString());
        return row;
    }

    private int getColCount() {
        EditText editText = (EditText)findViewById(R.id.colCountView);
        String count = editText.getText().toString();
        if (count.equals(""))
            return 0;
        int col = Integer.parseInt(editText.getText().toString());
        return col;
    }

    private void generateGame(int row, int col) {
        setGridAttr(row, col);
        for (int i = 0 ; i < row; ++i)
            for (int j = 0; j < col; ++j) {
                ImageView b = createButtonAt(i, j);
                insertButtonAt(i, j, b);
            }
    }

    private ImageView createButtonAt(int i, int j) {
        ImageView b = new ImageView(getApplicationContext());
        b.setLongClickable(true);



        b.setImageResource(R.drawable.square);
        b.setId(i*10000+j);
        b.setTag("closed");

        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (gameActive) {
                    ImageView b = (ImageView)v;

                    if (b.getTag().toString().equals("opened")) {
                        System.out.println(b.getTag().toString());
                    }

                    else if (b.getTag().toString().equals("closed")) {
                        b.setImageResource(R.drawable.square_flag);
                        b.setTag("flagged");
                        String bomb = bombView.getText().toString();
                        int bombint = Integer.parseInt(bomb);
                        bombView.setText(Integer.toString(bombint -1));

                        if ((bombint - 1) == 0) {
                            if (checkWinningCondition()) {
                                Toast.makeText(getApplicationContext(), "Congratulation!", Toast.LENGTH_SHORT).show();
                                restart.setVisibility(View.VISIBLE);
                                timerView.stop();
                                gameActive = false;
                            }
                        }
                    }

                    else if (b.getTag().toString().equals("flagged")) {
                        b.setImageResource(R.drawable.square);
                        b.setTag("closed");
                        String bomb = bombView.getText().toString();
                        int bombint = Integer.parseInt(bomb);
                        bombView.setText(Integer.toString(bombint + 1));
                    }
                    return true;
                }
                return false;
            }
        });


        GridLayout.LayoutParams params = new GridLayout.LayoutParams();



        params.width = screenWidth / colCount;
        params.height = params.width;

        params.columnSpec = GridLayout.spec(j);
        params.rowSpec = GridLayout.spec(i);

        b.setLayoutParams(params);
        b.setOnClickListener(gameLogic);
        return b;
    }

    private boolean checkWinningCondition() {
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                if (bombMap[i][j] == -1) {
                    ImageView v = (ImageView)gridLayout.getChildAt(i*colCount + j);
                    if (!v.getTag().toString().equals("flagged")) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void setGridAttr(int row, int col) {
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(col);
        gridLayout.setRowCount(row);
    }

    private void insertButtonAt(int i, int j, ImageView b){
        gridLayout.addView(b);
    }

   public void restartGame(View v) {
       firstTile = true;
       gameActive = true;
       restart.setVisibility(View.INVISIBLE);
       setContentView(R.layout.activity_gatherinfo);
       presetGame = false;
       timerView.refreshDrawableState();
   }

   static public void displayRestartButton() {
       restart.setVisibility(View.VISIBLE);
    }

    public void presetGenGame(View view) {
        presetGame = true;
        int a = Integer.parseInt(view.getTag().toString());
        rowCount = a;
        colCount = a;
        startGameActivity(view);
    }

    public void displayInstruction(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Instruction")
                .setMessage("Tap a tile to open it.\n" +
                        "Long tap will flag a tile.\n" +
                        "Long tap a flagged tile will unflag it.\n" +
                        "Flag all bombs to win.")
                .setPositiveButton("Close", null)
                .show();

        }
}
