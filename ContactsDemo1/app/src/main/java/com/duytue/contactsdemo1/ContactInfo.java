package com.duytue.contactsdemo1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.duytue.contactsdemo1.MainActivity.mAdapter;
import static com.duytue.contactsdemo1.MainActivity.people;


public class ContactInfo extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 289;

    TextView emailView, nameView, phoneView;
    ImageView profilePic;
    String name, email, phone;
    Bitmap pic;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.inflateMenu(R.menu.edit_profile_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_pic)
                {
                    // edit pic
                    // start camera
                    startCameraIntent();


                    return true;
                }
                return false;
            }
        });


        Intent i = getIntent();

        position = i.getIntExtra("position", 0);

        Contact temp = people.get(position);

        email = temp.email;
        phone = temp.phone;
        name = temp.name;
        pic = temp.profilepic;

        emailView = (TextView)findViewById(R.id.emailTextView);
        nameView = (TextView)findViewById(R.id.infoName);
        phoneView = (TextView)findViewById(R.id.phoneTextView);
        profilePic = (ImageView)findViewById(R.id.picView);

        emailView.setText(email);
        nameView.setText(name);
        phoneView.setText(phone);
        profilePic.setImageBitmap(pic);
    }

    public void startSMSIntent(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
        startActivity(i);
    }

    public void startCallIntent(View view) {

        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else
            startActivity(i);
    }

    public void startEmailIntent(View view) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        startActivity(i);
    }


    private void startCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(photo);
            people.get(position).profilepic = photo;
            mAdapter.notifyDataSetChanged();
        }
    }
}
