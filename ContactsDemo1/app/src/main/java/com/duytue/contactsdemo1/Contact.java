package com.duytue.contactsdemo1;

import android.graphics.Bitmap;

/**
 * Created by duytu on 21-May-17.
 */

class Contact {
    String name, email, phone;
    Bitmap profilepic;
    //String phoneNumber;
    // Drawable pic;

    public Contact(String name, String email, String phone, Bitmap profilepic) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilepic = profilepic;
    }
}
