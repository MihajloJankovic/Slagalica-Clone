package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.socket.client.Socket;

public class AssociationsGame extends AppCompatActivity {

        private TextView A2Txt;
        private TextView A1Txt;
        private TextView A3Txt;
        private TextView A4Txt;

        private int Aopen;
    private int Bopen;
    private int Copen;
    private int Dopen;
    private int Ag;
    private int Bg;
    private int Cg;
    private int Dg;
    int ab;
    private TextView B2Txt;
    private String m_Text = "";
    private TextView B1Txt;
    private TextView B3Txt;
    private TextView B4Txt;
    private TextView C2Txt;
    private TextView C1Txt;
    private TextView C3Txt;
    private TextView C4Txt;
    private TextView D2Txt;
    private TextView D1Txt;
    private TextView D3Txt;
    private TextView D4Txt;

    private TextView ATxt;
    private TextView BTxt;
    private TextView CTxt;
    private TextView DTxt;
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
    private int opened;
    private Socket mSocket;
    CountDownTimer timera;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         db = FirebaseFirestore.getInstance();
        ab = 0;
        Aopen = 0;
        Bopen = 0;
        Copen = 0;
        Dopen = 0;
        Ag= 0;
        Bg= 0;
        Cg= 0;
        Dg= 0;
        this.opened = 0;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_game);
        getSupportActionBar().hide();
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
               con app = (con)AssociationsGame.this.getApplication();
               this.mSocket = app.getSocket();
               mSocket.on("changeturn",(a) -> {

                   this.turn = 1;

               });
               mSocket.on("opens",(a) -> {

                   openOne(a[0].toString());
               });
               mSocket.on("opencs",(a) -> {

                   openCOlumn(a[0].toString());
               });
               mSocket.on("ennemywin",(a) -> {

                   Pobeda();
               });

           }
        }
            //The key argument here must match that used in the other activity
        }


        this.A2Txt = (TextView) findViewById(R.id.A2Txt);
        this.A1Txt = (TextView) findViewById(R.id.A1Txt);
        this.A3Txt = (TextView) findViewById(R.id.A3Txt);
        this.A4Txt = (TextView) findViewById(R.id.A4Txt);
        this.B2Txt = (TextView) findViewById(R.id.B2Txt);
        this.B1Txt = (TextView) findViewById(R.id.B1Txt);
        this.B3Txt = (TextView) findViewById(R.id.B3Txt);
        this.B4Txt = (TextView) findViewById(R.id.B4Txt);
        this.C2Txt = (TextView) findViewById(R.id.B2Txt);
        this.C1Txt = (TextView) findViewById(R.id.B1Txt);
        this.C3Txt = (TextView) findViewById(R.id.B3Txt);
        this.C4Txt = (TextView) findViewById(R.id.B4Txt);
        this.D2Txt = (TextView) findViewById(R.id.B2Txt);
        this.D1Txt = (TextView) findViewById(R.id.B1Txt);
        this.D3Txt = (TextView) findViewById(R.id.B3Txt);
        this.D4Txt = (TextView) findViewById(R.id.B4Txt);
        this.CTxt = (TextView) findViewById(R.id.CTxt);
        this.DTxt = (TextView) findViewById(R.id.DTxt);
        this.ATxt = (TextView) findViewById(R.id.ATxt);
        this.BTxt = (TextView) findViewById(R.id.BTxt);
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
                timer.setText("done!");
                if(round == 1 && ab == 0 && turn != 3)
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
                    intent.putExtra("round", 0);
                    intent.putExtra("turn", turn);
                    finish();
                    startActivity(intent);


                }if(round == 0 && ab == 0 && turn == 3)
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
                if(round == 0 && ab== 0 && turn !=3)
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
                    intent.putExtra("turn", turn);
                    intent.putExtra("round", 1);
                    finish();
                    startActivity(intent);



                }

            }
        }.start();







    }
    public void openCOlumn(String a) {
        if( turn != 3)
        {


                    String idd = "";
                    int res = 6 ;
                    int is = 0;
                    switch (a) {
                        case "BTxt":
                            // do something
                            idd = "BTxt";
                            is = R.id.BTxt;
                            res=   res - Bopen;

                            break;
                        case "CTxt":
                            // do something else
                            idd = "CTxt";
                            is = R.id.CTxt;
                            res=   res - Copen;
                            break;
                        case "DTxt":
                            // i'm lazy, do nothing
                            idd = "DTxt";
                            is = R.id.DTxt;
                            res=   res - Dopen;
                            break;
                        case "ATxt":
                            // do something
                            idd = "ATxt";
                            is = R.id.ATxt;
                            res=   res - Aopen;
                            break;
                    }
                    String finalIdd = idd;
                    int finalRes = res;

            int finalIs = is;
            int finalIs1 = is;
            db.collection("/games/asocijacije/1").document(round+"")
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(true)
                                    {


                                        bScore =String.valueOf(Integer.valueOf(bScore) + finalRes);
                                        TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                        field1.setText(bScore);
                                        TextView field = (TextView) findViewById(finalIs);
                                        field.setText(documentSnapshot.getString(a));
                                        switch (finalIs1) {
                                            case R.id.ATxt:
                                                // do something
                                                Ag=1;
                                                TextView field5 = (TextView) findViewById(R.id.A1Txt);
                                                field5.setText(documentSnapshot.getString("A1Txt"));
                                                TextView field2 = (TextView) findViewById(R.id.A2Txt);
                                                field2.setText(documentSnapshot.getString("A2Txt"));

                                                TextView field3 = (TextView) findViewById(R.id.A3Txt);
                                                field3.setText(documentSnapshot.getString("A3Txt"));

                                                TextView field4 = (TextView) findViewById(R.id.A4Txt);
                                                field4.setText(documentSnapshot.getString("A4Txt"));

                                                break;
                                            case R.id.BTxt:
                                                Bg = 1;
                                                TextView field51 = (TextView) findViewById(R.id.B1Txt);
                                                field51.setText(documentSnapshot.getString("B1Txt"));
                                                TextView field21 = (TextView) findViewById(R.id.B2Txt);
                                                field21.setText(documentSnapshot.getString("B2Txt"));

                                                TextView field31 = (TextView) findViewById(R.id.B3Txt);
                                                field31.setText(documentSnapshot.getString("B3Txt"));

                                                TextView field41 = (TextView) findViewById(R.id.B4Txt);
                                                field41.setText(documentSnapshot.getString("B4Txt"));

                                                break;
                                            case R.id.CTxt:
                                                Cg= 1;
                                                TextView field52 = (TextView) findViewById(R.id.C1Txt);
                                                field52.setText(documentSnapshot.getString("C1Txt"));
                                                TextView field22 = (TextView) findViewById(R.id.C2Txt);
                                                field22.setText(documentSnapshot.getString("C2Txt"));

                                                TextView field32 = (TextView) findViewById(R.id.C3Txt);
                                                field32.setText(documentSnapshot.getString("C3Txt"));

                                                TextView field42 = (TextView) findViewById(R.id.C4Txt);
                                                field42.setText(documentSnapshot.getString("C4Txt"));

                                                break;
                                            case R.id.DTxt:
                                                Dg = 1;
                                                TextView field53 = (TextView) findViewById(R.id.D1Txt);
                                                field53.setText(documentSnapshot.getString("D1Txt"));
                                                TextView field23 = (TextView) findViewById(R.id.D2Txt);
                                                field23.setText(documentSnapshot.getString("D2Txt"));

                                                TextView field33 = (TextView) findViewById(R.id.D3Txt);
                                                field33.setText(documentSnapshot.getString("D3Txt"));

                                                TextView field43 = (TextView) findViewById(R.id.D4Txt);
                                                field43.setText(documentSnapshot.getString("D4Txt"));

                                                break;
                                        }

                                    }


                                }

                                //db get string and set it to int
                            });


                }
            }


    public void addStep(View v) {
      if((turn == 1 || turn == 3) && opened == 1)
      {
          AlertDialog.Builder alert = new AlertDialog.Builder(this);
          alert.setTitle("Enter Value");


          // Set an EditText view to get user input
          final EditText input = new EditText(this);
          alert.setView(input);

          alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                  String value = String.valueOf(input.getText());
                  // Do something with value!

               if(turn != 3)
               {
                   mSocket.emit("turn");
                   turn = 2;
                   opened = 0;
               }
                  String idd = "";
                  int res = 6 ;
                  switch (v.getId()) {
                      case R.id.BTxt:
                          // do something
                          idd = "BTxt";
                        res=   res - Bopen;

                          break;
                      case R.id.CTxt:
                          // do something else
                          idd = "CTxt";
                          res=   res - Copen;
                          break;
                      case R.id.DTxt:
                          // i'm lazy, do nothing
                          idd = "DTxt";
                          res=   res - Dopen;
                          break;
                      case R.id.ATxt:
                          // do something
                          idd = "ATxt";
                          res=   res - Aopen;
                          break;
                  }
                  String finalIdd = idd;
                  int finalRes = res;

                  String finalIdd1 = idd;
                  db.collection("/games/asocijacije/1").document(round+"")
                          .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                              @Override
                              public void onSuccess(DocumentSnapshot documentSnapshot) {
                                  if(value.equals(documentSnapshot.getString(finalIdd)))
                                  {

                                      if(turn != 3)
                                      {
                                          mSocket.emit("openColumn", finalIdd1);

                                      }

                                      rScore =String.valueOf(Integer.valueOf(rScore) + finalRes);
                                      TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                      field1.setText(rScore);
                                      TextView field = (TextView) findViewById(v.getId());
                                      field.setText(value);
                                      switch (v.getId()) {
                                          case R.id.ATxt:
                                              // do something
                                              Ag=1;
                                              TextView field5 = (TextView) findViewById(R.id.A1Txt);
                                              field5.setText(documentSnapshot.getString("A1Txt"));
                                              TextView field2 = (TextView) findViewById(R.id.A2Txt);
                                              field2.setText(documentSnapshot.getString("A2Txt"));

                                              TextView field3 = (TextView) findViewById(R.id.A3Txt);
                                              field3.setText(documentSnapshot.getString("A3Txt"));

                                              TextView field4 = (TextView) findViewById(R.id.A4Txt);
                                              field4.setText(documentSnapshot.getString("A4Txt"));

                                              break;
                                          case R.id.BTxt:
                                              Bg = 1;
                                              TextView field51 = (TextView) findViewById(R.id.B1Txt);
                                              field51.setText(documentSnapshot.getString("B1Txt"));
                                              TextView field21 = (TextView) findViewById(R.id.B2Txt);
                                              field21.setText(documentSnapshot.getString("B2Txt"));

                                              TextView field31 = (TextView) findViewById(R.id.B3Txt);
                                              field31.setText(documentSnapshot.getString("B3Txt"));

                                              TextView field41 = (TextView) findViewById(R.id.B4Txt);
                                              field41.setText(documentSnapshot.getString("B4Txt"));

                                              break;
                                          case R.id.CTxt:
                                              Cg= 1;
                                              TextView field52 = (TextView) findViewById(R.id.C1Txt);
                                              field52.setText(documentSnapshot.getString("C1Txt"));
                                              TextView field22 = (TextView) findViewById(R.id.C2Txt);
                                              field22.setText(documentSnapshot.getString("C2Txt"));

                                              TextView field32 = (TextView) findViewById(R.id.C3Txt);
                                              field32.setText(documentSnapshot.getString("C3Txt"));

                                              TextView field42 = (TextView) findViewById(R.id.C4Txt);
                                              field42.setText(documentSnapshot.getString("C4Txt"));

                                              break;
                                          case R.id.DTxt:
                                              Dg = 1;
                                              TextView field53 = (TextView) findViewById(R.id.D1Txt);
                                              field53.setText(documentSnapshot.getString("D1Txt"));
                                              TextView field23 = (TextView) findViewById(R.id.D2Txt);
                                              field23.setText(documentSnapshot.getString("D2Txt"));

                                              TextView field33 = (TextView) findViewById(R.id.D3Txt);
                                              field33.setText(documentSnapshot.getString("D3Txt"));

                                              TextView field43 = (TextView) findViewById(R.id.D4Txt);
                                              field43.setText(documentSnapshot.getString("D4Txt"));

                                              break;
                                      }

                                      if(Aopen == 4 && Bopen==4 &&Copen==4 &&Dopen ==4)
                                      {
                                          opened = 1;
                                      }
                                        Konacno(v);

                                  }
                                  else{
                                      if(turn != 3 )
                                      {
                                          mSocket.emit("turn");
                                          turn = 2;
                                          opened = 0;
                                      }
                                  }

                              }

                              //db get string and set it to int
                          });


              }
          });

          alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                  // Canceled.
                  if(Aopen == 4 && Bopen==4 &&Copen==4 &&Dopen ==4)
                  {
                      opened = 1;
                  }
              }
          });

          alert.show();
      }

    }
    public void open(View v) {
        if((turn == 1 || turn == 3) && opened == 0)
        {
        String idd = "";
       int  idcase = v.getId();
            switch (v.getId()) {
                case R.id.A1Txt:
                    // do something
                    idd = "A1Txt"  ;
                    ++Aopen;
                    break;
                case R.id.A2Txt:
                    // do something else
                    idd =  "A2Txt" ;
                    ++Aopen;
                    break;
                case R.id.A3Txt:
                    // i'm lazy, do nothing
                    idd ="A3Txt"   ;
                    ++Aopen;
                    break;
                case R.id.A4Txt:
                    // do something
                    idd =  "A4Txt" ;
                    ++Aopen;
                    break;
                case R.id.B1Txt:
                    // do something else
                    idd =  "B1Txt" ;
                    ++Bopen;
                    break;
                case R.id.B2Txt:
                    // i'm lazy, do nothing
                    idd = "B2Txt"  ;
                    ++Bopen;
                    break;
                case R.id.B3Txt:
                    // do something
                    idd =  "B3Txt" ;
                    ++Bopen;
                    break;
                case R.id.B4Txt:
                    // do something else
                    idd =   "B4Txt";
                    ++Bopen;
                    break;
                case R.id.C1Txt:
                    // i'm lazy, do nothing
                    idd =  "C1Txt" ;
                    ++Copen;
                    break;
                case R.id.C2Txt:
                    // do something
                    idd =  "C2Txt" ;
                    ++Copen;
                    break;
                case R.id.C3Txt:
                    // do something else
                    idd =  "C3Txt" ;
                    ++Copen;
                    break;
                case R.id.C4Txt:
                    // i'm lazy, do nothing
                    idd =  "C4Txt" ;
                    ++Copen;
                    break;
                case R.id.D1Txt:
                    // do something
                    idd =  "D1Txt" ;
                    ++Dopen;
                    break;
                case R.id.D2Txt:
                    // do something else
                    idd =   "D2Txt";
                    ++Dopen;
                    break;
                case R.id.D3Txt:
                    // i'm lazy, do nothing
                    idd =  "D3Txt" ;
                    ++Dopen;
                    break;
                case R.id.D4Txt:
                    // i'm lazy, do nothing
                    idd =  "D4Txt" ;
                    ++Dopen;

                    break;
            }

            String finalIdd = idd;
          if(turn != 3)
          {
              mSocket.emit("open",finalIdd);
          }
            db.collection("/games/asocijacije/1").document(round+"")
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String   value = documentSnapshot.getString(finalIdd);
                            TextView field = (TextView) findViewById(v.getId());
                            field.setText(value);
                            opened = 1;
                        }

            //db get string and set it to int
        });

    }
}
    public void openOne(String a) {
        if( turn != 3)
        {
            String idd = "";
            int is = 0;
            switch (a) {
                case "A1Txt":
                    // do something
                    idd = "A1Txt"  ;
                    is = R.id.A1Txt;
                    ++Aopen;
                    break;
                case "A2Txt":
                    // do something else
                    idd =  "A2Txt" ;
                    is = R.id.A2Txt;
                    ++Aopen;
                    break;
                case "A3Txt" :
                    // i'm lazy, do nothing
                    idd ="A3Txt"   ;
                    is = R.id.A3Txt;
                    ++Aopen;
                    break;
                case "A4Txt":
                    // do something
                    idd =  "A4Txt" ;
                    is = R.id.A4Txt;
                    ++Aopen;
                    break;
                case  "B1Txt" :
                    // do something else
                    idd =  "B1Txt" ;
                    is = R.id.B1Txt;
                    ++Bopen;
                    break;
                case "B2Txt":
                    // i'm lazy, do nothing
                    idd = "B2Txt"  ;
                    is = R.id.B2Txt;
                    ++Bopen;
                    break;
                case "B3Txt":
                    // do something
                    idd =  "B3Txt" ;
                    is = R.id.B3Txt;
                    ++Bopen;
                    break;
                case "B4Txt":
                    // do something else
                    idd =   "B4Txt";
                    is = R.id.B4Txt;
                    ++Bopen;
                    break;
                case "C1Txt":
                    // i'm lazy, do nothing
                    idd =  "C1Txt" ;
                    is = R.id.C1Txt;
                    ++Copen;
                    break;
                case "C2Txt" :
                    // do something
                    idd =  "C2Txt" ;
                    is = R.id.C2Txt;
                    ++Copen;
                    break;
                case "C3Txt" :
                    // do something else
                    idd =  "C3Txt" ;
                    is = R.id.C3Txt;
                    ++Copen;
                    break;
                case "C4Txt":
                    // i'm lazy, do nothing
                    idd =  "C4Txt" ;
                    is = R.id.C4Txt;
                    ++Copen;
                    break;
                case  "D1Txt":
                    // do something
                    idd =  "D1Txt" ;
                    is = R.id.D1Txt;
                    ++Dopen;
                    break;
                case"D2Txt":
                    // do something else
                    idd =   "D2Txt";
                    is = R.id.D2Txt;
                    ++Dopen;
                    break;
                case "D3Txt" :
                    // i'm lazy, do nothing
                    idd =  "D3Txt" ;
                    ++Dopen;
                    is = R.id.D3Txt;
                    break;
                case  "D4Txt" :
                    // i'm lazy, do nothing
                    idd =  "D4Txt" ;
                    is = R.id.D4Txt;
                    ++Dopen;

                    break;
            }

            String finalIdd = idd;
            int finalIs = is;
            db.collection("/games/asocijacije/1").document(round+"")
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String   value = documentSnapshot.getString(finalIdd);
                            TextView field = (TextView) findViewById(finalIs);
                            field.setText(value);

                        }

                        //db get string and set it to int
                    });

        }
    }
    public void Pobeda() {
        if(turn != 3) {


            String finalIdd = "Konacno";
            db.collection("/games/asocijacije/1").document(round+"")
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(true)
                            {

                                int rs = 31;
                                if(Ag == 1)
                                {
                                    rs= rs-6;
                                }
                                else{
                                    rs=  rs-Aopen;
                                }
                                if(Bg == 1)
                                {
                                    rs= rs-6;
                                }else{
                                    rs=  rs-Bopen;
                                }
                                if(Cg == 1)
                                {
                                    rs= rs-6;
                                }else{
                                    rs=  rs-Copen;
                                }
                                if(Dg == 1)
                                {
                                    rs= rs-6;
                                }else{
                                    rs=  rs-Dopen;
                                }
                                bScore =String.valueOf(Integer.valueOf(bScore) + rs);
                                TextView field1 = (TextView) findViewById(R.id.bluePlayerScore);
                                field1.setText(rScore);

                                TextView field5 = (TextView) findViewById(R.id.A1Txt);
                                field5.setText(documentSnapshot.getString("A1Txt"));
                                TextView field2 = (TextView) findViewById(R.id.A2Txt);
                                field2.setText(documentSnapshot.getString("A2Txt"));

                                TextView field3 = (TextView) findViewById(R.id.A3Txt);
                                field3.setText(documentSnapshot.getString("A3Txt"));

                                TextView field4 = (TextView) findViewById(R.id.A4Txt);
                                field4.setText(documentSnapshot.getString("A4Txt"));


                                TextView field51 = (TextView) findViewById(R.id.B1Txt);
                                field51.setText(documentSnapshot.getString("B1Txt"));
                                TextView field21 = (TextView) findViewById(R.id.B2Txt);
                                field21.setText(documentSnapshot.getString("B2Txt"));

                                TextView field31 = (TextView) findViewById(R.id.B3Txt);
                                field31.setText(documentSnapshot.getString("B3Txt"));

                                TextView field41 = (TextView) findViewById(R.id.B4Txt);
                                field41.setText(documentSnapshot.getString("B4Txt"));


                                TextView field52 = (TextView) findViewById(R.id.C1Txt);
                                field52.setText(documentSnapshot.getString("C1Txt"));
                                TextView field22 = (TextView) findViewById(R.id.C2Txt);
                                field22.setText(documentSnapshot.getString("C2Txt"));

                                TextView field32 = (TextView) findViewById(R.id.C3Txt);
                                field32.setText(documentSnapshot.getString("C3Txt"));

                                TextView field42 = (TextView) findViewById(R.id.C4Txt);
                                field42.setText(documentSnapshot.getString("C4Txt"));


                                TextView field53 = (TextView) findViewById(R.id.D1Txt);
                                field53.setText(documentSnapshot.getString("D1Txt"));
                                TextView field23 = (TextView) findViewById(R.id.D2Txt);
                                field23.setText(documentSnapshot.getString("D2Txt"));

                                TextView field33 = (TextView) findViewById(R.id.D3Txt);
                                field33.setText(documentSnapshot.getString("D3Txt"));

                                TextView field43 = (TextView) findViewById(R.id.D4Txt);
                                field43.setText(documentSnapshot.getString("D4Txt"));

                                TextView field531 = (TextView) findViewById(R.id.ATxt);
                                field531.setText(documentSnapshot.getString("ATxt"));
                                TextView field231 = (TextView) findViewById(R.id.BTxt);
                                field231.setText(documentSnapshot.getString("BTxt"));

                                TextView field331 = (TextView) findViewById(R.id.CTxt);
                                field331.setText(documentSnapshot.getString("CTxt"));

                                TextView field431 = (TextView) findViewById(R.id.DTxt);
                                field431.setText(documentSnapshot.getString("DTxt"));

                                TextView field4312 = (TextView) findViewById(R.id.finalTxt);
                                field4312.setText(documentSnapshot.getString("Konacno"));

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(round == 1 && turn !=3)
                                        {
                                            ab=1;
                                            timera.cancel();
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
                                            intent.putExtra("turn", turn);
                                            finish();
                                            startActivity(intent);


                                        }
                                        if(round == 0 && turn !=3)
                                        {
                                            ab=1;
                                            timera.cancel();
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
                                            intent.putExtra("round", 1);
                                            intent.putExtra("turn", turn);
                                            finish();
                                            startActivity(intent);

                                        }
                                        if(round == 0 && turn ==3)
                                        {
                                            ab=1;
                                            timera.cancel();
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



                                    }
                                }, 2000);

                            }


                        }


                        //db get string and set it to int by Mihajlo
                    });
        }
            }






    public void Konacno(View v) {
        if((turn == 1 || turn == 3) && opened == 1)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter Final");


            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(Aopen == 4 && Bopen==4 &&Copen==4 &&Dopen ==4)
                    {
                        opened = 1;

                    }
                   String valuea = String.valueOf(input.getText());


                    opened=0;
                    String idd = "";

                    String finalIdd = "Konacno";
                    db.collection("/games/asocijacije/1").document(round+"")
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if(valuea.equals(documentSnapshot.getString(finalIdd)))
                                    {
                                        if(turn != 3 )
                                        {
                                            mSocket.emit("turn");
                                            turn = 2;
                                            opened = 0;
                                        }
                                        int rs = 31;
                                        if(Ag == 1)
                                        {
                                            rs= rs-6;
                                        }
                                        else{
                                          rs=  rs-Aopen;
                                        }
                                        if(Bg == 1)
                                        {
                                            rs= rs-6;
                                        }else{
                                            rs=  rs-Bopen;
                                        }
                                        if(Cg == 1)
                                        {
                                            rs= rs-6;
                                        }else{
                                            rs=  rs-Copen;
                                        }
                                        if(Dg == 1)
                                        {
                                            rs= rs-6;
                                        }else{
                                            rs=  rs-Dopen;
                                        }
                                        rScore =String.valueOf(Integer.valueOf(rScore) + rs);
                                        TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                                        field1.setText(rScore);

                                        TextView field5 = (TextView) findViewById(R.id.A1Txt);
                                        field5.setText(documentSnapshot.getString("A1Txt"));
                                        TextView field2 = (TextView) findViewById(R.id.A2Txt);
                                        field2.setText(documentSnapshot.getString("A2Txt"));

                                        TextView field3 = (TextView) findViewById(R.id.A3Txt);
                                        field3.setText(documentSnapshot.getString("A3Txt"));

                                        TextView field4 = (TextView) findViewById(R.id.A4Txt);
                                        field4.setText(documentSnapshot.getString("A4Txt"));


                                            TextView field51 = (TextView) findViewById(R.id.B1Txt);
                                            field51.setText(documentSnapshot.getString("B1Txt"));
                                            TextView field21 = (TextView) findViewById(R.id.B2Txt);
                                            field21.setText(documentSnapshot.getString("B2Txt"));

                                            TextView field31 = (TextView) findViewById(R.id.B3Txt);
                                            field31.setText(documentSnapshot.getString("B3Txt"));

                                            TextView field41 = (TextView) findViewById(R.id.B4Txt);
                                            field41.setText(documentSnapshot.getString("B4Txt"));


                                            TextView field52 = (TextView) findViewById(R.id.C1Txt);
                                            field52.setText(documentSnapshot.getString("C1Txt"));
                                            TextView field22 = (TextView) findViewById(R.id.C2Txt);
                                            field22.setText(documentSnapshot.getString("C2Txt"));

                                            TextView field32 = (TextView) findViewById(R.id.C3Txt);
                                            field32.setText(documentSnapshot.getString("C3Txt"));

                                            TextView field42 = (TextView) findViewById(R.id.C4Txt);
                                            field42.setText(documentSnapshot.getString("C4Txt"));


                                            TextView field53 = (TextView) findViewById(R.id.D1Txt);
                                            field53.setText(documentSnapshot.getString("D1Txt"));
                                            TextView field23 = (TextView) findViewById(R.id.D2Txt);
                                            field23.setText(documentSnapshot.getString("D2Txt"));

                                            TextView field33 = (TextView) findViewById(R.id.D3Txt);
                                            field33.setText(documentSnapshot.getString("D3Txt"));

                                            TextView field43 = (TextView) findViewById(R.id.D4Txt);
                                            field43.setText(documentSnapshot.getString("D4Txt"));

                                        TextView field531 = (TextView) findViewById(R.id.ATxt);
                                        field531.setText(documentSnapshot.getString("ATxt"));
                                        TextView field231 = (TextView) findViewById(R.id.BTxt);
                                        field231.setText(documentSnapshot.getString("BTxt"));

                                        TextView field331 = (TextView) findViewById(R.id.CTxt);
                                        field331.setText(documentSnapshot.getString("CTxt"));

                                        TextView field431 = (TextView) findViewById(R.id.DTxt);
                                        field431.setText(documentSnapshot.getString("DTxt"));

                                        TextView field4312 = (TextView) findViewById(R.id.finalTxt);
                                        field4312.setText(documentSnapshot.getString("Konacno"));

                                        if(turn != 3)
                                        {
                                            mSocket.emit("Pobeda");

                                        }
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(round == 1 && turn !=3)
                                                {
                                                    ab=1;
                                                    timera.cancel();
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
                                                    intent.putExtra("turn", turn);
                                                    finish();
                                                    startActivity(intent);

                                                }
                                                if(round == 0 && turn !=3)
                                                {
                                                    ab=1;
                                                    timera.cancel();
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
                                                    intent.putExtra("round", 1);
                                                    intent.putExtra("turn", turn);
                                                    finish();
                                                    startActivity(intent);


                                                }
                                                if(round == 0 && turn ==3)
                                                {
                                                    ab=1;
                                                    timera.cancel();
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


                                            }
                                        }, 2000);

                                    }
                                    else{

                                        if(turn != 3 )
                                        {
                                            mSocket.emit("turn");
                                            turn = 2;
                                            opened = 0;
                                        }
                                    }

                                }


                                //db get string and set it to int by Mihajlo
                            });



                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    if(Aopen == 4 && Bopen==4 &&Copen==4 &&Dopen ==4 && turn ==3)
                    {
                        opened = 1;
                    }
                    if(Bg == 1 && Ag==1 &&Cg==1 &&Dg ==1 && turn ==3)
                    {
                        opened = 1;
                    }
                    if(turn != 3 )
                    {
                        mSocket.emit("turn");
                        turn = 2;
                        opened = 0;
                    }
                }
            });

            alert.show();


        }
    }
}