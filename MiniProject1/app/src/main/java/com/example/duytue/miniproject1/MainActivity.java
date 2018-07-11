package com.example.duytue.miniproject1;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Place> placeList;
    public static ArrayList<Place> favoritePlaces;
    public static PlaceAdapter mAdapter;
    public static boolean exploreTab;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // change activity
                case R.id.navigation_home:
                    exploreTab = true;
                    mAdapter = new PlaceAdapter(getApplicationContext(), placeList);
                    initiateRecyclerView();
                    return true;
                case R.id.navigation_yourplaces:
                    exploreTab = false;
                    mAdapter = new PlaceAdapter(getApplicationContext(), favoritePlaces);
                    initiateRecyclerView();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exploreTab = true;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /*
        // Create new database and store list
        try {
            //SQLiteDatabase mainDB = this.openOrCreateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Use google place API


        // Manually create data samples
        */

        createToolbar();


        if (placeList == null)
            initiateDataList();

        mAdapter = new PlaceAdapter(this, placeList);

        initiateRecyclerView();


    }

    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //return to appropriate activity
            }
        });

    }

    private void initiateRecyclerView() {
        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(layoutManager);
    }

    private void initiateDataList() {
        placeList = new ArrayList<>();

        // replace with database
        favoritePlaces = new ArrayList<>();
        getDataFromDatabase(placeList);
    }

    private void getDataFromDatabase(ArrayList<Place> placeList) {
        //example



        placeList.add(new Place("Bến Thành Market", new String[]{"cho ben thanh", "Chợ Bến Thành"}, "Well-known standby for handicrafts, souvenirs, clothing & other goods along with local eats.", "http://www.chobenthanh.org.vn", "Lê Lợi, Cửa Nam Chợ Bến Thành, Bến Thành, Quận 1", "+842838299274", "", BitmapFactory.decodeResource(getResources(), R.drawable.cho_ben_thanh), new LatLng(10.7720634361544,106.69868202924728), new String[]{"Market"}));

        placeList.add(new Place("Nhà thờ chính tòa Đức Bà", new String[]{"nha tho duc ba", "church"}, "1880s Catholic cathedral built with French bricks & featuring 58m-tall Romanesque bell towers.", "https://giothanhle.net/gio-le/nha-tho-duc-ba-sai-gon", "1, Công xã Paris, Bến Nghé, Quận 1", "+842838220477", "", BitmapFactory.decodeResource(getResources(), R.drawable.ntdb), new LatLng(10.7790634361544,106.69868202924728), new String[]{"Church", "Architecture"}));

        placeList.add(new Place("Dinh Độc Lập", new String[]{"dinh doc lap", "toa nha dinh doc lap", "museum"}, "Historical building of the Vietnam War era, with tours of government offices, war rooms & artifacts", "http://www.dinhdoclap.gov.vn", "135 Nam Kỳ Khởi Nghĩa, Bến Thành, Quận 1", "+841647572172", "", BitmapFactory.decodeResource(getResources(), R.drawable.dinh_doc_lap_lq), new LatLng(10.7770634361544,106.69568202924728), new String[]{"Museum", "Architecture"}));

        placeList.add(new Place("Ho Chi Minh City University of Science", new String[]{"dai hoc khoa hoc tu nhien", "Đại học khoa học tự nhiên"}, "Ho Chi Minh City University of Science was re-founded in March 1996 following a split from Đại học Tổng hợp. The school has since been a member university of Vietnam National University, Ho Chi Minh City.", "http://www.hcmus.edu.vn", "227, Nguyễn Văn Cừ, Quận 5", "+8402862884499", "https://www.youtube.com/watch?v=D2OKUp8KREs", BitmapFactory.decodeResource(getResources(), R.drawable.khtn), new LatLng(10.7626391, 106.6820268), new String[]{"University"}));
        downloadThumbnails();
    }

    private void downloadThumbnails() {
        DownloadTask downloadTask = new DownloadTask();
        String[] urls = new String[placeList.size()];
        Log.i("urlslength", Integer.toString(urls.length));
        for (int i = 0; i < urls.length; ++i) {
            urls[i] = placeList.get(i).VideoURL;
        }
        downloadTask.execute(urls);
    }
}
