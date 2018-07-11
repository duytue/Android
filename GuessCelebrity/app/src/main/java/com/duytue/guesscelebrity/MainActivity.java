package com.duytue.guesscelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    String htmlContent;
    ArrayList<String> celebURLS = new ArrayList<String>();
    ArrayList<String> celebNAMES = new ArrayList<String>();
    int chosen = 0;
    int correctAnswer = 0;
    String[] answers = new String[4];

    ImageView imageView;
    Button button0, button1, button2, button3;


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            htmlContent = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1)//eof
                {
                    char current = (char) data;
                    htmlContent += current;
                    data = reader.read();
                }
                return htmlContent;
            } catch (Exception e) {
                e.printStackTrace();
                return "FAILED";
            }

        }

    }

    public void startGame(View view) {
        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setVisibility(View.INVISIBLE);

        String[] splitResult = htmlContent.split("<div class=\"sidebarContainer\">");

        Pattern p = Pattern.compile("<img src=\"(.*?)\"");
        Matcher m = p.matcher(splitResult[0]);

        while (m.find()) {
            celebURLS.add(m.group(1));
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(splitResult[0]);


        while (m.find()) {
            celebNAMES.add(m.group(1));
        }
        createNewQuestion();
    }

    public void changeQuestion(View view){
        if (view.getTag().toString().equals(Integer.toString(correctAnswer))) {
            Toast.makeText(getApplicationContext(), "You're correct. Fk u!", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong MTFK! IT's " + celebNAMES.get(chosen), Toast.LENGTH_SHORT).show();
        }
        createNewQuestion();
    }

    public void createNewQuestion() {
        Random random = new Random();
        chosen = random.nextInt(celebURLS.size());

        ImageDownloader imageTask = new ImageDownloader();
        Bitmap IMG = null;
        try {
            IMG = imageTask.execute(celebURLS.get(chosen)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        imageView.setImageBitmap(IMG);

        correctAnswer = random.nextInt(4);

        for (int i = 0; i < 4; ++i) {
            if (i == correctAnswer) {
                answers[i] = celebNAMES.get(chosen);
            }
            else {
                int randomPos = random.nextInt(celebURLS.size());
                while (randomPos == chosen) {
                    randomPos = random.nextInt(celebURLS.size());
                }
                answers[i] = celebNAMES.get(randomPos);
            }
        }


        button0.setText(answers[0]);
        button1.setText(answers[1]);
        button2.setText(answers[2]);
        button3.setText(answers[3]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        DownloadTask task = new DownloadTask();
        try {
            task.execute("http://www.posh24.se/kandisar");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }
}
