package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.socket.client.Socket;

public class CombinationsGame extends AppCompatActivity {
    private TextView timer;
    private int turn;
    private int round;
    private String rName;
    private String bName;
    private String rScore;
    private String bScore;
    private TextView rName1;
    private ImageView s1;
    private ImageView s2;
    private ImageView s3;
    private ImageView s4;
    private ImageView s5;
    private ImageView s6;
    private TextView bName1;
    private TextView rScore1;
    private TextView bScore1;
    private int hint = 0;

    private Socket mSocket;
    private int posetion =1;
    private int cardPosition =1;
    private int switcha = 0;
    int ab=0;
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
        setContentView(R.layout.activity_combinations_game);
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

                    Konekcija app = (Konekcija) CombinationsGame.this.getApplication();
                    this.mSocket = app.getSocket();
                    mSocket.on("changeturna",(a) -> {

                        this.turn = 1;
                        switcha = 1;
                        posetion = 1;
                        allSubmited = 0;
                        runOnUiThread(() -> Toast.makeText(CombinationsGame.this, "Your turn !", Toast.LENGTH_SHORT).show());
                        cardPosition = 25;

                    });
                    mSocket.on("enemyguessc",(a) -> {


                        Map<String,Object> mapa;
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            mapa = mapper.readValue(a[0].toString(), Map.class);

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                         addToUIHelp(Integer.valueOf( mapa.get("a").toString()),25);
                        addToUIHelp(Integer.valueOf(mapa.get("b").toString()),26);
                        addToUIHelp(Integer.valueOf(mapa.get("c").toString()),27);
                        addToUIHelp(Integer.valueOf(mapa.get("d").toString()),28);

                    });

