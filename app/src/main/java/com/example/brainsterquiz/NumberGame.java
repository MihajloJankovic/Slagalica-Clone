package com.example.brainsterquiz;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;


public class NumberGame extends AppCompatActivity {
    private Timer singleDigitTimer;
    TextView neededNumber;
    private TextView bluePlayerNumber;
    List<TextView> allButtons;
    TextView redPlayerNumber;
    private RelativeLayout clearInput;
    TextView confirmTxt;
    TextView inputNumbers;

    LinearLayout confirmButton;
    TextView number1;
    TextView number2;
    TextView number3;
    TextView number4;
    TextView number5;
    TextView number6;
    List<TextView> numbers;
    LinearLayout number1Layout;
    LinearLayout number2Layout;
    LinearLayout number3Layout;
    LinearLayout number4Layout;
    LinearLayout number5Layout;
    LinearLayout number6Layout;
    LinearLayout additionLayout;
    LinearLayout substractionLayout;
    LinearLayout multiplicationLayout;
    LinearLayout divisionLayout;

    private TextView timer;
    private int turn;
    private int round;
    private String rName;
    private TextView rName1;

    private StringBuilder eval;
    private String bName;
    private String rScore;
    private int stopped;
    private String bScore;
    TextView addition;
    TextView subtraction;
    TextView multiplication;
    TextView division;

    LinearLayout openBracketLayout;
    LinearLayout closedBracketLayout;
    TextView openBracket;
    TextView closedBracket;
    private TextView bName1;
    private TextView rScore1;
    private TextView bScore1;
    private List<Integer> digits;
    private List<Integer> doubleDigits;
    private List<Integer> lastDigits;

    private int hint = 0;
    private int opened;
    private int posetion =1;
    private int cardPosition =1;
    private int allSubmited= 0;
    private int guessedTrue = 0;

