package com.example.duytue.miniproject1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import static com.example.duytue.miniproject1.MainActivity.exploreTab;
import static com.example.duytue.miniproject1.MainActivity.favoritePlaces;
import static com.example.duytue.miniproject1.MainActivity.mAdapter;

/**
 * Created by duytue on 6/17/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {
    Context context;
    ArrayList<Place> placeList;

    public PlaceAdapter (Context context, ArrayList<Place> list) {
        this.context = context;
        placeList = list;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.place_row_info, parent, false);

        PlaceViewHolder placeViewHolder = new PlaceViewHolder(itemView);
        return placeViewHolder;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        bindCardView(holder, placeList.get(position));
        setCardViewOnClick(holder, position);
        setBookmarkOnClick(holder.bookmarkView, position);
    }

    private void setBookmarkOnClick(ImageView view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView temp = (ImageView)view.findViewById(R.id.bookmarkView);
                if (!isSaved(placeList.get(position))) {
                    bookmarkIt(placeList.get(position));
                    temp.setImageResource(R.drawable.ic_star_hq);
                }
                else  {
                    // TODO: prompt to confirm action
                    confirmAction(position, temp);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void confirmAction(final int position, final ImageView temp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder .setTitle("Confirm your action")
                .setMessage("Do you want to remove this item from Your Places?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unBookmarkIt(placeList.get(position));
                        temp.setImageResource(R.drawable.ic_star_border_black_48dp);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void unBookmarkIt(Place place) {
        for (int i = 0; i < favoritePlaces.size(); ++i) {
            if (favoritePlaces.get(i) == place) {
                favoritePlaces.remove(i);
                return;
            }
        }
    }

    private void bookmarkIt(Place place) {
        favoritePlaces.add(place);
    }

    private boolean isSaved(Place p) {
        for (int i = 0 ; i < favoritePlaces.size(); ++i) {
            if (p == favoritePlaces.get(i))
                return true;
        }
        return false;
    }

    private void setCardViewOnClick(PlaceViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaceInformationActivity(position);
            }
        });
    }

    private void startPlaceInformationActivity(int position) {
        Intent i = new Intent(context, InformationActivity.class);
        i.putExtra("position", position);
        context.startActivity(i);
    }

    private void bindCardView(PlaceViewHolder holder, Place place) {
        holder.nameView.setText(place.Name);
        //holder.addressView.setText(place.Address);
        //holder.telephoneView.setText(place.Telephone);
        holder.descView.setText(place.Description);
        holder.imageView.setImageBitmap(place.Image);
        if (exploreTab) {
            if (isSaved(place))
                holder.bookmarkView.setImageResource(R.drawable.ic_star_hq);
            else
                holder.bookmarkView.setImageResource(R.drawable.ic_star_border_black_48dp);
        } else {
            holder.bookmarkView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
