package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

public class QuestionsGame extends AppCompatActivity {

    private TextView timer;
    private int turn;
    private int round;
    private String rName;
    private String bName;
    private String rScore;
    private String bScore;
    private TextView rName1;
    private TextView bName1;
    private TextView rScore1;
    private TextView bScore1;
    private int hint = 0;
    private QueryDocumentSnapshot user;
    int trScore=0;
    String myid;
    String gameid;
    private int opened;
    private Socket mSocket;
    CountDownTimer timera;

    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView answer4;
    private TextView question;
    private TextView question1;
    private TextView question2;
    private TextView question3;
    private TextView question4;
    private TextView question5;
    private TextView dontknow;
    private int questionnum = 1;


    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // COLOR RED   im.setBackgroundTintList(ColorStateList.valueOf(Color.RED));break;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_game);
        getSupportActionBar().hide();  db = FirebaseFirestore.getInstance();


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



                this.turn = extras.getInt("turn");
                if(turn != 3)
                {
                    Konekcija  app = (Konekcija) QuestionsGame.this.getApplication();
                    this.mSocket = app.getSocket();
                    this.user =app.getUser();
                    this.myid= user.getId();
                    this.gameid = extras.getString("gameid");
                    this.trScore = Integer.parseInt(rScore);
                    if(round == 1 )
                    {
                        this.trScore = Integer.valueOf(extras.getString("tscore"));
                    }
                    mSocket.on("changeturn",(a) -> {

                        this.turn = 1;

                    });
                    mSocket.on("enemyrightguess",(a) -> {


                    });

                    mSocket.on("ennemywrongguess",(a) -> {


                    });
                    mSocket.on("pointscac",(a) -> {

                        bScore= String.valueOf(Integer.valueOf(bScore)+Integer.valueOf(a[0].toString()));

                    });

                }
            }
            //The key argument here must match that used in the other activity
        }


        this.answer1 = (TextView) findViewById(R.id.answer1);
        this.answer2 = (TextView) findViewById(R.id.answer2);
        this.answer3 = (TextView) findViewById(R.id.answer3);
        this.answer4= (TextView) findViewById(R.id.answer4);
        this.question1 = (TextView) findViewById(R.id.question1);
        this.question2 = (TextView) findViewById(R.id.question2);
        this.question3 = (TextView) findViewById(R.id.question3);
        this.question4 = (TextView) findViewById(R.id.question4);
        this.question5 = (TextView) findViewById(R.id.question5);
        this.question = (TextView) findViewById(R.id.question);

        this.dontknow = (TextView) findViewById(R.id.arrowText);
        this.timer = (TextView) findViewById(R.id.timer);
        this.rScore1 = (TextView) findViewById(R.id.redPlayerScore);
        this.bScore1 = (TextView) findViewById(R.id.bluePlayerScore);
        this.rName1 = (TextView) findViewById(R.id.redPlayerName);
        this.bName1 = (TextView) findViewById(R.id.bluePlayerName);
        this.rScore1.setText(rScore);
        this.bScore1.setText(bScore);
        this.rName1.setText(rName);
        this.bName1.setText(bName);


        timera= new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(round ==0 && turn != 3)
                {
                    Map<String, Object> userForOrgs = new HashMap<>();

                    db.collection("/matches").document(gameid)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.getString("user1").equals(myid))
                                    {
                                        userForOrgs.put("q1",Integer.valueOf(rScore)-trScore );
                                    }
                                    if(documentSnapshot.getString("user2").equals(myid))
                                    {
                                        userForOrgs.put("q2",Integer.valueOf(rScore)-trScore );
                                    }
                                    documentSnapshot.getReference().set(userForOrgs);
                                }

                                //db get string and set it to int
                            });
                }
                timer.setText("done!");
              if(round == 0 &&turn == 3)
                {

                    Intent intent = new Intent(getApplicationContext(), MatchingGameActivity.class);
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
                    intent.putExtra("turn", 3);
                    finish();
                    startActivity(intent);


                }
                if(round == 0 && turn !=3)
                {
                    Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("gameid",String.valueOf(gameid));
                    intent.putExtra("bScore",bScore);
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    db.collection("/matches").document(gameid)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.getString("user1").equals(myid))
                                    {
                                        intent.putExtra("turn", 1);
                                    }
                                    if(documentSnapshot.getString("user2").equals(myid))
                                    {
                                        intent.putExtra("turn", 2);
                                    }
                                }

                                //db get string and set it to int
                            });
                    intent.putExtra("round", 1);
                    finish();
                    startActivity(intent);



                }

            }
        }.start();







    }
}