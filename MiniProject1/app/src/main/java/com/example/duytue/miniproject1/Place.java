package com.example.duytue.miniproject1;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by duytue on 6/17/17.
 */

public class Place {
    String Name;
    String[] alternateNames;
    String Description;
    String WebsiteURL;
    String Address;
    String Telephone;
    String[] types;

    Bitmap Image, VideoThumbnail;       //images?
    String VideoURL;
    LatLng Location;

    boolean saved;

    public Place(String name, String[] alt, String Description, String WebsiteURL, String Address, String Telephone, String VideoURL, Bitmap Image, LatLng latLng, String[] types) {
        this.Name = name;
        this.alternateNames = alt;
        this.Description = Description;
        this.WebsiteURL  = WebsiteURL;
        this.Address = Address;
        this.Telephone = Telephone;
        this.Image = Image;
        this.VideoURL = VideoURL;
        this.Location = latLng;
        this.types = types;
        saved = false;
    }
}
