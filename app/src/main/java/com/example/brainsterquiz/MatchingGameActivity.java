package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MatchingGameActivity extends AppCompatActivity {

    private TextView a1;
    private TextView a2;
    private TextView a3;
    private TextView a4;
    private TextView a5;
    private TextView l1;
    private TextView l2;
    private TextView l3;
    private TextView l4;
    private TextView l5;
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

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);
        getSupportActionBar().hide();


        db = FirebaseFirestore.getInstance();

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



                this.turn = extras.getInt("turn");
                if(turn != 3)
                {
                    Konekcija  app = (Konekcija) MatchingGameActivity.this.getApplication();
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
                    mSocket.on("opens",(a) -> {


                    });
                    mSocket.on("opencs",(a) -> {


                    });
                    mSocket.on("ennemywin",(a) -> {


                    });

                }
            }
            //The key argument here must match that used in the other activity
        }


        this.l1 = (TextView) findViewById(R.id.matchingPair1LeftTxt);
        this.l2 = (TextView) findViewById(R.id.matchingPair2LeftTxt);
        this.l3 = (TextView) findViewById(R.id.matchingPair3LeftTxt);
        this.l4 = (TextView) findViewById(R.id.matchingPair4LeftTxt);
        this.l5 = (TextView) findViewById(R.id.matchingPair5LeftTxt);
        this.a1 = (TextView) findViewById(R.id.matchingPair1RightTxt);
        this.a2 = (TextView) findViewById(R.id.matchingPair2RightTxt);
        this.a3 = (TextView) findViewById(R.id.matchingPair3RightTxt);
        this.a4 = (TextView) findViewById(R.id.matchingPair4RightTxt);
        this.a5 = (TextView) findViewById(R.id.matchingPair5RightTxt);

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
                if(round ==1 && turn != 3)
                {
                    Map<String, Object> userForOrgs = new HashMap<>();

                    db.collection("/matches").document(gameid)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.getString("user1").equals(myid))
                                    {
                                        userForOrgs.put("m1",Integer.valueOf(rScore)-trScore );
                                    }
                                    if(documentSnapshot.getString("user2").equals(myid))
                                    {
                                        userForOrgs.put("m2",Integer.valueOf(rScore)-trScore );
                                    }
                                    documentSnapshot.getReference().set(userForOrgs);
                                }

                                //db get string and set it to int
                            });
                }
                timer.setText("done!");
                if(round == 1  && turn != 3)
                {
                    Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bName", bName);
                    intent.putExtra("rScore", rScore);
                    intent.putExtra("bScore",bScore);
                    intent.putExtra("tscore",String.valueOf(trScore));
                    intent.putExtra("gameid",String.valueOf(gameid));
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("round", 0);
                    intent.putExtra("round", 0);
                    intent.putExtra("turn", turn);
                    finish();
                    startActivity(intent);


                }if(round == 0  && turn == 3)
                {

                    Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
                    intent.putExtra("tscore",String.valueOf(trScore));
                    intent.putExtra("gameid",String.valueOf(gameid));
                    intent.putExtra("bScore",bScore);
                    if(turn == 3)
                    {
                        intent.putExtra("solo", 1);
                    }else{
                        intent.putExtra("solo", 0);
                    }
                    intent.putExtra("turn", turn);
                    intent.putExtra("round", 1);
                    finish();
                    startActivity(intent);



                }

            }
        }.start();


        db.collection("/games/matchinggame/round").document(String.valueOf(round))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        TextView field1 = l1;
                        field1.setText(documentSnapshot.getString("l1"));
                         field1 = l2;
                        field1.setText(documentSnapshot.getString("l2"));
                        field1 = l3;
                        field1.setText(documentSnapshot.getString("l3"));
                         field1 = l4;
                        field1.setText(documentSnapshot.getString("l4"));
                         field1 = l5;
                        field1.setText(documentSnapshot.getString("l5"));

                        field1 = a1;
                        field1.setText(documentSnapshot.getString("a1"));
                        field1 = a2;
                        field1.setText(documentSnapshot.getString("a2"));
                        field1 = a3;
                        field1.setText(documentSnapshot.getString("a3"));
                        field1 = a4;
                        field1.setText(documentSnapshot.getString("a4"));
                        field1 = a5;
                        field1.setText(documentSnapshot.getString("a5"));

                    }

                    //db get string and set it to int
                });



    }


    public void Pobeda() {
        if(round ==1 && turn != 3)
        {
            Map<String, Object> userForOrgs = new HashMap<>();

            db.collection("/matches").document(gameid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.getString("user1").equals(myid))
                            {
                                userForOrgs.put("m1",Integer.valueOf(rScore)-trScore );
                            }
                            if(documentSnapshot.getString("user2").equals(myid))
                            {
                                userForOrgs.put("m2",Integer.valueOf(rScore)-trScore );
                            }
                            documentSnapshot.getReference().set(userForOrgs);
                        }

                        //db get string and set it to int
                    });
        }
        timer.setText("done!");
        if(round == 1  && turn != 3)
        {
            Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);
            intent.putExtra("rName", rName);
            intent.putExtra("bName", bName);
            intent.putExtra("rScore", rScore);
            intent.putExtra("bScore",bScore);
            intent.putExtra("tscore",String.valueOf(trScore));
            intent.putExtra("gameid",String.valueOf(gameid));
            if(turn == 3)
            {
                intent.putExtra("solo", 1);
            }else{
                intent.putExtra("solo", 0);
            }
            intent.putExtra("round", 0);
            intent.putExtra("round", 0);
            intent.putExtra("turn", turn);
            finish();
            startActivity(intent);


        }if(round == 0  && turn == 3)
        {

            Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
            intent.putExtra("tscore",String.valueOf(trScore));
            intent.putExtra("gameid",String.valueOf(gameid));
            intent.putExtra("bScore",bScore);
            if(turn == 3)
            {
                intent.putExtra("solo", 1);
            }else{
                intent.putExtra("solo", 0);
            }
            intent.putExtra("turn", turn);
            intent.putExtra("round", 1);
            finish();
            startActivity(intent);



        }


    }









}