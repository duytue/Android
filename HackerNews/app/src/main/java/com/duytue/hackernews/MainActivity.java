package com.duytue.hackernews;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {


    Map<Integer, String> articleURLs = new HashMap<Integer, String>();
    Map<Integer, String> articleTitles = new HashMap<Integer, String>();
    ArrayList<Integer> articleIDs = new ArrayList<Integer>();

    SQLiteDatabase articleDB;


    JSONArray jsonArray;
    String content;
    boolean isMotherFunc = true;


    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();
    //ArrayAdapter arrayAdapter;


    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListView listView = (ListView)findViewById(R.id.listView);
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        //listView.setAdapter(arrayAdapter);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);


        mAdapter = new ArticleAdapter(titles, urls, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);



        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);





        articleDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articleDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, url VARCHAR, title VARCHAR, content VARCHAR)");

        newsFetcherFunc();
    }

    public void newsFetcherFunc() {
        DownloadTask newsFetcher = new DownloadTask();
        try {
            isMotherFunc = true;
            newsFetcher.execute("https://hacker-news.firebaseio.com/v0/beststories.json?print=pretty");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void articleFectcher(String content) {
        try {


            jsonArray = new JSONArray(content);

            articleDB.execSQL("DELETE FROM articles");

            for (int i = 0; i < 20; ++i) {
                String articleID = jsonArray.getString(i);
                DownloadTask newTask = new DownloadTask();
                isMotherFunc = false;
                content = newTask.execute("https://hacker-news.firebaseio.com/v0/item/" + articleID + ".json?print=pretty").get();

                JSONObject jsonObject = new JSONObject(content);

                String articleTitle, articleURL;
                articleTitle = jsonObject.getString("title");
                articleURL = jsonObject.getString("url");

                articleIDs.add(Integer.valueOf(articleID));
                articleTitles.put(Integer.valueOf(articleID), articleTitle);
                articleURLs.put(Integer.valueOf(articleID), articleURL);


                String sql = "INSERT INTO articles (articleID, title, url) VALUES (?, ?, ?)";
                SQLiteStatement statement = articleDB.compileStatement(sql);
                statement.bindString(1, articleID);
                statement.bindString(2, articleTitle);
                statement.bindString(3, articleURL);

                statement.execute();

            }

            Cursor c = articleDB.rawQuery("SELECT * FROM articles ORDER BY articleID DESC", null);  //DESC: descending order
            int articleIDIndex = c.getColumnIndex("articleID");
            int urlIndex = c.getColumnIndex("url");
            int titleIndex = c.getColumnIndex("title");

            c.moveToFirst();

            titles.clear();
            urls.clear();
          //  while (c != null) {
            for (int i =0; i < 20; ++i) {
                titles.add(c.getString(titleIndex));
                urls.add(c.getString(urlIndex));

                Log.i("Result", Integer.toString(c.getInt(articleIDIndex)));
                Log.i("URL", c.getString(urlIndex));
                Log.i("Title", c.getString(titleIndex));
                c.moveToNext();
            }
            //arrayAdapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
               // content = result;
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


            if (isMotherFunc) {
                Log.i("Mother?", "YES");
                articleFectcher(result);
            }
        }
    }

}
