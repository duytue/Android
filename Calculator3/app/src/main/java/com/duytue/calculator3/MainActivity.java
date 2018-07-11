package com.duytue.calculator3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button  btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnpls, btnsub, btndiv, btnmul, btnclr, btneql;
    TextView textView, signView;
    int operand1, operand2, result, operator;

    boolean onProcess, eqlJustTapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn0 = (Button)findViewById(R.id.number0);
        btn1 = (Button)findViewById(R.id.number1);
        btn2 = (Button)findViewById(R.id.number2);
        btn3 = (Button)findViewById(R.id.number3);
        btn4 = (Button)findViewById(R.id.number4);
        btn5 = (Button)findViewById(R.id.number5);
        btn6 = (Button)findViewById(R.id.number6);
        btn7 = (Button)findViewById(R.id.number7);
        btn8 = (Button)findViewById(R.id.number8);
        btn9 = (Button)findViewById(R.id.number9);
        btnpls = (Button)findViewById(R.id.plus);
        btnsub = (Button)findViewById(R.id.subtract);
        btndiv = (Button)findViewById(R.id.div);
        btnmul = (Button)findViewById(R.id.multi);
        btnclr = (Button)findViewById(R.id.clear);
        btneql = (Button)findViewById(R.id.equal);

        textView = (TextView)findViewById(R.id.textView);
        signView = (TextView)findViewById(R.id.signView);

        SetClickListeners();
        operator = 0;
        result = 0;

        onProcess = false;
        eqlJustTapped = false;      //after pressing =, make the result go away if tapped a new number
    }

    private void SetClickListeners() {
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("0");
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("1");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("2");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("4");
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("5");
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("6");
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("7");
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("8");
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTapped("9");
            }
        });

        btnclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(true);
            }
        });

        btnpls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operandTapped(1);
            }
        });

        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operandTapped(2);
            }
        });

        btnmul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operandTapped(3);
            }
        });

        btndiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operandTapped(4);
            }
        });

        btneql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
                clear(false);
                eqlJustTapped = true;
            }
        });

    }

    private void calculate() {
        if (operator != 0) {
            operand2 = Integer.parseInt(textView.getText().toString());
            Log.i("operand1", Integer.toString(operand1));
            switch (operator) {
                case 1:
                    result = operand1 + operand2;
                    break;
                case 2:
                    result = operand1 - operand2;
                    break;
                case 3:
                    result = operand1 * operand2;
                    break;
                case 4:
                    if (operand2 != 0)
                        result = operand1 / operand2;
                    else
                        textView.setText("∞");
                    break;

            }
            if (!textView.getText().equals("∞"))
                textView.setText(Integer.toString(result));
        }
    }

    private void clear(boolean clrBtnTapped) {
        if (clrBtnTapped)
            textView.setText("");
        operator = 0;
        operand1 = 0;
        operand2 = 0;
        result = 0;
        signView.setText("");
        onProcess = false;
    }

   private void NumberTapped(String c) {
       if (eqlJustTapped) {
           textView.setText(c);
           eqlJustTapped = false;
       }
       else
        textView.setText(textView.getText().toString() + c);
   }

   private void operandTapped(int op) {
       if (!textView.getText().toString().equals("")) {
           if (onProcess) {
               calculate();
               System.out.println("Operand1: " + operand1);
               System.out.println("Operand2: " + operand2);
               System.out.println("Result: " + result);
               operand1 = result;
           }
           operator = op;
           setSignView(op);
           operand1 = Integer.parseInt(textView.getText().toString());
           textView.setText("");
       } else if (op == 2) {
           textView.setText("-");
       }
       onProcess = true;
   }

    private void setSignView(int op) {
        switch (op) {
            case 1:
                signView.setText("ADD");
                break;
            case 2:
                signView.setText("SUB");
                break;
            case 3:
                signView.setText("MUL");
                break;
            case 4:
                signView.setText("DIV");
                break;
        }
    }

}