    Map<Integer,Integer> combination=new HashMap<Integer,Integer>();
    Map<Integer,Integer> guessedCombination=new HashMap<Integer,Integer>();
    Map<Integer,Integer> temp=new HashMap<Integer,Integer>();
    CountDownTimer timera;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);
        getSupportActionBar().hide();
        digits = new ArrayList<Integer>();
        doubleDigits = new ArrayList<Integer>();
        lastDigits = new ArrayList<Integer>();
        allButtons = new ArrayList<TextView>();

        allButtons.add(number1);
        allButtons.add(number2);
        allButtons.add(number3);
        allButtons.add(number4);
        allButtons.add(number5);
        allButtons.add(number6);

        allButtons.add(addition);
        allButtons.add(subtraction);
        allButtons.add(multiplication);
        allButtons.add(division);
        allButtons.add(openBracket);
        allButtons.add(closedBracket);

        eval = new StringBuilder();

        digits.add(1);
        digits.add(2);
        digits.add(3);
        digits.add(4);
        digits.add(5);
        digits.add(6);
        digits.add(7);
        digits.add(8);
        digits.add(9);

        doubleDigits.add(10);
        doubleDigits.add(15);
        doubleDigits.add(20);

        lastDigits.add(25);
        lastDigits.add(50);
        lastDigits.add(100);

        db = FirebaseFirestore.getInstance();

        numbers = new ArrayList<TextView>();

        numbers.add(number1);
        numbers.add(number2);
        numbers.add(number3);
        numbers.add(number4);
        numbers.add(number5);
        numbers.add(number6);

        this.opened = 0;
        this.turn = 3;
        this.rName = "Guest";
        this.bName = "";

        this.bScore = "";
        //   int solo = 3;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int solo = extras.getInt("solo");
            this.round = extras.getInt("round");
            if(solo == 1)
            {
                this.turn = 3;
                this.bName = "";
                this.rScore = extras.getString("rScore");
                this.rName = "Guest";

            }
            else{
                this.rName = extras.getString("rName");
                this.bName = extras.getString("bName");
                this.rScore = extras.getString("rScore");
                this.bScore = extras.getString("bScore");
            }
            //The key argument here must match that used in the other activity
        }

        setupUI();

        timera= new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
                if(round == 1&& turn != 3)
                {
                    Intent intent = new Intent(getApplicationContext(), BrainsterHomeUnregistered.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore",bScore);
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 0);

                    startActivity(intent);

               }
                if(round == 0 && turn == 3)
                {
                    Intent intent = new Intent(getApplicationContext(), BrainsterHomeUnregistered.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore",bScore);
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 0);

                    startActivity(intent);

                }
                if(round == 0 && turn !=3)
                {
                    Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore",bScore);
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 1);

                    startActivity(intent);

                }
            }
        }.start();


    }


    public void setupUI(){
        this.neededNumber = (TextView) findViewById(R.id.neededNumber);
        this.bluePlayerNumber = (TextView) findViewById(R.id.bluePlayerNumber);
        this.redPlayerNumber = (TextView) findViewById(R.id.redPlayerNumber);
        this.clearInput = (RelativeLayout) findViewById(R.id.clearInput);
        this.confirmTxt = (TextView) findViewById(R.id.confirmTxt);
        this.number1 = (TextView) findViewById(R.id.number1);
        this.number2 = (TextView) findViewById(R.id.number2);
        this.number3 = (TextView) findViewById(R.id.number3);
        this.number4 = (TextView) findViewById(R.id.number4);
        this.number5 = (TextView) findViewById(R.id.number5);
        this.number6 = (TextView) findViewById(R.id.number6);
        this.addition = (TextView) findViewById(R.id.addition);
        this.subtraction = (TextView) findViewById(R.id.subtraction);
        this.multiplication = (TextView) findViewById(R.id.multiplication);
        this.division =(TextView) findViewById(R.id.division);
        this.openBracket = (TextView) findViewById(R.id.openBracket);
        this.closedBracket = (TextView) findViewById(R.id.closedBracket);
        this.inputNumbers = (TextView) findViewById(R.id.inputNumbers);
        this.timer = (TextView) findViewById(R.id.timer);
        this.rScore1 = (TextView) findViewById(R.id.redPlayerScore);
        this.bScore1 = (TextView) findViewById(R.id.bluePlayerScore);
        this.rName1 = (TextView) findViewById(R.id.redPlayerName);
        this.bName1 = (TextView) findViewById(R.id.bluePlayerName);
        this.rScore1.setText(rScore);
        this.bScore1.setText(bScore);
        this.rName1.setText(rName);
        this.bName1.setText(bName);
        this.openBracketLayout = (LinearLayout) findViewById(R.id.openBracketLayout);
        this.closedBracketLayout = (LinearLayout) findViewById(R.id.closedBracketLayout);
        this.number1Layout = (LinearLayout) findViewById(R.id.number1Layout);
        this.number2Layout = (LinearLayout) findViewById(R.id.number2Layout);
        this.number3Layout = (LinearLayout) findViewById(R.id.number3Layout);
        this.number4Layout = (LinearLayout) findViewById(R.id.number4Layout);
        this.number5Layout = (LinearLayout) findViewById(R.id.number5Layout);
        this.number6Layout = (LinearLayout) findViewById(R.id.number6Layout);
        this.additionLayout = (LinearLayout) findViewById(R.id.additionLayout);
        this.substractionLayout = (LinearLayout) findViewById(R.id.subtractionLayout);
        this.multiplicationLayout = (LinearLayout) findViewById(R.id.multiplicationLayout);
        this.divisionLayout = (LinearLayout) findViewById(R.id.divisionLayout);
        this.confirmButton = (LinearLayout) findViewById(R.id.confirmButtonMainLayout);

    }
    public void generateNumbers(View view) {


        Random random = new Random();

        List<Integer> neededNumbers = new ArrayList<Integer>();

        for (int i = 1; i<=1000; i++){
            neededNumbers.add(i);
        }
        if(confirmTxt.getText().toString().equals("STOP")){
            neededNumber.setText(neededNumbers.get(random.nextInt(neededNumbers.size())).toString());
            number1.setText(digits.get(random.nextInt(digits.size())).toString());
            number2.setText(digits.get(random.nextInt(digits.size())).toString());
            number3.setText(digits.get(random.nextInt(digits.size())).toString());
            number4.setText(digits.get(random.nextInt(digits.size())).toString());

            number5.setText(doubleDigits.get(random.nextInt(doubleDigits.size())).toString());
            number6.setText(lastDigits.get(random.nextInt(lastDigits.size())).toString());

            confirmTxt.setText("CONFIRM");

            number1.setClickable(true);
            number2.setClickable(true);
            number3.setClickable(true);
            number4.setClickable(true);
            number5.setClickable(true);
            number6.setClickable(true);

            addition.setClickable(false);
            subtraction.setClickable(false);
            multiplication.setClickable(false);
            division.setClickable(false);
            openBracket.setClickable(true);
            closedBracket.setClickable(true);
        }
    }

    public void makeExpression(View view){
        generateNumbers(view);
        inputNumbers.setText("");
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText("");
                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);

                addition.setClickable(false);
                subtraction.setClickable(false);
                multiplication.setClickable(false);
                division.setClickable(false);
                openBracket.setClickable(true);
                closedBracket.setClickable(true);

                number1Layout.setBackgroundResource(R.drawable.numbers_clicked);
                number2Layout.setBackgroundResource(R.drawable.numbers_clicked);
                number3Layout.setBackgroundResource(R.drawable.numbers_clicked);
                number4Layout.setBackgroundResource(R.drawable.numbers_clicked);
                number5Layout.setBackgroundResource(R.drawable.numbers_clicked);
                number6Layout.setBackgroundResource(R.drawable.numbers_clicked);

                number1Layout.setClickable(true);
                number2Layout.setClickable(true);
                number3Layout.setClickable(true);
                number4Layout.setClickable(true);
                number5Layout.setClickable(true);
                number6Layout.setClickable(true);
            }
        });

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText() + "+");
                addition.setClickable(false);
                subtraction.setClickable(false);
                multiplication.setClickable(false);
                division.setClickable(false);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);
            }
        });

        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+"-");
                addition.setClickable(false);
                subtraction.setClickable(false);
                multiplication.setClickable(false);
                division.setClickable(false);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);

            }
        });

        multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+"*");
                addition.setClickable(false);
                subtraction.setClickable(false);
                multiplication.setClickable(false);
                division.setClickable(false);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);
            }
        });

        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+"/");
                addition.setClickable(false);
                subtraction.setClickable(false);
                multiplication.setClickable(false);
                division.setClickable(false);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);
            }
        });

        openBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+"(");
                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);
            }
        });

        closedBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+")");
                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number1.setClickable(true);
                number2.setClickable(true);
                number3.setClickable(true);
                number4.setClickable(true);
                number5.setClickable(true);
                number6.setClickable(true);
            }
        });


        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number1.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number1Layout.setBackgroundColor(Color.GRAY);
                number1Layout.setClickable(false);
            }
        });

        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number2.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number2Layout.setBackgroundColor(Color.GRAY);
                number2Layout.setClickable(false);
            }
        });

        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number3.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number3Layout.setBackgroundColor(Color.GRAY);
                number3Layout.setClickable(false);
            }
        });

        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number4.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number4Layout.setBackgroundColor(Color.GRAY);
                number4Layout.setClickable(false);
            }
        });

        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number5.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number5Layout.setBackgroundColor(Color.GRAY);
                number5Layout.setClickable(false);
            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNumbers.setText(inputNumbers.getText()+String.valueOf(number6.getText()));
                number1.setClickable(false);
                number2.setClickable(false);
                number3.setClickable(false);
                number4.setClickable(false);
                number5.setClickable(false);
                number6.setClickable(false);

                addition.setClickable(true);
                subtraction.setClickable(true);
                multiplication.setClickable(true);
                division.setClickable(true);

                number6Layout.setBackgroundColor(Color.GRAY);
                number6Layout.setClickable(false);
            }
        });

        if(!confirmTxt.getText().toString().equals("STOP")){
            confirmTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculateExpression(view);
                }
            });
        }
    }
    public void calculateExpression(View view){
        String expression = String.valueOf(inputNumbers.getText());
        Double result = new DoubleEvaluator().evaluate(expression);

        int resultInt = result.intValue();

        redPlayerNumber.setText(String.valueOf(resultInt));
        confirmButton.setVisibility(View.GONE);

        number1Layout.setVisibility(View.GONE);
        number2Layout.setVisibility(View.GONE);
        number3Layout.setVisibility(View.GONE);
        number4Layout.setVisibility(View.GONE);
        number5Layout.setVisibility(View.GONE);
        number6Layout.setVisibility(View.GONE);

        additionLayout.setVisibility(View.GONE);
        substractionLayout.setVisibility(View.GONE);
        multiplicationLayout.setVisibility(View.GONE);
        divisionLayout.setVisibility(View.GONE);
        openBracketLayout.setVisibility(View.GONE);
        closedBracketLayout.setVisibility(View.GONE);
        givePoints(view);
        timera.cancel();
    }

    public void givePoints(View view) {
        if(neededNumber.getText() == redPlayerNumber.getText()){
            rScore =String.valueOf(Integer.valueOf(rScore) + 20);
            TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
            field1.setText(rScore);

        }
        else{
            rScore =String.valueOf(Integer.valueOf(rScore) + 5);
            TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
            field1.setText(rScore);
        }
    }
}