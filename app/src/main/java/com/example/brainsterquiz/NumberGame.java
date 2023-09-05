package com.example.brainsterquiz;


import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import io.socket.client.Socket;


public class NumberGame extends AppCompatActivity implements SensorEventListener {
    private Timer singleDigitTimer;
    TextView neededNumber;
    private SensorManager sensorManager;
    private TextView bluePlayerNumber;
    List<TextView> allButtons;
    TextView redPlayerNumber;
    private RelativeLayout clearInput;
    TextView confirmTxt;
    TextView inputNumbers;
    private QueryDocumentSnapshot user;
    int trScore = 0;
    String myid;
    String gameid;
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
    private int currentIndexList = 0;
    private List<Integer> doubleDigits;
    private List<Integer> lastDigits;

    private int hint = 0;
    private int opened;
    private int posetion = 1;
    private int cardPosition = 1;
    private int allSubmited = 0;
    private int guessedTrue = 0;
    private Socket mSocket;
    
    private Sensor accelerometerSensor;
    private  int SHAKE_THRESHOLD = 45;
    private ValueAnimator[] oneDigit = new ValueAnimator[4];
    private ValueAnimator doubleDigitsAnim = new ValueAnimator();
    private ValueAnimator lastDigitsAnim = new ValueAnimator();
    private ValueAnimator neededNumberAnim = new ValueAnimator();

    private int targetNumber;
    private ArrayList<Integer> offeredNumbers = new ArrayList<>();

    Map<Integer, Integer> combination = new HashMap<Integer, Integer>();
    Map<Integer, Integer> guessedCombination = new HashMap<Integer, Integer>();
    Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
    CountDownTimer timera;

    FirebaseFirestore db;
    private ValueAnimator animatorDoubleDigit;

