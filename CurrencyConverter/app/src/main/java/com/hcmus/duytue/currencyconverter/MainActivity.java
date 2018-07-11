package com.hcmus.duytue.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void toVND(View view) {
        EditText inputUSD = (EditText) findViewById(R.id.dollarInput);

        Log.i("dollar Input", inputUSD.getText().toString());

        Float usd = Float.parseFloat(inputUSD.getText().toString());
        Float vnd = usd*22000;

        String vndStr = Float.toString(vnd);
        Toast.makeText(getApplicationContext(), vndStr, Toast.LENGTH_LONG).show();
    }

    public void toUSD(View view){
        EditText inputVND = (EditText) findViewById(R.id.vndInput);

        Float vnd = Float.parseFloat(inputVND.getText().toString());
        Float usd = vnd/22000;

        String usdStr = Float.toString(usd);
        Toast.makeText(getApplicationContext(), usdStr, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
