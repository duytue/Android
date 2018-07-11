package com.example.duytue.miniproject1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.duytue.miniproject1.MainActivity.exploreTab;
import static com.example.duytue.miniproject1.MainActivity.favoritePlaces;
import static com.example.duytue.miniproject1.MainActivity.mAdapter;
import static com.example.duytue.miniproject1.MainActivity.placeList;
public class InformationActivity extends AppCompatActivity {


    ImageView imageView, videoView;
    TextView nameView, descView, telephoneView, websiteView, addressView;
    String name, description, telephone, website, address;
    Bitmap image, thumbnail;

    int position;
    boolean videoIsAvailable, thumbnailAvailable;

    ImageView callButton, mapButton, webButton, bookmarkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        videoIsAvailable = false;
        thumbnailAvailable = false;
        createToolbar();
        prepareInformation();
        setOnClickButtons();
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

    private void prepareInformation() {
        Intent i = getIntent();

        position = i.getIntExtra("position", 0);
        Place temp;
        if (exploreTab)
            temp = placeList.get(position);
        else
            temp = favoritePlaces.get(position);

        bindInformation(temp);
    }

    private void bindInformation(Place temp) {
        name = temp.Name;
        address = temp.Address;
        description = temp.Description;
        telephone = temp.Telephone;
        website = temp.WebsiteURL;
        image = temp.Image;
        if (temp.VideoURL != null) {
            Log.i("video is available", "true");
            videoIsAvailable = true;
            if (temp.VideoThumbnail != null) {
                thumbnail = temp.VideoThumbnail;
                thumbnailAvailable = true;
            }
        }

        bookmarkButton = (ImageView)findViewById(R.id.bookmarkInfoImageView);

        if (isSaved(temp))
            bookmarkButton.setImageResource(R.drawable.ic_star_hq);

        bindView();
    }

    private void bindView() {
        nameView = (TextView)findViewById(R.id.nameInfoView);
        telephoneView = (TextView)findViewById(R.id.telephoneInfoView);
        addressView = (TextView)findViewById(R.id.addressInfoView);
        websiteView = (TextView)findViewById(R.id.websiteInfoView);
        descView = (TextView)findViewById(R.id.descInfoView);

        videoView = (ImageView)findViewById(R.id.videoInfoView);
        imageView = (ImageView)findViewById(R.id.imageInfoView);


        nameView.setText(name);
        telephoneView.setText(telephone);
        addressView.setText(address);
        websiteView.setText(website);
        descView.setText(description);

        imageView.setImageBitmap(image);

        if (thumbnailAvailable) {
            videoView.setImageBitmap(thumbnail);
        }
        else {
            CardView cv = (CardView)findViewById(R.id.videoInfoCardView);
            cv.setVisibility(View.INVISIBLE);
        }
    }

    public void setBookmarkOnClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Tapped", "Yes");
                ImageView temp = (ImageView)view.findViewById(R.id.bookmarkInfoImageView);
                if (!isSaved(placeList.get(position))) {
                    bookmarkIt(placeList.get(position));
                    //view.setTag("saved");
                    temp.setImageResource(R.drawable.ic_star_hq);
                }
                else {
                    unBookmarkIt(placeList.get(position));
                    //view.setTag("unsaved");
                    temp.setImageResource(R.drawable.ic_star_blue_sq);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
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

    private void setOnClickButtons() {
        callButton = (ImageView)findViewById(R.id.telephoneInfoImageView);
        mapButton = (ImageView)findViewById(R.id.addressInfoImageView);
        webButton = (ImageView)findViewById(R.id.websiteInfoImageView);
        videoView = (ImageView)findViewById(R.id.videoInfoView);

        setOnClick(callButton, "ACTION_DIAL");
        setOnClick(mapButton, "ACTION_MAP");
        setOnClick(webButton, "ACTION_WEB");
        setOnClick(videoView, "ACTION_VIDEO");
    }

    private void setOnClick(View view, final String action) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place temp = placeList.get(position);
                switch (action) {
                    case "ACTION_DIAL":
                        startCallActivity(temp);
                        break;
                    case "ACTION_MAP":
                        startMapActivity(temp);
                        break;
                    case "ACTION_WEB":
                        startWebActivity(temp);
                        break;
                    case "ACTION_VIDEO":
                        startVideoActivity(temp);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void startVideoActivity(Place p) {
        if (!p.VideoURL.equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.VideoURL));
            startActivity(intent);
        }
    }

    private void startWebActivity(Place p) {
        // https://developer.android.com/guide/components/intents-common.html
        // section: Web Browser
        Uri webpage = Uri.parse(p.WebsiteURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void startMapActivity(Place p) {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        i.putExtra("ACTION", "SHOW_LOCATION");
        i.putExtra("Lat", p.Location.latitude);
        i.putExtra("Lng", p.Location.longitude);
        i.putExtra("Name", p.Name);
        startActivity(i);
    }

    private void startCallActivity(Place p) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ p.Telephone));
        startActivity(i);
    }

    // this method may slow the app down when working with huge database
    private boolean isSaved(Place p) {
        for (int i = 0 ; i < favoritePlaces.size(); ++i) {
            if (p == favoritePlaces.get(i))
                return true;
        }
        return false;
    }
}
