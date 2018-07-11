package com.duytue.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Events", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR, year INT(4))");
            //sql syntax                name   varname - type, varname2 - type

            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('Birth', 1997)");
            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('HN', 1000)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM events", null);
            //select all(*)
            int eventnameIndex = c.getColumnIndex("name");
            int yearIndex = c.getColumnIndex("year");

            c.moveToFirst();
            while (c != null) {
                Log.i("Event", c.getString(eventnameIndex));
                Log.i("Event", Integer.toString(c.getInt(yearIndex)));
                c.moveToNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
