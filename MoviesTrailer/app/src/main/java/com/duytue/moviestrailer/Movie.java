package com.duytue.moviestrailer;

import android.graphics.Bitmap;

/**
 * Created by duytu on 22-May-17.
 */

class Movie {
    String name, url;
    Bitmap poster;

    public Movie(String name, String url, Bitmap poster) {
        this.name = name;
        this.url = url;
        this.poster = poster;
    }
}
