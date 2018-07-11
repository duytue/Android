package com.duytue.hackernews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by duytu on 10-May-17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
     public static int positionURL;

        private ArrayList<String> articleTitles;
        private ArrayList<String> articleURLs;
        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView TitleView, URLView;
            CardView cv;

            public MyViewHolder(View view) {
                super(view);
                cv = (CardView)view.findViewById(R.id.cv);
                TitleView = (TextView)view.findViewById(R.id.articleTitle);
                URLView = (TextView)view.findViewById(R.id.articleURL);
            }
        }


        public  ArticleAdapter(ArrayList<String> articleTitle, ArrayList<String> articleURL, Context context) {
            this.articleTitles = articleTitle;
            this.articleURLs = articleURL;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
                String title = articleTitles.get(position);
                final String url = articleURLs.get(position);
                holder.TitleView.setText(title);
                holder.URLView.setText(url);

            holder.cv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context.getApplicationContext(), WebActivity.class);
                    String newurl = articleURLs.get(position);
                    i.putExtra("url", newurl);
                    i.putExtra("title", articleTitles.get(position));
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return articleTitles.size();
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public String returnURL(int position) {
        return articleURLs.get(position);
    }
}
