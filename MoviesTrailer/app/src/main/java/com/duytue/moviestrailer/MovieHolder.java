package com.duytue.moviestrailer;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by duytu on 22-May-17.
 */

class MovieHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView nameView;
    ImageView posterView;

    public MovieHolder(View itemView) {
        super(itemView);

        cv = (CardView)itemView.findViewById(R.id.cv);
        nameView = (TextView)itemView.findViewById(R.id.movienameView);
        posterView = (ImageView)itemView.findViewById(R.id.posterView);
    }
}
