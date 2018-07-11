package com.duytue.moviestrailer;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> list;

    MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        mAdapter = new MovieAdapter(this, list);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mAdapter);
    }

    private void initData() {
        list = new ArrayList<Movie>();

        list.add(new Movie("IT 2017", "https://www.youtube.com/watch?v=M_zLest7BVI&t=144s", BitmapFactory.decodeResource(getResources(), R.drawable.itposter)));
        list.add(new Movie("Transformers 5: The Last Knight", "https://www.youtube.com/watch?v=IaAoZ74WggI", BitmapFactory.decodeResource(getResources(), R.drawable.tfm5)));
    }
}
