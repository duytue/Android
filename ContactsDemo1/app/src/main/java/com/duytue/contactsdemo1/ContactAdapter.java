package com.duytue.contactsdemo1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflate;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by duytu on 21-May-17.
 */

class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    ArrayList<Contact> people;
    Context context;

    public ContactAdapter(Context context, ArrayList<Contact> people) {
        this.context = context;
        this.people = people;
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contact_row, parent, false);

        ContactViewHolder newContactViewHolder = new ContactViewHolder(itemView);
        return newContactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final Contact temp = people.get(position);

        holder.nameView.setText(temp.name);
        holder.emailView.setText(temp.email);
        holder.phoneView.setText(temp.phone);
        holder.profile_pic.setImageBitmap(temp.profilepic);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start Activity here
                Intent i = new Intent(context.getApplicationContext(), ContactInfo.class);
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