                    mSocket.on("loptac",(a) -> {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Map<String,Object> mapa;
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    mapa = mapper.readValue(a[0].toString(), Map.class);

                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                int bb = Integer.valueOf( mapa.get("a").toString());
                                int cc = Integer.valueOf( mapa.get("b").toString());
                                addToBall(bb,cc);

                            }
                        });

                    });

                    mSocket.on("guessc",(a) -> {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                              
                                Map<String,Object> mapa;
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    mapa = mapper.readValue(a[0].toString(), Map.class);

                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                int aa = Integer.valueOf( mapa.get("a").toString());
                                int bb = Integer.valueOf( mapa.get("b").toString());
                                int cc = Integer.valueOf( mapa.get("c").toString());
                                int dd = Integer.valueOf( mapa.get("d").toString());

                                addToUI(aa);
                                ++cardPosition;
                                addToUI(bb);
                                ++cardPosition;
                                addToUI(cc);
                                ++cardPosition;
                                addToUI(dd);
                                ++cardPosition;


                            }
                        });



                    });
                    mSocket.on("passcomc",(a) -> {

                                Map<String,Object> mapa;
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    mapa = mapper.readValue(a[0].toString(), Map.class);

                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                int aa = Integer.valueOf( mapa.get("a").toString());
                                int bb = Integer.valueOf( mapa.get("b").toString());
                                int cc = Integer.valueOf( mapa.get("c").toString());
                                int dd = Integer.valueOf( mapa.get("d").toString());
                                combination.put(1,aa);
                                combination.put(2,bb);
                                combination.put(3,cc);
                                combination.put(4,dd);






                    });
                    mSocket.on("pointsc",(a) -> {

                        bScore= String.valueOf(Integer.valueOf(bScore)+10);


                    });
                    mSocket.on("pointscac",(a) -> {

                        bScore= String.valueOf(Integer.valueOf(bScore)+Integer.valueOf(a[0].toString()));


                    });
                    mSocket.on("nextgamecc",(a) -> {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(true){

                                    for (int i =1;i<combination.values().size()+1;i++
                                    ) {
                                        int id =0;
                                        switch (i) {
                                            case 1:
                                                id = R.id.guessingCard29;
                                                break;
                                            case 2:
                                                id = R.id.guessingCard30;
                                                break;
                                            case 3:
                                                id = R.id.guessingCard31;
                                                break;
                                            case 4:
                                                id = R.id.guessingCard32;
                                                break;
                                        }
                                        ImageView im = (ImageView) findViewById(id);
                                        int bas=0;
                                        switch (i) {
                                            case 1:
                                                bas = combination.get(1);
                                                break;
                                            case 2:
                                                bas = combination.get(2);
                                                break;
                                            case 3:
                                                bas = combination.get(3);
                                                break;
                                            case 4:
                                                bas = combination.get(4);
                                                break;
                                        }
                                        switch (bas) {
                                            case 1:
                                                im.setBackground(s1.getBackground());break;
                                            case 2:
                                                im.setBackground(s2.getBackground());break;
                                            case 3:
                                                im.setBackground(s3.getBackground());break;
                                            case 4:
                                                im.setBackground(s4.getBackground());break;
                                            case 5:
                                                im.setBackground(s5.getBackground());break;
                                            case 6:
                                                im.setBackground(s6.getBackground());break;
                                        }


                                    }
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            timera.cancel();

                                            if(round == 1  && turn != 3)
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
                                                startActivity(intent);

                                            }
                                            if(round == 0  && turn !=3)
                                            {
                                                Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
            //The key argument here must match that used in the other activity
        }
        this.timer = (TextView) findViewById(R.id.timer);
        this.rScore1 = (TextView) findViewById(R.id.redPlayerScore);
        this.bScore1 = (TextView) findViewById(R.id.bluePlayerScore);
        this.rName1 = (TextView) findViewById(R.id.redPlayerName);
        this.bName1 = (TextView) findViewById(R.id.bluePlayerName);
        this.s1 = (ImageView) findViewById(R.id.symbol1);
        this.s2 = (ImageView) findViewById(R.id.symbol2);
        this.s3 = (ImageView) findViewById(R.id.symbol3);
        this.s4 = (ImageView) findViewById(R.id.symbol4);
        this.s5= (ImageView) findViewById(R.id.symbol5);
        this.s6= (ImageView) findViewById(R.id.symbol6);
        this.rScore1.setText(rScore);
        this.bScore1.setText(bScore);
        Random rand = new Random();

        for(int i =0;i <4 ;i++)
        {
            int randomNum = (int) ((Math.random() * (6 - 1)) + 1);
            combination.put(i+1,randomNum);
        }
        this.rName1.setText(rName);
        this.bName1.setText(bName);
        timera=    new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
                timer.setText("done!");
                for (int i =1;i<combination.values().size()+1;i++
                ) {
                    int id =0;
                    switch (i) {
                        case 1:
                            id = R.id.guessingCard25;
                            break;
                        case 2:
                            id = R.id.guessingCard26;
                            break;
                        case 3:
                            id = R.id.guessingCard27;
                            break;
                        case 4:
                            id = R.id.guessingCard28;
                            break;
                    }
                    ImageView im = (ImageView) findViewById(id);
                    int bas=0;
                    switch (i) {
                        case 1:
                            bas = combination.get(1);
                            break;
                        case 2:
                            bas = combination.get(2);
                            break;
                        case 3:
                            bas = combination.get(3);
                            break;
                        case 4:
                            bas = combination.get(4);
                            break;
                    }
                    switch (bas) {
                        case 1:
                            im.setBackground(s1.getBackground());break;
                        case 2:
                            im.setBackground(s2.getBackground());break;
                        case 3:
                            im.setBackground(s3.getBackground());break;
                        case 4:
                            im.setBackground(s4.getBackground());break;
                        case 5:
                            im.setBackground(s5.getBackground());break;
                        case 6:
                            im.setBackground(s6.getBackground());break;
                    }


                }
                timera.cancel();

                if(round == 1  && turn != 3)
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
                    startActivity(intent);

                }
                if(round == 0  && turn !=3)
                {
                    Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
                    startActivity(intent);

                }
                finish();
            }
        }.start();



    }
    public void open(View v) {
        if(guessedTrue ==1 || turn ==2 )
        {
            return;
        }
        switch (v.getId())
        {
            case R.id.symbol1 : if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,1);
                addToUI(1);
                if(posetion == 4){
                    allSubmited = 1;

                    check();
                }
                else {
                    posetion = posetion+1;
                }
                ++cardPosition;
                break;
            }
            case R.id.symbol2 :if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,2);
                addToUI(2);
                if(posetion == 4){
                    allSubmited = 1;

                    check();
                }
                else {
                    posetion = posetion+1;
                }
                ++cardPosition;
                break;
            }
            case R.id.symbol3 :if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,3);
                addToUI(3);
                if(posetion == 4){
                    allSubmited = 1;

                    check();
                }
                else {
                    posetion = posetion+1;
                }
                ++cardPosition;
                break;
            }
            case R.id.symbol4 :if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,4);
                addToUI(4);
                if(posetion == 4){
                    allSubmited = 1;
                    check();
                }
                else {
                    posetion = posetion+1;
                }

                ++cardPosition;
                break;
            }
            case R.id.symbol5 :if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,5);
                addToUI(5);
                if(posetion == 4){
                    allSubmited = 1;

                    check();
                }
                else {
                    posetion = posetion+1;
                }
                ++cardPosition;
                break;
            }
            case R.id.symbol6 :if(allSubmited == 1)
            {

                break;
            }
            else{
                guessedCombination.put(posetion,6);
                addToUI(6);
                if(posetion == 4){
                    allSubmited = 1;
                    check();

                }
                else {
                    posetion = posetion+1;
                }
                ++cardPosition;
                break;
            }
        }
        if(turn ==3 && ab ==1 ){
            guessedTrue=1;
            for (int i =1;i<combination.values().size()+1;i++
            ) {
                int id =0;
                switch (i) {
                    case 1:
                        id = R.id.guessingCard29;
                        break;
                    case 2:
                        id = R.id.guessingCard30;
                        break;
                    case 3:
                        id = R.id.guessingCard31;
                        break;
                    case 4:
                        id = R.id.guessingCard32;
                        break;
                }
                ImageView im = (ImageView) findViewById(id);
                int bas=0;
                switch (i) {
                    case 1:
                        bas = combination.get(1);
                        break;
                    case 2:
                        bas = combination.get(2);
                        break;
                    case 3:
                        bas = combination.get(3);
                        break;
                    case 4:
                        bas = combination.get(4);
                        break;
                }
                int finalBas = bas;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        switch (finalBas) {
                            case 1:
                                im.setBackground(s1.getBackground());break;
                            case 2:
                                im.setBackground(s2.getBackground());break;
                            case 3:
                                im.setBackground(s3.getBackground());break;
                            case 4:
                                im.setBackground(s4.getBackground());break;
                            case 5:
                                im.setBackground(s5.getBackground());break;
                            case 6:
                                im.setBackground(s6.getBackground());break;
                        }
                    }
                });



            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timera.cancel();

                    if(round == 1  && turn != 3)
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
                        startActivity(intent);

                    }
                    if(round == 0  && turn !=3)
                    {
                        Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
                        startActivity(intent);

                    }
                    finish();
                }
            }, 5000);
        }

    }
    public void check() {

        List<Integer> a = new ArrayList<>((Collection) guessedCombination.values());
        List<Integer> b = new ArrayList<>((Collection) combination.values());
        List<Integer> c= new ArrayList<>();
        Map<Integer,Integer> tempa= new HashMap<>(combination);
        int p = cardPosition -3;
        int status =0;
        if(turn != 3) {
            mSocket.emit("guess", a.get(0), a.get(1), a.get(2), a.get(3));
        }
        if(guessedTrue == 0 &&(turn ==3 || turn ==1))
        {

           for (int i =0;i < 4;i++)
           {
               if(a.get(i) == b.get(i))
               {
                    a.set(i,0);
                   b.set(i,0);
                    ++status;
                    if(cardPosition < 25)
                    {
                        if(turn != 3) {
                            mSocket.emit("lopta", p + i, 1);
                        }
                        addToBall(p+i,1);
                    }

               }
           }


            if(status == 4)
            {
                int hh=0;
                guessedTrue = 1;

                switch (cardPosition/4)
                {
                    case 1: case 2: rScore= String.valueOf(Integer.valueOf(rScore)+20);if(turn != 3){mSocket.emit("pointsca",20);}break;
                    case 3: case 4:rScore= String.valueOf(Integer.valueOf(rScore)+15);if(turn != 3){mSocket.emit("pointsca",15);}break;
                    case 5: case 6:rScore= String.valueOf(Integer.valueOf(rScore)+10);if(turn != 3){mSocket.emit("pointsca",10);}break;
                    case 7:rScore= String.valueOf(Integer.valueOf(rScore)+10);if(turn != 3){mSocket.emit("pointsca",10);} break;
                }
                if(turn != 3)
                {
                    hh= 1;
                    mSocket.emit("nextgamec");

                }

                TextView field1 = (TextView) findViewById(R.id.redPlayerScore);
                field1.setText(rScore);
                for (int i =1;i<combination.values().size()+1;i++
                ) {
                    int id =0;
                    switch (i) {
                        case 1:
                            id = R.id.guessingCard29;
                            break;
                        case 2:
                            id = R.id.guessingCard30;
                            break;
                        case 3:
                            id = R.id.guessingCard31;
                            break;
                        case 4:
                            id = R.id.guessingCard32;
                            break;
                    }
                    ImageView im = (ImageView) findViewById(id);
                    int bas=0;
                    switch (i) {
                        case 1:
                            bas = combination.get(1);
                            break;
                        case 2:
                            bas = combination.get(2);
                            break;
                        case 3:
                            bas = combination.get(3);
                            break;
                        case 4:
                            bas = combination.get(4);
                            break;
                    }
                    switch (bas) {
                        case 1:
                            im.setBackground(s1.getBackground());break;
                        case 2:
                            im.setBackground(s2.getBackground());break;
                        case 3:
                            im.setBackground(s3.getBackground());break;
                        case 4:
                            im.setBackground(s4.getBackground());break;
                        case 5:
                            im.setBackground(s5.getBackground());break;
                        case 6:
                            im.setBackground(s6.getBackground());break;
                    }


                }
               if(p == 25 && hh == 0)
               {
                   List<Integer> coma = new ArrayList<>((Collection) guessedCombination.values());

                   mSocket.emit("enemyguess",coma.get(0),coma.get(1),coma.get(2),coma.get(3));
                   mSocket.emit("points");
                   mSocket.emit("nextgamec");
               }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timera.cancel();

                        if(round == 1  && turn != 3)
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
                            startActivity(intent);

                        }
                        if(round == 0  && turn !=3)
                        {
                            Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
                            startActivity(intent);

                        }
                        finish();
                    }
                }, 5000);

            }

        }
        if(guessedTrue == 0)
        {
            List<Integer> zz = new ArrayList<>((Collection) b); //final comb copy
            List<Integer> za = new ArrayList<>((Collection) a); //your guesses copy
            List<Integer> baa = new ArrayList<>((Collection) combination.values()); //final comb copy 2
            
            for (int t =0;t < zz.size() ;t++)
            {
                for (int j =0;j < za.size() ;j++)
                {
                    if(zz.get(t)== za.get(j) && zz.get(t)!=0 &&za.get(j) != 0)
                    {
                        int o = za.indexOf(za.get(j));
                        int v = zz.indexOf(zz.get(t));
                        za.set(o,0);
                        zz.set(v,0);

                       if(p < 25)
                       {
                           addToBall(p+v,2);
                          if(turn != 3)
                          {
                              mSocket.emit("lopta",p+v,2);
                          }
                       }
                        j=5;

                    }
                }
            }

            if(p == 21 && turn != 3)
            {
                List<Integer> bzz = new ArrayList<>((Collection) combination.values());
                mSocket.emit("passcom",bzz.get(0),bzz.get(1),bzz.get(2),bzz.get(3));


                mSocket.emit("turna");
                switcha = 1;
                return;
            }
            else{

                if(p == 25 && turn != 3)
                {
                    List<Integer> coma = new ArrayList<>((Collection) guessedCombination.values());
                    mSocket.emit("enemyguess",coma.get(0),coma.get(1),coma.get(2),coma.get(3));
                    mSocket.emit("nextgamec");
                    timera.cancel();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timera.cancel();

                            if(round == 1  && turn != 3)
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
                                startActivity(intent);

                            }
                            if(round == 0  && turn !=3)
                            {
                                Intent intent = new Intent(getApplicationContext(), CombinationsGame.class);
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
                                startActivity(intent);

                            }
                            finish();
                        }
                    }, 5000);



                }

        }
        }

    allSubmited = 0;
    posetion = 1;
    }
    public void addToBall(int pos,int color) {

        String a = "guessingBall";

        String temp = a + String.valueOf(pos);
        int id = 1;
        switch (temp) {
            case "guessingBall1":
                id = R.id.guessingBall1;
                break;
            case "guessingBall2":
                id = R.id.guessingBall2;
                break;
            case "guessingBall3":
                id = R.id.guessingBall3;
                break;
            case "guessingBall4":
                id = R.id.guessingBall4;
                break;
            case "guessingBall5":
                id = R.id.guessingBall5;
                break;
            case "guessingBall6":
                id = R.id.guessingBall6;
                break;
            case "guessingBall7":
                id = R.id.guessingBall7;
                break;
            case "guessingBall8":
                id = R.id.guessingBall8;
                break;
            case "guessingBall9":
                id = R.id.guessingBall9;
                break;
            case "guessingBall10":
                id = R.id.guessingBall10;
                break;
            case "guessingBall11":
                id = R.id.guessingBall11;
                break;
            case "guessingBall12":
                id = R.id.guessingBall12;
                break;
            case "guessingBall13":
                id = R.id.guessingBall13;
                break;
            case "guessingBall14":
                id = R.id.guessingBall14;
                break;
            case "guessingBall15":
                id = R.id.guessingBall15;
                break;
            case "guessingBall16":
                id = R.id.guessingBall16;
                break;
            case "guessingBall17":
                id = R.id.guessingBall17;
                break;
            case "guessingBall18":
                id = R.id.guessingBall18;
                break;
            case "guessingBall19":
                id = R.id.guessingBall19;
                break;
            case "guessingBall20":
                id = R.id.guessingBall20;
                break;
            case "guessingBall21":
                id = R.id.guessingBall21;
                break;
            case "guessingBall22":
                id = R.id.guessingBall22;
                break;
            case "guessingBall23":
                id = R.id.guessingBall23;
                break;
            case "guessingBall24":
                id = R.id.guessingBall24;
                break;

        }
        LinearLayout im = (LinearLayout) findViewById(id);
        int idd;
        int redColorValue = Color.RED;
        int u = Color.YELLOW;
        switch (color) {
            case 1:

               im.setBackgroundTintList(ColorStateList.valueOf(Color.RED));break;
            case 2:
                im.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));break;

        }

    }

    public void addToUI(int pos) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {


                String a = "guessingCard";
                String temp = a + String.valueOf(cardPosition);
                int id = 1;
                switch (temp) {
                    case "guessingCard1":
                        id = R.id.guessingCard1;
                        break;
                    case "guessingCard2":
                        id = R.id.guessingCard2;
                        break;
                    case "guessingCard3":
                        id = R.id.guessingCard3;
                        break;
                    case "guessingCard4":
                        id = R.id.guessingCard4;
                        break;
                    case "guessingCard5":
                        id = R.id.guessingCard5;
                        break;
                    case "guessingCard6":
                        id = R.id.guessingCard6;
                        break;
                    case "guessingCard7":
                        id = R.id.guessingCard7;
                        break;
                    case "guessingCard8":
                        id = R.id.guessingCard8;
                        break;
                    case "guessingCard9":
                        id = R.id.guessingCard9;
                        break;
                    case "guessingCard10":
                        id = R.id.guessingCard10;
                        break;
                    case "guessingCard11":
                        id = R.id.guessingCard11;
                        break;
                    case "guessingCard12":
                        id = R.id.guessingCard12;
                        break;
                    case "guessingCard13":
                        id = R.id.guessingCard13;
                        break;
                    case "guessingCard14":
                        id = R.id.guessingCard14;
                        break;
                    case "guessingCard15":
                        id = R.id.guessingCard15;
                        break;
                    case "guessingCard16":
                        id = R.id.guessingCard16;
                        break;
                    case "guessingCard17":
                        id = R.id.guessingCard17;
                        break;
                    case "guessingCard18":
                        id = R.id.guessingCard18;
                        break;
                    case "guessingCard19":
                        id = R.id.guessingCard19;
                        break;
                    case "guessingCard20":
                        id = R.id.guessingCard20;
                        break;
                    case "guessingCard21":
                        id = R.id.guessingCard21;
                        break;
                    case "guessingCard22":
                        id = R.id.guessingCard22;
                        break;
                    case "guessingCard23":
                        id = R.id.guessingCard23;
                        break;
                    case "guessingCard24":
                        if(turn == 3)
                        {
                            ab=1;
                        }
                        id = R.id.guessingCard24;
                        break;
                    case "guessingCard25":
                        id = R.id.guessingCard25;
                        break;
                    case "guessingCard26":
                        id = R.id.guessingCard26;
                        break;
                    case "guessingCard27":
                        id = R.id.guessingCard27;
                        break;
                    case "guessingCard28":
                        ab=1;
                        id = R.id.guessingCard28;
                        break;
                    case "guessingCard29":
                        id = R.id.guessingCard29;
                        break;
                    case "guessingCard30":
                        id = R.id.guessingCard30;
                        break;
                    case "guessingCard31":
                        id = R.id.guessingCard31;
                        break;
                    case "guessingCard32":

                        id = R.id.guessingCard32;
                        break;
                }
                ImageView im = (ImageView) findViewById(id);
                int idd;
                switch (pos) {
                    case 1:
                        im.setBackground(s1.getBackground());break;
                    case 2:
                        im.setBackground(s2.getBackground());break;
                    case 3:
                        im.setBackground(s3.getBackground());break;
                    case 4:
                        im.setBackground(s4.getBackground());break;
                    case 5:
                        im.setBackground(s5.getBackground());break;
                    case 6:
                        im.setBackground(s6.getBackground());break;
                }


            }
        });

        }
    public void addToUIHelp(int pos,int cardb) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                String a = "guessingCard";
                String temp = a + String.valueOf(cardb);
                int id = 1;
                switch (temp) {
                    case "guessingCard1":
                        id = R.id.guessingCard1;
                        break;
                    case "guessingCard2":
                        id = R.id.guessingCard2;
                        break;
                    case "guessingCard3":
                        id = R.id.guessingCard3;
                        break;
                    case "guessingCard4":
                        id = R.id.guessingCard4;
                        break;
                    case "guessingCard5":
                        id = R.id.guessingCard5;
                        break;
                    case "guessingCard6":
                        id = R.id.guessingCard6;
                        break;
                    case "guessingCard7":
                        id = R.id.guessingCard7;
                        break;
                    case "guessingCard8":
                        id = R.id.guessingCard8;
                        break;
                    case "guessingCard9":
                        id = R.id.guessingCard9;
                        break;
                    case "guessingCard10":
                        id = R.id.guessingCard10;
                        break;
                    case "guessingCard11":
                        id = R.id.guessingCard11;
                        break;
                    case "guessingCard12":
                        id = R.id.guessingCard12;
                        break;
                    case "guessingCard13":
                        id = R.id.guessingCard13;
                        break;
                    case "guessingCard14":
                        id = R.id.guessingCard14;
                        break;
                    case "guessingCard15":
                        id = R.id.guessingCard15;
                        break;
                    case "guessingCard16":
                        id = R.id.guessingCard16;
                        break;
                    case "guessingCard17":
                        id = R.id.guessingCard17;
                        break;
                    case "guessingCard18":
                        id = R.id.guessingCard18;
                        break;
                    case "guessingCard19":
                        id = R.id.guessingCard19;
                        break;
                    case "guessingCard20":
                        id = R.id.guessingCard20;
                        break;
                    case "guessingCard21":
                        id = R.id.guessingCard21;
                        break;
                    case "guessingCard22":
                        id = R.id.guessingCard22;
                        break;
                    case "guessingCard23":
                        id = R.id.guessingCard23;
                        break;
                    case "guessingCard24":
                        ab=1;
                        id = R.id.guessingCard24;
                        break;
                    case "guessingCard25":
                        id = R.id.guessingCard25;
                        break;
                    case "guessingCard26":
                        id = R.id.guessingCard26;
                        break;
                    case "guessingCard27":
                        id = R.id.guessingCard27;
                        break;
                    case "guessingCard28":
                        ab=1;
                        id = R.id.guessingCard28;
                        break;
                }
                ImageView im = (ImageView) findViewById(id);
                int idd;
                switch (pos) {
                    case 1:
                        im.setBackground(s1.getBackground());break;
                    case 2:
                        im.setBackground(s2.getBackground());break;
                    case 3:
                        im.setBackground(s3.getBackground());break;
                    case 4:
                        im.setBackground(s4.getBackground());break;
                    case 5:
                        im.setBackground(s5.getBackground());break;
                    case 6:
                        im.setBackground(s6.getBackground());break;
                }


            }
        });

    }


    }
