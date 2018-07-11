package com.duytue.contactsdemo1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by duytu on 21-May-17.
 */

class ContactViewHolder extends RecyclerView.ViewHolder {

    ImageView profile_pic;
    TextView nameView, emailView, phoneView;
    CardView cardView;

    public ContactViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        profile_pic = (ImageView) itemView.findViewById(R.id.profileImage);
        nameView = (TextView) itemView.findViewById(R.id.profileName);
        emailView = (TextView) itemView.findViewById(R.id.profileEmail);
        phoneView = (TextView) itemView.findViewById(R.id.phoneView);
    }
}
