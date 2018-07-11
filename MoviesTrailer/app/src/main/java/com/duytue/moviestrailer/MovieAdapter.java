package com.duytue.moviestrailer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by duytu on 22-May-17.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieHolder>  {
    ArrayList<Movie> list;
    Context context;


    public MovieAdapter(Context context, ArrayList<Movie> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);

        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        final Movie temp = list.get(position);

        holder.posterView.setImageBitmap(temp.poster);
        holder.nameView.setText(temp.name);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.url));

                context.startActivity(youtubeActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