    int MaxNumber(int a, int b, int c) {
        if (Math.abs(c - a) < Math.abs(c - b))
            return a;
        else
            return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        numberAnimations();


        this.turn = 3;
        this.rName = "Guest";
        this.bName = "";

        this.bScore = "";
        //   int solo = 3;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int solo = extras.getInt("solo");
            this.round = extras.getInt("round");
            if (solo == 1) {
                this.turn = 3;
                this.bName = "";
                this.rScore = extras.getString("rScore");
                this.rName = "Guest";

            } else {
                this.rName = extras.getString("rName");
                this.bName = extras.getString("bName");
                this.rScore = extras.getString("rScore");
                this.bScore = extras.getString("bScore");
                this.turn = extras.getInt("turn");
                if (turn != 3) {

                    Konekcija app = (Konekcija) NumberGame.this.getApplication();
                    this.mSocket = app.getSocket();
                    this.user = app.getUser();
                    this.myid = user.getId();
                    this.gameid = extras.getString("gameid");
                    this.trScore = Integer.parseInt(rScore);
                    if (round == 1) {
                        this.trScore = Integer.valueOf(extras.getString("tscore"));
                    }

                    mSocket.on("neededc", (a) -> {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                neededNumber.setText(String.valueOf(a[0]));
                                stopValueAnimations();

                                makeExpression();
                                //Za izmenuti da postavi random brojeve  osim u nedded number
                                // AKo je turn 2 iskljuciti senzor da trigeruje odabir brojeva nego da ovde trigerujes
                                // odabir brojeva ali posle trigera i postavljanja brojeva overajtuj random broj sa ovim
                                // neededNumber.setText(String.valueOf(a[0]));
                           /*     Random random = new Random();

                                List<Integer> neededNumbers = new ArrayList<Integer>();

                                for (int i = 1; i <= 1000; i++) {
                                    neededNumbers.add(i);
                                }
                                if (confirmTxt.getText().toString().equals("CONFIRM")) {
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
                                if (confirmTxt.getText().toString().equals("STOP")) {
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
                                }*/
                            }
                        });

                    });
                    mSocket.on("myguessc", (a) -> {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                bluePlayerNumber.setText(a[0].toString());
                                if (!bluePlayerNumber.getText().toString().equals("???") && bluePlayerNumber.getText().toString() != " ") {
                                    if (!redPlayerNumber.getText().toString().equals("???") && redPlayerNumber.getText().toString() != " ") {

                                        mSocket.emit("finalNumberGame");
                                    }
                                }
                            }
                        });

                    });

                    mSocket.on("finalNumberGamea", (a) -> {
                        timera.cancel();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (turn == 2) {
                                    if (bluePlayerNumber.getText() == neededNumber.getText()) {
                                        bScore = String.valueOf(Integer.valueOf(bScore) + 20);
                                        TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                        field1.setText(bScore);

                                    } else {
                                        if (redPlayerNumber.getText() == neededNumber.getText()) {
                                            rScore = String.valueOf(Integer.valueOf(rScore) + 20);
                                            TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                            field1.setText(rScore);

                                        } else {
                                            int nedeed = Integer.valueOf((String) neededNumber.getText());
                                            int closer = MaxNumber(Integer.valueOf((String) redPlayerNumber.getText()), Integer.valueOf((String) bluePlayerNumber.getText()), Integer.valueOf((String) neededNumber.getText()));
                                            if (closer == Integer.valueOf((String) redPlayerNumber.getText())) {
                                                rScore = String.valueOf(Integer.valueOf(rScore) + 5);
                                                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                                field1.setText(rScore);
                                            }
                                            if (closer == Integer.valueOf((String) bluePlayerNumber.getText())) {
                                                bScore = String.valueOf(Integer.valueOf(bScore) + 5);
                                                TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                                field1.setText(bScore);
                                            }
                                        }
                                    }
                                    turn = 1;

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            timera.cancel();
                                            if (round == 1 && turn != 3) {
                                                Map<String, Object> userForOrgs = new HashMap<>();

                                                db.collection("/matches").document(gameid)
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Map<String, Object> map = new HashMap<>();
                                                                map.put("yourProperty", "yourValue");
                                                                if (documentSnapshot.getString("user1").equals(myid)) {
                                                                    userForOrgs.put("n1", Integer.valueOf(rScore) - trScore);
                                                                    if(Integer.valueOf(rScore) >= Integer.valueOf(bScore))
                                                                    {
                                                                        userForOrgs.put("winner", myid);
                                                                    }

                                                                }
                                                                if (documentSnapshot.getString("user2").equals(myid)) {
                                                                    userForOrgs.put("n2", Integer.valueOf(rScore) - trScore);
                                                                    if(Integer.valueOf(rScore) >= Integer.valueOf(bScore))
                                                                    {
                                                                        userForOrgs.put("winner", myid);
                                                                    }
                                                                }
                                                                db.collection("/matches").document(gameid).update(userForOrgs);
                                                            }

                                                            //db get string and set it to int
                                                        });
                                            }
                                            if (round == 1 && turn != 3) {
                                                Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                                                finish();
                                                startActivity(intent);


                                            }
                                            if (round == 0 && turn == 3) {

                                                Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                                                finish();
                                                startActivity(intent);


                                            }
                                            if (round == 0 && turn != 3) {
                                                Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                                                intent.putExtra("rName", rName);
                                                intent.putExtra("bName", bName);
                                                intent.putExtra("rScore", rScore);
                                                intent.putExtra("tscore", String.valueOf(trScore));
                                                intent.putExtra("gameid", String.valueOf(gameid));
                                                intent.putExtra("bScore", bScore);
                                                if (turn == 3) {
                                                    intent.putExtra("turn", 3);
                                                    intent.putExtra("solo", 1);
                                                } else {
                                                    intent.putExtra("solo", 0);
                                                }
                                                intent.putExtra("round", 1);

                                                intent.putExtra("turn", turn);

                                                finish();
                                                startActivity(intent);


                                            }

                                        }
                                    }, 2000);
                                }
                                if (turn == 1) {
                                    if (redPlayerNumber.getText().equals(neededNumber.getText())) {
                                        rScore = String.valueOf(Integer.valueOf(rScore) + 20);
                                        TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                        field1.setText(rScore);
                                    } else {
                                        if (bluePlayerNumber.getText().equals(neededNumber.getText())) {
                                            bScore = String.valueOf(Integer.valueOf(bScore) + 20);
                                            TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                            field1.setText(bScore);

                                        } else {
                                            int nedeed = Integer.valueOf((String) neededNumber.getText());
                                            int closer = MaxNumber(Integer.valueOf((String) redPlayerNumber.getText()), Integer.valueOf((String) bluePlayerNumber.getText()), Integer.valueOf((String) neededNumber.getText()));
                                            if (closer == Integer.valueOf((String) redPlayerNumber.getText())) {
                                                rScore = String.valueOf(Integer.valueOf(rScore) + 5);
                                                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                                field1.setText(rScore);
                                            }
                                            if (closer == Integer.valueOf((String) bluePlayerNumber.getText())) {
                                                bScore = String.valueOf(Integer.valueOf(bScore) + 5);
                                                TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                                field1.setText(bScore);
                                            }
                                        }
                                    }
                                    turn = 2;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (round == 0 && turn != 3) {
                                                Map<String, Object> userForOrgs = new HashMap<>();

                                                db.collection("/matches").document(gameid)
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Map<String, Object> map = new HashMap<>();
                                                                map.put("yourProperty", "yourValue");
                                                                if (documentSnapshot.getString("user1").equals(myid)) {
                                                                    userForOrgs.put("n1", Integer.valueOf(rScore) - trScore);
                                                                }
                                                                if (documentSnapshot.getString("user2").equals(myid)) {
                                                                    userForOrgs.put("n2", Integer.valueOf(rScore) - trScore);
                                                                }
                                                                db.collection("/matches").document(gameid).update(userForOrgs);
                                                            }

                                                            //db get string and set it to int
                                                        });
                                            }
                                            if (round == 1 && turn != 3) {
                                                Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                                                finish();
                                                startActivity(intent);


                                            }
                                            if (round == 0 && turn == 3) {

                                                Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                                                finish();
                                                startActivity(intent);


                                            }
                                            if (round == 0 && turn != 3) {
                                                Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                                                intent.putExtra("rName", rName);
                                                intent.putExtra("bName", bName);
                                                intent.putExtra("rScore", rScore);
                                                intent.putExtra("tscore", String.valueOf(trScore));
                                                intent.putExtra("gameid", String.valueOf(gameid));
                                                intent.putExtra("bScore", bScore);
                                                if (turn == 3) {
                                                    intent.putExtra("turn", 3);
                                                    intent.putExtra("solo", 1);
                                                } else {
                                                    intent.putExtra("solo", 0);
                                                }
                                                intent.putExtra("round", 1);

                                                intent.putExtra("turn", turn);

                                                finish();
                                                startActivity(intent);


                                            }

                                        }
                                    }, 2000);
                                }
                            }
                        });

                    });


                }


            }
        }
        //The key argument here must match that used in the other activity
        setupUI();

        timera = new CountDownTimer(120000, 1000) {

            public void sensorSetup() {

            }

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
                if (round == 0 && turn != 3) {
                    Map<String, Object> userForOrgs = new HashMap<>();

                    db.collection("/matches").document(gameid)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("yourProperty", "yourValue");
                                    if (documentSnapshot.getString("user1").equals(myid)) {
                                        userForOrgs.put("n1", Integer.valueOf(rScore) - trScore);
                                    }
                                    if (documentSnapshot.getString("user2").equals(myid)) {
                                        userForOrgs.put("n2", Integer.valueOf(rScore) - trScore);
                                    }
                                    db.collection("/matches").document(gameid).update(userForOrgs);
                                }

                                //db get string and set it to int
                            });
                }
                if (round == 1 && turn != 3) {
                    Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                    finish();
                    startActivity(intent);

                }
                if (round == 0 && turn == 3) {
                    Intent intent = new Intent(getApplicationContext(), BrainsterHome.class);
                    finish();
                    startActivity(intent);

                }
                if (round == 0 && turn != 3) {
                    Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore", bScore);
                    intent.putExtra("tscore", String.valueOf(trScore));
                    intent.putExtra("gameid", String.valueOf(gameid));
                    intent.putExtra("round", 1);
                    if (turn == 3) {
                        intent.putExtra("solo", 1);
                    } else {
                        intent.putExtra("solo", 0);
                    }

                    if (turn == 1) {
                        intent.putExtra("turn", 2);
                    }
                    if (turn == 2) {
                        intent.putExtra("turn", 1);
                    }
                    if (turn == 3) {
                        intent.putExtra("turn", 3);
                    }

                    finish();
                    startActivity(intent);

                }
            }
        }.start();


    }


    public void numberAnimations() {
        setupUI();
        oneDigit = new ValueAnimator[4];
        doubleDigitsAnim = new ValueAnimator();
        lastDigitsAnim = new ValueAnimator();
        neededNumberAnim = new ValueAnimator();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            TextView[] textViews = new TextView[4];
            textViews[0] = number1;
            textViews[1] = number2;
            textViews[2] = number3;
            textViews[3] = number4;
            int duration = 80 + i * 3;
            oneDigit[i] = ValueAnimator.ofInt(1, 9);
            oneDigit[i].setDuration(duration);
            int finalI = i;
            oneDigit[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue();
                    textViews[finalI].setText(String.valueOf(value));
                }
            });
            oneDigit[i].setRepeatMode(ValueAnimator.REVERSE);
            oneDigit[i].setRepeatCount(ValueAnimator.INFINITE);
            oneDigit[i].start();
        }
        doubleDigitsAnim = ValueAnimator.ofInt(10, 15, 20);
        doubleDigitsAnim.setInterpolator(null);
        doubleDigitsAnim.setDuration(80);

        doubleDigitsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                int randomIndex = new Random().nextInt(3);
                int[] values = {10, 15, 20};
                number5.setText(String.valueOf(values[randomIndex]));
            }
        });
        doubleDigitsAnim.setRepeatMode(ValueAnimator.REVERSE);
        doubleDigitsAnim.setRepeatCount(ValueAnimator.INFINITE);
        doubleDigitsAnim.start();

        lastDigitsAnim = ValueAnimator.ofInt(25, 50, 75, 100);
        lastDigitsAnim.setInterpolator(null);
        lastDigitsAnim.setDuration(80);

        lastDigitsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                int randomIndex = new Random().nextInt(4);
                int[] values = {25, 50, 75, 100};
                number6.setText(String.valueOf(values[randomIndex]));
            }
        });
        lastDigitsAnim.setRepeatMode(ValueAnimator.REVERSE);
        lastDigitsAnim.setRepeatCount(ValueAnimator.INFINITE);
        lastDigitsAnim.start();

        neededNumberAnim = ValueAnimator.ofInt(1, 1000);
        neededNumberAnim.setInterpolator(null);
        neededNumberAnim.setDuration(80);

        neededNumberAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                neededNumber.setText(String.valueOf(value));
            }
        });
        neededNumberAnim.setRepeatMode(ValueAnimator.REVERSE);
        neededNumberAnim.setRepeatCount(ValueAnimator.INFINITE);
        neededNumberAnim.start();
    }

    private void stopValueAnimations() {
        for (ValueAnimator animator : oneDigit) {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
        if(doubleDigitsAnim != null && doubleDigitsAnim.isRunning()){
            doubleDigitsAnim.cancel();
        }
        if(lastDigitsAnim != null && lastDigitsAnim.isRunning()){
            lastDigitsAnim.cancel();
        }

        if(neededNumberAnim != null && neededNumberAnim.isRunning()){
            neededNumberAnim.cancel();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      if(turn == 1 )
      {
          db.collection("/matches").document(gameid)
                  .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                          if (documentSnapshot.getString("user1").equals(myid) && round ==0) {
                             SHAKE_THRESHOLD = 1000000;
                          }
                          else {
                              if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                  float x = event.values[0];
                                  float y = event.values[1];
                                  float z = event.values[2];

                                  float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                                  if (acceleration > SHAKE_THRESHOLD) {
                                      stopValueAnimations();

                                      makeExpression();
                                  }
                              }
                          }
                          if (documentSnapshot.getString("user2").equals(myid)&& round ==1) {
                              SHAKE_THRESHOLD = 1000000;
                          }
                          else {
                              if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                  float x = event.values[0];
                                  float y = event.values[1];
                                  float z = event.values[2];

                                  float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                                  if (acceleration > SHAKE_THRESHOLD) {
                                      stopValueAnimations();

                                      makeExpression();
                                  }
                              }
                          }
                      }

                      //db get string and set it to int
                  });
      }
      if(turn == 3)
      {

          if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
              float x = event.values[0];
              float y = event.values[1];
              float z = event.values[2];

              float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

              if (acceleration > SHAKE_THRESHOLD) {
                  stopValueAnimations();

                  makeExpression();
                  Toast.makeText(this, "Device shaken!", Toast.LENGTH_SHORT).show();
              }
          }
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void setupUI() {
        if (turn == 2) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    confirmTxt.setText(" ");
                    confirmButton.setClickable(false);
                }
            });
        }
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
        this.division = (TextView) findViewById(R.id.division);
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

    public void generateNumbers() {


        Random random = new Random();

        List<Integer> neededNumbers = new ArrayList<Integer>();

        for (int i = 1; i <= 1000; i++) {
            neededNumbers.add(i);
        }
        if (confirmTxt.getText().toString().equals("CONFIRM")) {

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

        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Character.isDigit(inputNumbers.getText().charAt(inputNumbers.length() - 1)) || inputNumbers.getText().toString().equals("") || inputNumbers.getText().toString() == null) {
                    Toast.makeText(NumberGame.this, "Expression not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (turn == 1) {
            mSocket.emit("needed", Integer.valueOf((String) neededNumber.getText()));
        }

    }

    public void calculateExpression(View view) {
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
        if (turn == 3) {
            timera.cancel();
            if (neededNumber.getText() == redPlayerNumber.getText()) {
                rScore = String.valueOf(Integer.valueOf(rScore) + 20);
                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                field1.setText(rScore);

            } else {
                rScore = String.valueOf(Integer.valueOf(rScore) + 5);
                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                field1.setText(rScore);
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timera.cancel();
                    if (round == 0 && turn != 3) {
                        Map<String, Object> userForOrgs = new HashMap<>();

                        db.collection("/matches").document(gameid)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("yourProperty", "yourValue");
                                        if (documentSnapshot.getString("user1").equals(myid)) {
                                            userForOrgs.put("n1", Integer.valueOf(rScore) - trScore);
                                        }
                                        if (documentSnapshot.getString("user2").equals(myid)) {
                                            userForOrgs.put("n2", Integer.valueOf(rScore) - trScore);
                                        }
                                        db.collection("/matches").document(gameid).update(userForOrgs);
                                    }

                                    //db get string and set it to int
                                });
                    }
                    Intent intent = new Intent(getApplicationContext(), BrainsterHomeUnregistered.class);
                    finish();
                    startActivity(intent);
                }
            }, 3000);

        }
        if (turn == 1) {
            mSocket.emit("myguess", redPlayerNumber.getText());

        }
        if (turn == 2) {
            mSocket.emit("myguess", redPlayerNumber.getText());

        }

    }

    public void makeExpression() {
        generateNumbers();
        confirmTxt.setText("CONFIRM");
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
                inputNumbers.setText(inputNumbers.getText() + "-");
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
                inputNumbers.setText(inputNumbers.getText() + "*");
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
                inputNumbers.setText(inputNumbers.getText() + "/");
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
                inputNumbers.setText(inputNumbers.getText() + "(");
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
                inputNumbers.setText(inputNumbers.getText() + ")");
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number1.getText()));
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number2.getText()));
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number3.getText()));
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number4.getText()));
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number5.getText()));
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
                inputNumbers.setText(inputNumbers.getText() + String.valueOf(number6.getText()));
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


        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateExpression(view);
            }
        });

    }







    }