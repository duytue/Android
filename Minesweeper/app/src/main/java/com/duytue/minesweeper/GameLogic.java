package com.duytue.minesweeper;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import static com.duytue.minesweeper.MainActivity.bombNum;
import static com.duytue.minesweeper.MainActivity.gameActive;
import static com.duytue.minesweeper.MainActivity.rowCount;
import static com.duytue.minesweeper.MainActivity.colCount;
import static com.duytue.minesweeper.MainActivity.bombMap;
import static com.duytue.minesweeper.MainActivity.firstTile;
import static com.duytue.minesweeper.MainActivity.gridLayout;
import static com.duytue.minesweeper.MainActivity.timerView;

/**
 * Created by duytu on 17-May-17.
 */

// NOTE
// I THINK ITS BETTER TO CREATE onClickListener IN MAINACTIVITY
// SHOULD MIGRATE FUNCTIONS TO GAME LOGIC CLASS
// This class is just temporary, i will replace it with a proper class type

class GameLogic implements View.OnClickListener {

    Context context;

    public GameLogic(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (gameActive) {
            ImageView b = (ImageView) v;

            if (b.getTag().equals("flagged"))
                return;


            int row, col;
            row = b.getId() / 10000;
            col = b.getId() % 10000;

            if (firstTile) {
                //insert genGame with bombs here
                firstTile = false;
                genGameWithBomb(row, col);

                timerView.setBase(SystemClock.elapsedRealtime());
                timerView.start();
            }

            //int status = replaceImage(b, row, col);

            // function to expand bomb-cleared region
            int status = floodFill(row, col);

            if (status == -1) {
                gameActive = false;
               revealAllBomb();
                displayMessage();
               MainActivity.displayRestartButton();
                timerView.stop();
            }

        }
    }

    private int floodFill(int row, int col) {
        ImageView b = (ImageView)gridLayout.getChildAt(row*colCount + col);
        if (b.getTag().toString().equals("closed")) {
            int status = replaceImage(b, row, col);
            if (status == -1)
                return status;
            else {
                //if cell doesnt have neighbor bombs
                if (scanNear(row, col) == 0) {
                    for (int i = row - 1 ; i < row +2; ++i)
                        for (int j = col - 1 ; j < col + 2; ++j)
                            if (i >= 0 && i < rowCount && j >=0 && j < colCount)
                                if (i != row || j != col)
                                    floodFill(i, j);
                }
                return 0;
            }
        }
        return 0;
    }

    private int replaceImage(ImageView v, int row, int col) {
        int num = bombMap[row][col];
        v.setTag("opened");

        switch (num) {
            case -1:
                //gameActive = false;
                v.setImageResource(R.drawable.square_mine);
                return -1;
            case 0:
                v.setImageResource(R.drawable.square0);
                break;
            case 1:
                v.setImageResource(R.drawable.square1);
                break;
            case 2:
                v.setImageResource(R.drawable.square2);
                break;
            case 3:
                v.setImageResource(R.drawable.square3);
                break;
            case 4:
                v.setImageResource(R.drawable.square4);
                break;
            case 5:
                v.setImageResource(R.drawable.square5);
                break;
            case 6:
                v.setImageResource(R.drawable.square6);
                break;
            case 7:
                v.setImageResource(R.drawable.square7);
                break;
            case 8:
                v.setImageResource(R.drawable.square8);
                break;

        }
        if (num != -1)
            num = 0;
        return num;
    }

    //firstX, firstY is the first tile touched, hence there is no bomb
    private void genGameWithBomb(int firstX, int firstY) {
        int row, col;
        row = rowCount;
        col = colCount;

        initMap(row, col);

        placeBomb(firstX, firstY);

        //mark unbombed tile with number
        createMapHint();
    }

    private void initMap(int row, int col) {
        bombMap = new int[row][col];
        for (int i = 0; i < row; ++i)
            for (int j = 0; j < col; ++j) {
                bombMap[i][j] = 0;
            }
    }

    private void placeBomb(int firstX, int firstY) {
        Random random = new Random();
        int currentBomb = 0;
        int x, y;
        while (currentBomb < bombNum) {
            x = random.nextInt(rowCount);
            y = random.nextInt(colCount);

            if (checkAvoidFirstTile(x, y, firstX, firstY)) {

            } else

                if (bombMap[x][y] != -1) {
                    bombMap[x][y] = -1;
                    ++currentBomb;
                }
        }
    }

    private boolean checkAvoidFirstTile(int x, int y, int firstX, int firstY) {
        if ((x == firstX - 1 && y == firstY - 1)
                || (x == firstX - 1 && y == firstY)
                || (x == firstX - 1 && y == firstY + 1)
                || (x == firstX && y == firstY - 1)
                || (x == firstX && y == firstY)
                || (x == firstX && y == firstY + 1)
                || (x == firstX + 1 && y == firstY - 1)
                || (x == firstX + 1 && y == firstY)
                || (x == firstX + 1 && y == firstY + 1))
            return true;
        return false;
    }

    private void createMapHint() {
        int row, col;
        row = rowCount;
        col = colCount;

        for (int i = 0; i < row; ++i)
            for (int j = 0; j < col; ++j) {
                if (bombMap[i][j] != -1) {
                    bombMap[i][j] = scanNear(i, j);
                }
            }
    }

    private int scanNear(int i, int j) {
        int bombs = 0;


        bombs += checkBombAt(i - 1, j - 1);
        bombs += checkBombAt(i - 1, j);
        bombs += checkBombAt(i - 1, j + 1);
        bombs += checkBombAt(i, j - 1);
        bombs += checkBombAt(i, j + 1);
        bombs += checkBombAt(i + 1, j - 1);
        bombs += checkBombAt(i + 1, j);
        bombs += checkBombAt(i + 1, j + 1);


        return bombs;
    }

    private int checkBombAt(int i, int j) {
        if (i >= 0 && i < rowCount && j >=0 && j < colCount && bombMap[i][j] == -1)
            return 1;
        return 0;
    }

    private void revealAllBomb() {
        for (int i = 0 ; i < rowCount; ++i)
            for (int j = 0 ; j < colCount; ++j) {
                if (bombMap[i][j] == -1) {
                    int id = i * 10000 + j;
                    ImageView v = (ImageView)gridLayout.getChildAt(i*colCount + j);
                    replaceImage(v, i, j);
                }
            }
    }

    private void displayMessage() {
        Toast.makeText(context, "Sorry! You hit a bomb.", Toast.LENGTH_SHORT).show();
    }

}
