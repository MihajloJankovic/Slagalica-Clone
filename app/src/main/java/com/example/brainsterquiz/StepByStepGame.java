package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

public class StepByStepGame extends AppCompatActivity {
    private TextView timer;
    private int turn;
    private int round;
    private int stepsRevealed;
    private String rName;
    private String bName;
    private String rScore;
    private String bScore;
    private TextView rName1;
    private TextView sentence1;
    private TextView sentence2;
    private TextView sentence3;
    private TextView sentence4;
    private TextView sentence5;
    private TextView sentence6;
    private TextView sentence7;

    private TextView step1Points;
    private TextView step2Points;
    private TextView step3Points;
    private TextView step4Points;
    private TextView step5Points;
    private TextView step6Points;
    private TextView step7Points;

    private EditText answerInput;

    private TextView bName1;
    private TextView rScore1;
    private TextView bScore1;
    private int hint = 0;
    private int opened;
    private int posetion =1;
    private int cardPosition =1;
    private int allSubmited= 0;
    private Socket mSocket;
    private int guessedTrue = 0;
    Map<Integer,Integer> combination=new HashMap<Integer,Integer>();
    Map<Integer,Integer> guessedCombination=new HashMap<Integer,Integer>();
    Map<Integer,Integer> temp=new HashMap<Integer,Integer>();
    CountDownTimer timera;


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        this.opened = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_game);
        getSupportActionBar().hide();
        this.turn = 3;
        this.stepsRevealed = 1;
        openStep(1);
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
                if(turn != 3)
                {

                    Konekcija app = (Konekcija) StepByStepGame.this.getApplication();
                    this.mSocket = app.getSocket();
                    mSocket.on("changeturn",(a) -> {
                            this.turn = 1;
                            this.stepsRevealed = stepsRevealed +1;
                            if(stepsRevealed < 7)
                            {
                                openStep(stepsRevealed);
                            }

                    });
                    mSocket.on("pointscac",(a) -> {

                        bScore= String.valueOf(Integer.valueOf(bScore)+Integer.valueOf(a[0].toString()));


                    });
                    mSocket.on("nextgamecc",(a) -> {
                        openStep(1);
                        openStep(2);
                        openStep(3);
                        openStep(4);
                        openStep(5);
                        openStep(6);
                        openStep(7);



                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(true){


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            timera.cancel();

                                            if(round == 1  && turn != 3)
                                            {
                                                Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                                                intent.putExtra("rName", rName);
                                                intent.putExtra("bName", bName);
                                                intent.putExtra("rScore", rScore);
                                                intent.putExtra("bScore",bScore);
                                                if(turn == 3)
                                                {
                                                    intent.putExtra("turn", 3);
                                                    intent.putExtra("solo", 1);
                                                }else{
                                                    intent.putExtra("solo", 0);
                                                }
                                                intent.putExtra("round", 0);
                                                if(turn == 1){

                                                        intent.putExtra("turn", 2);


                                                }
                                                if(turn == 2){

                                                        intent.putExtra("turn", 1);

                                                }

                                                startActivity(intent);

                                            }if(round == 0 && turn == 3)
                                            {

                                                Intent intent = new Intent(getApplicationContext(), StepByStepGame.class);
                                                intent.putExtra("rName", rName);
                                                intent.putExtra("bName", bName);
                                                intent.putExtra("rScore", rScore);
                                                intent.putExtra("bScore",bScore);
                                                if(turn == 3)
                                                {
                                                    intent.putExtra("turn", 3);
                                                    intent.putExtra("solo", 1);
                                                }else{
                                                    intent.putExtra("solo", 0);
                                                }
                                                intent.putExtra("round", 0);
                                                if(turn == 1){

                                                        intent.putExtra("turn", 2);


                                                }
                                                if(turn == 2){

                                                        intent.putExtra("turn", 1);

                                                }
                                                startActivity(intent);

                                            }
                                            if(round == 0  && turn !=3)
                                            {
                                                Intent intent = new Intent(getApplicationContext(), StepByStepGame.class);
                                                intent.putExtra("rName", rName);
                                                intent.putExtra("bName", bName);
                                                intent.putExtra("rScore", rScore);
                                                intent.putExtra("bScore",bScore);
                                                if(turn == 3)
                                                {
                                                    intent.putExtra("turn", 3);
                                                    intent.putExtra("solo", 1);
                                                }else{
                                                    intent.putExtra("solo", 0);
                                                }
                                                intent.putExtra("round", 1);
                                                if(turn == 1){


                                                        intent.putExtra("turn", 2);





                                                }
                                                if(turn == 2){

                                                        intent.putExtra("turn", 1);


                                                }
                                                startActivity(intent);

                                            }
                                            finish();
                                        }
                                    }, 5000);
                                }
                            }
                        });



                    });





                }
            }

        }

        setupUI();
        timera = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
                if (round == 1 && turn != 3) {
                    Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore", bScore);
                    if (turn == 3) {
                        intent.putExtra("solo", 1);
                    } else {
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 0);
                    if(turn == 1){


                        intent.putExtra("turn", 2);





                    }
                    if(turn == 2){

                        intent.putExtra("turn", 1);


                    }

                    startActivity(intent);

                }
                if (round == 0 && turn == 3) {
                    Intent intent = new Intent(getApplicationContext(), NumberGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore", bScore);
                    if (turn == 3) {
                        intent.putExtra("solo", 1);
                    } else {
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 0);
                    if(turn == 1){


                        intent.putExtra("turn", 2);





                    }
                    if(turn == 2){

                        intent.putExtra("turn", 1);


                    }

                    startActivity(intent);

                }
                if (round == 0 && turn != 3) {
                    Intent intent = new Intent(getApplicationContext(), StepByStepGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore", bScore);
                    if (turn == 3) {
                        intent.putExtra("solo", 1);
                    } else {
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 1);
                    if(turn == 1){


                        intent.putExtra("turn", 2);





                    }
                    if(turn == 2){

                        intent.putExtra("turn", 1);


                    }

                    startActivity(intent);

                }
            }
        }.start();


    }
    public void setupUI() {
        this.sentence1 = (TextView) findViewById(R.id.sentence1);
        this.sentence2 = (TextView) findViewById(R.id.sentence2);
        this.sentence3 = (TextView) findViewById(R.id.sentence3);
        this.sentence4 = (TextView) findViewById(R.id.sentence4);
        this.sentence5 = (TextView) findViewById(R.id.sentence5);
        this.sentence6 = (TextView) findViewById(R.id.sentence6);
        this.sentence7 = (TextView) findViewById(R.id.sentence7);

        this.step1Points = (TextView) findViewById(R.id.step1Points);
        this.step2Points = (TextView) findViewById(R.id.step2Points);
        this.step3Points = (TextView) findViewById(R.id.step3Points);
        this.step4Points = (TextView) findViewById(R.id.step4Points);
        this.step5Points = (TextView) findViewById(R.id.step5Points);
        this.step6Points = (TextView) findViewById(R.id.step6Points);
        this.step7Points = (TextView) findViewById(R.id.step7Points);

        this.answerInput = (EditText) findViewById(R.id.answerInput);

        this.timer = (TextView) findViewById(R.id.timer);
        this.rScore1 = (TextView) findViewById(R.id.redPlayerScore);
        this.bScore1 = (TextView) findViewById(R.id.bluePlayerScore);
        this.rName1 = (TextView) findViewById(R.id.redPlayerName);
        this.bName1 = (TextView) findViewById(R.id.bluePlayerName);
        this.rScore1.setText(rScore);
        this.bScore1.setText(bScore);
        this.rName1.setText(rName);
        this.bName1.setText(bName);
    }
    public void addToSentence(int pos) {

        String sent = "sentence";

        String temp = sent + String.valueOf(pos);
        int id = 1;

        switch (temp) {
            case "sentence1":
                id = R.id.sentence1;
                break;
            case "sentence2":
                id = R.id.sentence2;
                break;
            case "sentence3":
                id = R.id.sentence3;
                break;
            case "sentence4":
                id = R.id.sentence4;
                break;
            case "sentence5":
                id = R.id.sentence5;
                break;
            case "sentence6":
                id = R.id.sentence6;
                break;
            case "sentence7":
                id = R.id.sentence7;
                break;
        }

        TextView current = (TextView) findViewById(id);
        int idd;
        int back = 0;
        switch (back) {
            case 1:
                current.setBackgroundColor(Color.parseColor("#011A40"));
                break;
            case 2:
                current.setBackgroundColor(Color.WHITE);
        }
    }

    public void openStep(int a) {
        int id =1;
        if(turn == 1 || turn == 3) {
            String sentenceID = "sentence" +a;

            switch (sentenceID) {
                case "sentence1":
                    id =R.id.sentence1;
                    break;
                case "sentence2":
                    id =R.id.sentence2;
                    break;
                case "sentence3":
                    id =R.id.sentence3;
                    break;
                case "sentence4":
                    id =R.id.sentence4;
                    break;
                case "sentence5":
                    id =R.id.sentence5;
                    break;
                case "sentence6":
                    id =R.id.sentence6;
                    break;
                case "sentence7":
                    id =R.id.sentence7;
                    break;
            }

            String neededSentenceID = sentenceID;
            int finalId = id;
            db.collection("/games/Step by step/1").document(round + "").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String value = documentSnapshot.getString(neededSentenceID);
                    TextView field = (TextView) findViewById(finalId);
                    mSocket.emit("nextstep");
                    field.setText(value);
                    opened = 1;
                }
            });
        }
    }

    public void guessAnswer(View v) {
        if (turn == 1 || turn == 3) {



             EditText input = (EditText) findViewById(R.id.answerInput);



                    String guess = String.valueOf(input.getText());


                    String finalID = "answer";
                    db.collection("/games/Step by step/1").document(round + "").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(guess.equals(documentSnapshot.getString(finalID))) {
                                mSocket.emit("nextgamec");
                                Toast.makeText(getApplicationContext(),"Success Guess",Toast.LENGTH_SHORT).show();
                                int result = 0;
                                if (stepsRevealed == 1){
                                    result = 20;
                                }

                                if(stepsRevealed == 2) {
                                    result = 18;
                                }

                                if(stepsRevealed == 3) {
                                    result = 16;
                                }

                                if(stepsRevealed == 4) {
                                    result = 14;
                                }

                                if(stepsRevealed == 5) {
                                    result = 12;
                                }

                                if(stepsRevealed == 6) {
                                    result = 10;
                                }

                                if(stepsRevealed == 7) {
                                    result = 8;
                                }
                                rScore = String.valueOf(Integer.valueOf(rScore)+result);
                                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                field1.setText(rScore);
                                if(turn != 3)
                                {

                                    mSocket.emit("pointsca",result);
                                    mSocket.emit("nextgamec");
                                }

                                TextView sent1 = (TextView) findViewById(R.id.sentence1);
                                sent1.setText(documentSnapshot.getString("sentence1"));

                                TextView sent2 = (TextView) findViewById(R.id.sentence2);
                                sent2.setText(documentSnapshot.getString("sentence2"));

                                TextView sent3 = (TextView) findViewById(R.id.sentence3);
                                sent3.setText(documentSnapshot.getString("sentence3"));

                                TextView sent4 = (TextView) findViewById(R.id.sentence4);
                                sent4.setText(documentSnapshot.getString("sentence4"));

                                TextView sent5 = (TextView) findViewById(R.id.sentence5);
                                sent5.setText(documentSnapshot.getString("sentence5"));

                                TextView sent6 = (TextView) findViewById(R.id.sentence6);
                                sent6.setText(documentSnapshot.getString("sentence6"));

                                TextView sent7 = (TextView) findViewById(R.id.sentence7);
                                sent7.setText(documentSnapshot.getString("sentence7"));

                                EditText answer = (EditText) findViewById(R.id.answerInput);
                                answer.setText(documentSnapshot.getString("answer"));

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(round == 1 && turn !=3)
                                        {

                                            timera.cancel();
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
                                            intent.putExtra("round", 0);

                                            startActivity(intent);
                                            finish();
                                        }
                                        if(round == 0 && turn !=3)
                                        {

                                            timera.cancel();
                                            Intent intent = new Intent(getApplicationContext(), StepByStepGame.class);
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
                                            finish();
                                        }
                                        if(round == 0 && turn ==3)
                                        {

                                            timera.cancel();
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
                                            intent.putExtra("round", 0);

                                            startActivity(intent);

                                        }


                                    }
                                }, 2000);


                            }
                            else {
                                if(stepsRevealed < 7)
                                {
                                    stepsRevealed=stepsRevealed+1;
                                    openStep(stepsRevealed);
                                    mSocket.emit("turn");
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Wrong guess",Toast.LENGTH_SHORT).show();
                                    mSocket.emit("turn");
                                }

                            }
                        }
                    });
                }



        }
    }
