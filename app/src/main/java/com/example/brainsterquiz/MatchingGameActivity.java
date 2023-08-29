package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
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

public class  MatchingGameActivity extends AppCompatActivity {

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
    private int switcha = 0;
    private String bName;
    private String rScore;
    private String bScore;
    private TextView rName1;
    private TextView bName1;
    private TextView rScore1;
    private TextView bScore1;
    private int tempa = 0;
    private int tempb = 0;
    private int counter = 0;
    private int matchcounter = 0;
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
                        switcha = 1;
                        runOnUiThread(() -> Toast.makeText(MatchingGameActivity.this, "Your turn !", Toast.LENGTH_SHORT).show());
                        this.turn = 1;

                    });
                    mSocket.on("pointscac",(a) -> {

                        bScore= String.valueOf(Integer.valueOf(bScore)+Integer.valueOf(a[0].toString()));
                        TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                        field1.setText(bScore);

                    });
                        mSocket.on("matchsenda",(a) -> {
                        String tempf= a[0].toString();
                        int aa=0;
                        ++counter;
                        switch (tempf) {
                            case "l1c": aa=R.id.matchingPair1Left;  break;
                            case "l2c": aa=R.id.matchingPair2Left; break;
                            case "l3c": aa=R.id.matchingPair3Left;break;
                            case "l4c": aa=R.id.matchingPair4Left;  break;
                            case "l5c": aa=R.id.matchingPair5Left;  break;
                        }
                        int finalAa = aa;
                        db.collection("/games/matchinggame/round").document(round+"")
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if(documentSnapshot.getString(tempf).equals(a1.getText().toString()))
                                        {
                                            LinearLayout ta = MatchingGameActivity.this.findViewById(finalAa);
                                            LinearLayout tb = MatchingGameActivity.this.findViewById(R.id.matchingPair1Right);
                                            ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            ta.setClickable(false);
                                            tb.setClickable(false);
                                        }
                                        if(documentSnapshot.getString(tempf).equals(a2.getText().toString()))
                                        {
                                            LinearLayout ta = MatchingGameActivity.this.findViewById(finalAa);
                                            LinearLayout tb = MatchingGameActivity.this.findViewById(R.id.matchingPair2Right);
                                            ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            ta.setClickable(false);
                                            tb.setClickable(false);
                                        }
                                        if(documentSnapshot.getString(tempf).equals(a3.getText().toString()))
                                        {
                                            LinearLayout ta = MatchingGameActivity.this.findViewById(finalAa);
                                            LinearLayout tb = MatchingGameActivity.this.findViewById(R.id.matchingPair3Right);
                                            ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            ta.setClickable(false);
                                            tb.setClickable(false);
                                        }
                                        if(documentSnapshot.getString(tempf).equals(a4.getText().toString()))
                                        {
                                            LinearLayout ta = MatchingGameActivity.this.findViewById(finalAa);
                                            LinearLayout tb = MatchingGameActivity.this.findViewById(R.id.matchingPair4Right);
                                            ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            ta.setClickable(false);
                                            tb.setClickable(false);
                                        }
                                        if(documentSnapshot.getString(tempf).equals(a5.getText().toString()))
                                        {
                                            LinearLayout ta = MatchingGameActivity.this.findViewById(finalAa);
                                            LinearLayout tb = MatchingGameActivity.this.findViewById(R.id.matchingPair5Right);
                                            ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                            ta.setClickable(false);
                                            tb.setClickable(false);
                                        }


                                    }

                                    //db get string and set it to int
                                });




                    });
                    mSocket.on("wrongmatchsenda",(a) -> {

                        Map<String,Object> mapa;
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            mapa = mapper.readValue(a[0].toString(), Map.class);

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }


                        String tempf= mapa.get("a").toString();
                        String tempg= mapa.get("b").toString();
                        int aa=0;
                        int bb=0;
                        switch (tempf) {
                            case "l1": aa=R.id.matchingPair1Left;  break;
                            case "l2": aa=R.id.matchingPair2Left; break;
                            case "l3": aa=R.id.matchingPair3Left;break;
                            case "l4": aa=R.id.matchingPair4Left;  break;
                            case "l5": aa=R.id.matchingPair5Left;  break;
                        }
                        switch (tempg                                                                                                                                                                                                                                                                                                                                                                                                                                    ) {
                            case "a1": bb=R.id.matchingPair1Right;  break;
                            case "a2": bb=R.id.matchingPair2Right; break;
                            case "a3": bb=R.id.matchingPair3Right;break;
                            case "a4": bb=R.id.matchingPair4Right;  break;
                            case "a5": bb=R.id.matchingPair5Right;  break;
                        }
                        int finalAa = aa;
                        LinearLayout ta = MatchingGameActivity.this.findViewById(aa);
                        LinearLayout tb = MatchingGameActivity.this.findViewById(bb);



                        ta.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        tb.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        ta.setClickable(true);
                        ta.setClickable(true);



                    });
                    mSocket.on("nextgamecc",(a) -> {

                      Pobeda();

                    });


                }
            }
            //The key argument here must match that used in the other activity
        }



        LinearLayout l1a = (LinearLayout) findViewById(R.id.matchingPair1Left);
        LinearLayout l2a = (LinearLayout) findViewById(R.id.matchingPair2Left);
        LinearLayout l3a = (LinearLayout) findViewById(R.id.matchingPair3Left);
        LinearLayout l4a = (LinearLayout) findViewById(R.id.matchingPair4Left);
        LinearLayout l5a = (LinearLayout) findViewById(R.id.matchingPair5Left);
        LinearLayout a1a = (LinearLayout) findViewById(R.id.matchingPair1Right);
        LinearLayout a2a = (LinearLayout) findViewById(R.id.matchingPair2Right);
        LinearLayout a3a = (LinearLayout) findViewById(R.id.matchingPair3Right);
        LinearLayout a4a = (LinearLayout) findViewById(R.id.matchingPair4Right);
        LinearLayout a5a = (LinearLayout) findViewById(R.id.matchingPair5Right);


        this.timer = (TextView) findViewById(R.id.timer);
        this.rScore1 = (TextView) findViewById(R.id.redPlayerScore);
        this.bScore1 = (TextView) findViewById(R.id.bluePlayerScore);
        this.rName1 = (TextView) findViewById(R.id.redPlayerName);
        this.bName1 = (TextView) findViewById(R.id.bluePlayerName);
        this.rScore1.setText(rScore);
        this.bScore1.setText(bScore);
        this.rName1.setText(rName);
        this.bName1.setText(bName);


        timera= new CountDownTimer(30000, 1000) {

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
                timera.cancel();
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
                    if(turn == 1){
                        if(switcha == 0)
                        {
                            intent.putExtra("turn", 2);
                        }
                        else{
                            intent.putExtra("turn", 1);
                        }

                    }
                    if(turn == 2){
                        if(switcha == 0)
                        {
                            intent.putExtra("turn", 1);
                        }
                        else{
                            intent.putExtra("turn", 2);
                        }
                    }
                    finish();
                    startActivity(intent);


                }if(round == 0  && turn == 3)
                {

                    Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);
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
                    Intent intent = new Intent(getApplicationContext(), MatchingGameActivity.class);
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
                    if(turn == 1){
                        if(switcha == 0)
                        {
                            intent.putExtra("turn", 2);
                        }
                        else{
                            intent.putExtra("turn", 1);
                        }

                    }
                    if(turn == 2){
                        if(switcha == 0)
                        {
                            intent.putExtra("turn", 1);
                        }
                        else{
                            intent.putExtra("turn", 2);
                        }
                    }
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
        timera.cancel();
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

            if(turn == 1){
                if(switcha == 0)
                {
                    intent.putExtra("turn", 2);
                }
                else{
                    intent.putExtra("turn", 1);
                }

            }
            if(turn == 2){
                if(switcha == 0)
                {
                    intent.putExtra("turn", 1);
                }
                else{
                    intent.putExtra("turn", 2);
                }
            }
            finish();
            startActivity(intent);


        }if(round == 0  && turn == 3)
        {

            Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);
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
            Intent intent = new Intent(getApplicationContext(), MatchingGameActivity.class);
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
            if(turn == 1){
                if(switcha == 0)
                {
                    intent.putExtra("turn", 2);
                }
                else{
                    intent.putExtra("turn", 1);
                }

            }
            if(turn == 2){
                if(switcha == 0)
                {
                    intent.putExtra("turn", 1);
                }
                else{
                    intent.putExtra("turn", 2);
                }
            }
            intent.putExtra("round", 1);
            finish();
            startActivity(intent);



        }


    }
    public void  matchpair( ) {
            if(turn == 1 || turn ==3)
            {

                db.collection("/games/matchinggame/round").document(round+"")
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                String texttemp1 = null;
                                int aa = 0;
                                String ag = null;
                                String lg = null;
                                switch (tempa) {
                                    case R.id.matchingPair1Left: lg = "l1";aa=R.id.matchingPair1LeftTxt;texttemp1 ="l1c" ;  break;
                                    case R.id.matchingPair2Left: lg = "l2"; aa=R.id.matchingPair2LeftTxt;texttemp1 ="l2c" ; break;
                                    case R.id.matchingPair3Left: lg = "l3"; aa=R.id.matchingPair3LeftTxt;texttemp1 ="l3c"; break;
                                    case R.id.matchingPair4Left: lg = "l4"; aa=R.id.matchingPair4LeftTxt; texttemp1 ="l4c"; break;
                                    case R.id.matchingPair5Left: lg = "l5"; aa=R.id.matchingPair5LeftTxt; texttemp1 ="l5c"; break;
                                }
                                int bb=0;
                                switch (tempb) {
                                    case R.id.matchingPair1Right: ag = "a1";bb=R.id.matchingPair1RightTxt;   break;
                                    case R.id.matchingPair2Right: ag = "a2";bb=R.id.matchingPair2RightTxt; break;
                                    case R.id.matchingPair3Right: ag = "a3";bb=R.id.matchingPair3RightTxt;  break;
                                    case R.id.matchingPair4Right: ag = "a4"; bb=R.id.matchingPair4RightTxt; break;
                                    case R.id.matchingPair5Right: ag = "a5"; bb=R.id.matchingPair5RightTxt; break;
                                }
                                LinearLayout ta = MatchingGameActivity.this.findViewById(tempa);
                                LinearLayout tb = MatchingGameActivity.this.findViewById(tempb);
                                TextView tbb = MatchingGameActivity.this.findViewById(bb);
                                TextView taa = MatchingGameActivity.this.findViewById(aa);
                                if(tbb.getText().toString().equals(documentSnapshot.getString(texttemp1))) {
                                    ++matchcounter;

                                    if(turn == 1)
                                    {
                                        mSocket.emit("pointsca",5);
                                        mSocket.emit("matchsend",texttemp1);
                                    }
                                    ++counter;
                                    rScore =String.valueOf(Integer.valueOf(rScore) + 5);
                                    TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                    field1.setText(rScore);
                                    tempa = 0;
                                    tempb = 0;
                                    ta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                    tb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                                    ta.setClickable(false);
                                    tb.setClickable(false);
                                    if(counter == 5 && turn ==3)
                                    {
                                        Pobeda();
                                    }
                                    if(counter == 5 && turn ==1)
                                    {
                                        if(matchcounter <5 && switcha == 0)
                                        {
                                            mSocket.emit("turn");
                                            switcha = 1;
                                            turn = 2;
                                        }
                                        else
                                        {
                                            if(matchcounter < 5 && switcha == 1)
                                            {
                                                mSocket.emit("nextgamec");
                                                Pobeda();
                                            }
                                        }




                                    }


                                }
                                if(!tbb.getText().toString().equals(documentSnapshot.getString(texttemp1))){
                                    ++counter;
                                    if(turn ==1)
                                    {
                                        mSocket.emit("wrongmatchsend",lg,ag);
                                    }

                                    if(counter == 5 && turn ==3)
                                    {
                                        Pobeda();
                                    }
                                    if(counter == 5 && turn ==1)
                                    {
                                        if(matchcounter <5 && switcha == 0)
                                        {
                                            mSocket.emit("turn");
                                            switcha = 1;
                                            turn = 2;
                                        }
                                        else
                                        {
                                            if(matchcounter < 5 && switcha == 1)
                                            {
                                                mSocket.emit("nextgamec");
                                                Pobeda();
                                            }
                                        }




                                    }

                                    tempa = 0;
                                    tempb = 0;
                                    ta.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                    tb.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                                }
                            }

                            //db get string and set it to int
                        });


            }
    }



        public void  match(View a)
        {
          if(turn == 1 ||  turn == 3)
          {
              int procced = 0;
              switch (a.getId()) {
                  case R.id.matchingPair1Left: procced=1;   break;
                  case R.id.matchingPair2Left: procced=1;  break;
                  case R.id.matchingPair3Left: procced=1;  break;
                  case R.id.matchingPair4Left:  procced=1; break;
                  case R.id.matchingPair5Left:  procced=1; break;
                  case R.id.matchingPair1Right: procced=2;   break;
                  case R.id.matchingPair2Right: procced=2;  break;
                  case R.id.matchingPair3Right: procced=2;  break;
                  case R.id.matchingPair4Right:  procced=2; break;
                  case R.id.matchingPair5Right:  procced=2; break;
              }

              if(procced == 1)
              {
                  LinearLayout ta = MatchingGameActivity.this.findViewById(a.getId());
                  ta.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                  if(tempa == 0)
                  {
                      tempa = a.getId();
                  }



              }
              if(procced == 2)
              {

                  if(tempa == 0)
                  {

                  }
                  else {
                      LinearLayout ta = MatchingGameActivity.this.findViewById(a.getId());
                      ta.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                      tempb= a.getId();
                      matchpair();


                  }


              }

          }
        }







}