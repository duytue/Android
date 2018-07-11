package com.example.duytue.miniproject1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by duytue on 6/17/17.
 */

public class PlaceViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView, bookmarkView;
    TextView nameView, descView; //addressView, telephoneView;
    CardView cardView;



    public PlaceViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView)itemView.findViewById(R.id.cardView);
        imageView = (ImageView)itemView.findViewById(R.id.imageView);
        nameView = (TextView)itemView.findViewById(R.id.nameView);
        //addressView = (TextView)itemView.findViewById(R.id.addressView);
        //telephoneView = (TextView)itemView.findViewById(R.id.telephoneView);
        descView = (TextView)itemView.findViewById(R.id.descView);
        bookmarkView = (ImageView)itemView.findViewById(R.id.bookmarkView);
    }
}
