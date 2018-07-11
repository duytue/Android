package com.duytue.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    JSONArray arr;
    JSONObject jsonObject;
    TextView weatherView, descView, tempView, humidView;
    Button refreshButton;
    String cityName = "Hue";

    public void refreshInfo(View view) {
        DownloadTask refresh = new DownloadTask();
        refresh.execute("http://api.openweathermap.org/data/2.5/weather?q=Hue,vn&APPID=8be298b59e7526867c4e49950441e05a");

        refreshButton = (Button)findViewById(R.id.refreshButtonView);
        refreshButton.animate().rotation(refreshButton.getRotation() + 1800f).setDuration(2000);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");
                String main = jsonObject.getString("main");

                JSONObject mainPart = new JSONObject(main);
                tempView=  (TextView)findViewById(R.id.temparatureTextView);
                String temp = mainPart.getString("temp");
                String intTemp = "";
                for (int i = 0; i < temp.length(); ++i) {
                    if (temp.charAt(i) == '.') {
                        break;
                    }
                    intTemp += temp.charAt(i);
                }


                tempView.setText(Integer.toString(Integer.parseInt(intTemp) - 273) + "\t°C");

                humidView = (TextView)findViewById(R.id.humidTextView);
                humidView.setText(mainPart.getString("humidity") + " %");

                arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    weatherView = (TextView)findViewById(R.id.weatherCondition);
                    weatherView.setText(jsonPart.getString("main"));

                    descView = (TextView)findViewById(R.id.weatherDescription);
                    descView.setText(jsonPart.getString("description").toUpperCase());

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Floating
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DownloadTask task = new DownloadTask();
        //load default location
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=Hue,vn&APPID=8be298b59e7526867c4e49950441e05a");
        }

}
