package com.duytue.contactsdemo1;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

     static ArrayList<Contact> people;
    //ArrayList<String> profilePicID;


    static ContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mAdapter = new ContactAdapter(this, people);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mAdapter);
    }

    private void init() {
        people = new ArrayList<Contact>();
        Contact a = new Contact("Duy Tuệ Trần Văn", "duytue289@gmail.com", "01647572172", BitmapFactory.decodeResource(getResources(),R.drawable.myprofile_pic));
        people.add(a);
        people.add(new Contact("Duy Tuệ Trần Văn", "tvdtue@apcs.vn", "01647572172", BitmapFactory.decodeResource(getResources(),R.drawable.default_pic)));

        people.add(new Contact("Duy Tuệ Trần Văn", "duytue997@yahoo.com", "01647572172", BitmapFactory.decodeResource(getResources(),R.drawable.default_pic)));

    }


}
