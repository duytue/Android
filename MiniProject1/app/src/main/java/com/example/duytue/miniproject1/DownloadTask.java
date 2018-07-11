package com.example.duytue.miniproject1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import static com.example.duytue.miniproject1.MainActivity.placeList;
/**
 * Created by duytue on 6/17/17.
 */

public class DownloadTask extends AsyncTask<String, Void, Bitmap> {

    String VIDEO_THUMBNAIL_LINK = "http://img.youtube.com/vi/";
    String THUMBNAIL_HD = "/hqdefault.jpg";
    String THUMBNAIL_SD = "/mqdefault.jpg";


    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            Bitmap temp;
            for (int i = 0; i < urls.length; ++i) {
                if (!urls[i].equals("")) {
                    URL url = new URL(createThumbnailLink(urls[i]));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    temp = BitmapFactory.decodeStream(inputStream);
                    Log.i("DownloadTask", urls[i]);
                    if (temp != null)
                        placeList.get(i).VideoThumbnail = temp;
                    else
                        Log.i("Bitmap", "is NULL");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.i("DownloadTask", "Finished");

    }

    private String createThumbnailLink(String url) {
        return VIDEO_THUMBNAIL_LINK + url.substring(url.indexOf("=") + 1) + THUMBNAIL_HD;
    }
}
