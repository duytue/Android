package com.duytue.calculator1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public void calculate(View v) {
        int in1, in2, result = 0;
        EditText input1, input2, resultView;
        input1 = (EditText)findViewById(R.id.input1);
        input2 = (EditText)findViewById(R.id.input2);

        if (input1.getText().toString().equals("") || input2.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
        } else {

            in1 = Integer.parseInt(input1.getText().toString());
            in2 = Integer.parseInt(input2.getText().toString());

            boolean inf = false;

            if (v == findViewById(R.id.buttonPlus)) {
                result = in1 + in2;
            } else if (v == findViewById(R.id.buttonSub)) {
                result = in1 - in2;
            } else if (v == findViewById(R.id.buttonMul)) {
                result = in1 * in2;
            } else if (v == findViewById(R.id.buttonDiv)) {
                if (in2 != 0)
                    result = in1 / in2;
                else
                    inf = true;
            }

            resultView = (EditText) findViewById(R.id.output);
            if (!inf)
                resultView.setText(Integer.toString(result));
            else
                resultView.setText("INF");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
