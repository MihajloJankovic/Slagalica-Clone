package com.example.brainsterquiz;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;


import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.text.DecimalFormat;
import java.util.HashMap;

import java.util.Map;
import java.util.Random;

import io.socket.client.Socket;

public class BrainsterHome extends AppCompatActivity {

    private ImageButton logoutButton;
    private Dialog userProfile;
    private Dialog playerStatistics;
    private LinearLayout notificationsList;
    private ImageButton myProfileButton;
    private RelativeLayout closeButtonProfile;
    private RelativeLayout statisticsButton;
    private RelativeLayout editProfileButtonClick;
    private RelativeLayout closeButtonNotifications;

    private LinearLayout messageIconLayout;
    private LinearLayout messageIcon;
    private RelativeLayout notificationTextLayout;
    private TextView notificationText;
    private RelativeLayout saveProfileButton;
    private RelativeLayout closeButtonStatistics;
    private ProgressBar numberGameProgressBar;
    private ProgressBar stepByStepGameProgressBar;
    private ProgressBar matchingGameProgressBar;
    private ProgressBar combinationsGameProgressBar;
    private ProgressBar questionsGameProgressBar;
    private ProgressBar associationsGameProgressBar;
    private RelativeLayout rankingButton;
    private TextView firstBoxPointer;
    private TextView secondBoxPointer;
    private TextView thirdBoxPointer;
    private RelativeLayout closeButtonRanking;
    private TextView fourthBoxPointer;
    private TextView fifthBoxPointer;
    private TextView sixthBoxPointer;

    private TextView peopleName;
    private TextView peopleName2;
    private TextView peopleName3;
    private TextView peopleName4;
    private TextView peopleName5;

    private TextView peopleStarsQuantity;
    private TextView peopleStarsQuantity2;
    private TextView peopleStarsQuantity3;
    private TextView peopleStarsQuantity4;
    private TextView peopleStarsQuantity5;

    private TextView peopleTokensQuantity;
    private TextView peopleTokensQuantity2;
    private TextView peopleTokensQuantity3;
    private TextView peopleTokensQuantity4;
    private TextView peopleTokensQuantity5;

    private LinearLayout peopleLayout;
    private LinearLayout peopleList;

    private RelativeLayout rankingWeeklyLayout;
    private RelativeLayout rankingMonthlyLayout;
    private TextView boxHeader;
    private TextView row1Label;
    private TextView row2Label;
    private TextView row3Label;
    private TextView row4Label;
    private TextView row5Label;
    private TextView row6Label;
    private TextView rankingWeekly;
    private TextView rankingMonthly;
    private TextView seasonLayout;
    private BrainsterHome bh = this;
    private TextView row7Label;
    private Dialog notifications;
    private Dialog ranking;
    private RelativeLayout notificationsBelly;
    private TextView row1Value;
    private TextView row2Value;
    private TextView row3Value;
    private TextView row4Value;
    private TextView row5Value;
    private TextView row6Value;
    private TextView row7Value;

    private ArrayList<LinearLayout> peopleListClone;

    private TextView peopleNumber;


    private String timeLeft;

    int totalnum =0;
    String gameid;

    private LinearLayout notificationLayout;
    private Socket mSocket;
    private TextView timeLeftTextView;
    private QueryDocumentSnapshot user;
    private String bname;
    private String rname;
    private LinearLayout peoplePointsLayout;
    private int turn;
    FirebaseFirestore db;
    private  ChatApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brainster_home);
        getSupportActionBar().hide();
        db = FirebaseFirestore.getInstance();
        userProfile = new Dialog(this);
        playerStatistics = new Dialog(this);
        notifications = new Dialog(this);
        ranking = new Dialog(this);

        peopleListClone = new ArrayList<>(10);

        ranking.setContentView(R.layout.ranking);

//       ----------------------------------------------------------------------------------------------------
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime endOfWeek = startTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
//        endOfWeek = endOfWeek.withHour(0).withMinute(0);
//        Duration duration = Duration.between(startTime, endOfWeek);
//        long totalMinutes = duration.toMinutes();
//        long days = totalMinutes / (60 * 24);
//        long hours = (totalMinutes % (60 * 24)) / 60;
//        long minutes = totalMinutes % 60;
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM. dd. yyyy HH:mm");
//
//        timeLeft = days + " d " + hours + " h " + minutes + " m";
//
//        timeLeftTextView = (TextView) ranking.findViewById(R.id.timeLeftTextView);
//        timeLeftTextView.setText("Ends in: " + timeLeft);
//        ----------------------------------------------------------------------------------------------------
//
//        TextView weekDatesTextView = ranking.findViewById(R.id.seasonDate);
//
//        LocalDate now = LocalDate.now();
//        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        LocalDate endOfTheWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
//
//        DateTimeFormatter formattered = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
//        String formattedDates = startOfWeek.format(formattered) + " - " + endOfTheWeek.format(formattered);
//
//        weekDatesTextView.setText(formattedDates);
//
//       ----------------------------------------------------------------------------------------------------
//
//        LocalDate start = LocalDate.now();
//        LocalDate startOfMonth = start.with(TemporalAdjusters.firstDayOfMonth());
//        LocalDate endOfMonth = start.with(TemporalAdjusters.lastDayOfMonth());
//
//        String formattedDates2 = startOfMonth.format(formattered) + " - " + endOfMonth.format(formatter);
//
//// Ispis datuma za mesecnu sezonu
//        System.out.println("Monthly Season: " + formattedDates);
//
//
//        LocalDateTime endOfMonthTime = LocalDateTime.of(endOfMonth, LocalTime.MAX);
//        Duration duration2 = Duration.between(startTime, endOfMonthTime);
//
//        long totalMinutes2 = duration.toMinutes();
//        long days2 = totalMinutes / (60 * 24);
//        long hours2 = (totalMinutes % (60 * 24)) / 60;
//        long minutes2 = totalMinutes % 60;
//
//// Formatiranje preostalog vremena
//        String timeLeft2 = days2 + " d " + hours2 + " h " + minutes2 + " m";



        Konekcija  app = (Konekcija) BrainsterHome.this.getApplication();
        this.mSocket = app.getSocket();
        this.user =app.getUser();

        this.rname = user.getString("name");

        mSocket.on("pleyer1",(a) -> {
            Tost();
        });
        mSocket.on("user1upisa",(a) -> {
            Map<String, Object> docData = new HashMap<>();
            docData.put("user1", user.getId());
            this.gameid =(String) a[0];
            db.collection("matches").document(String.valueOf(Double.valueOf((String) a[0]))).update(docData);
        });
        mSocket.on("pleyer2",(a) -> {
            try {
                Tost2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        mSocket.on("startMatch",(a) -> {

            StartMatch(a);
        });
        mSocket.on("podaci",(a) -> {


            Map<String,Object> mapa;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapa = mapper.readValue(a[0].toString(), Map.class);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            bname=mapa.get("a").toString();
            rname=mapa.get("b").toString();


        });
        setUIViews();
    }
    public void StartMatch( Object a){




        mSocket.emit("Imena");

        Intent intent = new Intent(getApplicationContext(), QuestionsGame.class);


        intent.putExtra("solo", 0);
        intent.putExtra("round", 0);
        intent.putExtra("bName", bname);
        intent.putExtra("rName", rname);
        intent.putExtra("rScore", "0");
        intent.putExtra("bScore", "0");
        intent.putExtra("gameid",gameid );
        intent.putExtra("turn", 1);

        finish();
        startActivity(intent);



    }
    public void Tost() {
        runOnUiThread(() -> Toast.makeText(bh, "weiting for other pleyer !", Toast.LENGTH_SHORT).show());
        this.turn = 1;
        mSocket.emit("Ime", rname);
    }

    public void Tost2() throws InterruptedException {
        runOnUiThread(() -> Toast.makeText(bh, "Match will start soon !", Toast.LENGTH_SHORT).show());
        this.turn = 2;


        double te = Math.random();


        this.gameid =String.valueOf(te);
        Map<String, Object> docData = new HashMap<>();
        docData.put("user2", user.getId());
        db.collection("matches").document(String.valueOf(te)).set(docData);
        mSocket.emit("user1upis",String.valueOf(te));
        mSocket.emit("Ime", rname);
        mSocket.emit("Imena");
        mSocket.emit("start");
    }
    public void onClickPlay(View v) {

        mSocket.connect();

    }

    public void myProfileDialogListeners(View view) {
        setUIViews();
        userProfile.show();
        TextView name =(TextView) userProfile.findViewById(R.id.usernameInfo);
        TextView email =(TextView) userProfile.findViewById(R.id.emailInfo);
        name.setText(user.getString("name"));
        email.setText(user.getString("email"));

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile.dismiss();
                Toast.makeText(BrainsterHome.this, "Saved changes!", Toast.LENGTH_SHORT).show();
                //save changes on profile...
            }
        });

        closeButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile.dismiss();
            }
        });

        editProfileButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //function for changing profile picture...
            }
        });
    }

    public void notificationsListeners(View view) {
        setUIViews();
        notifications.show();
        addNotification("notifikacija broj 1");
        addNotification("notifikacija broj 2");
        addNotification("notifikacija broj 3");

        closeButtonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications.dismiss();
            }
        });
    }

    public void addNotification(String text){

        Random random = new Random();
        LinearLayout newNotification = new LinearLayout(BrainsterHome.this);
        newNotification.setId(random.nextInt());
        newNotification.setLayoutParams(notificationLayout.getLayoutParams());
        newNotification.setBackground(notificationLayout.getBackground());
        newNotification.setOrientation(notificationLayout.getOrientation());

        LinearLayout newMessageIconLayout = new LinearLayout(BrainsterHome.this);
        newMessageIconLayout.setId(random.nextInt());
        newMessageIconLayout.setLayoutParams(messageIconLayout.getLayoutParams());
        newMessageIconLayout.setBackground(messageIconLayout.getBackground());
        newMessageIconLayout.setPadding(messageIconLayout.getPaddingLeft(), messageIconLayout.getPaddingTop(), messageIconLayout.getPaddingRight(), messageIconLayout.getPaddingBottom());

        LinearLayout newMessageIcon = new LinearLayout(BrainsterHome.this);
        newMessageIcon.setId(random.nextInt());
        newMessageIcon.setLayoutParams(messageIcon.getLayoutParams());
        newMessageIcon.setBackground(messageIcon.getBackground());
        newMessageIcon.setOrientation(messageIcon.getOrientation());

        RelativeLayout newNotificationTextLayout = new RelativeLayout(BrainsterHome.this);
        newNotificationTextLayout.setId(random.nextInt());
        newNotificationTextLayout.setLayoutParams(notificationTextLayout.getLayoutParams());
        newNotificationTextLayout.setGravity(notificationTextLayout.getGravity());

        TextView newNotificationText = new TextView(BrainsterHome.this);
        newNotificationText.setId(random.nextInt());
        newNotificationText.setLayoutParams(notificationText.getLayoutParams());
        newNotificationText.setText(text);
        newNotificationText.setGravity(notificationText.getGravity());
        newNotificationText.setTextColor(notificationText.getTextColors());
        newNotificationText.setTextSize(12);
        Typeface typeface = ResourcesCompat.getFont(BrainsterHome.this, R.font.quiz_font);
        newNotificationText.setTypeface(typeface);

        newNotificationTextLayout.addView(newNotificationText);
        newMessageIconLayout.addView(newMessageIcon);
        newNotification.addView(newMessageIconLayout);
        newNotification.addView(newNotificationTextLayout);

        notificationsList.addView(newNotification);

    }
    public void rankingBoxListeners(View view) {
        setUIViews();
        ranking.show();

        String colorCode = "#FF9800";
        int color = Color.parseColor(colorCode);
        rankingWeeklyLayout.setBackgroundColor(color);
        rankingWeekly.setTextColor(Color.BLACK);

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endOfWeek = startTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        endOfWeek = endOfWeek.withHour(0).withMinute(0);
        Duration duration = Duration.between(startTime, endOfWeek);
        long totalMinutes = duration.toMinutes();
        long days = totalMinutes / (60 * 24);
        long hours = (totalMinutes % (60 * 24)) / 60;
        long minutes = totalMinutes % 60;


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM. dd. yyyy HH:mm");

        timeLeft = days + " d " + hours + " h " + minutes + " m";

        timeLeftTextView = (TextView) ranking.findViewById(R.id.timeLeftTextView);
        timeLeftTextView.setText("Ends in: " + timeLeft);
//        ----------------------------------------------------------------------------------------------------

        TextView weekDatesTextView = ranking.findViewById(R.id.seasonDate);

        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfTheWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        DateTimeFormatter formattered = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
        String formattedDates = startOfWeek.format(formattered) + " - " + endOfTheWeek.format(formattered);

        weekDatesTextView.setText(formattedDates);

        closeButtonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ranking.dismiss();
            }
        });

        rankingWeeklyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = "#FF9800";
                int color = Color.parseColor(colorCode);

                peopleName.setText("Beska");
                peopleName2.setText("Milojica");
                peopleName3.setText("Pera");
                peopleName4.setText("Dusan");
                peopleName5.setText("Milan");

                peopleStarsQuantity.setText("45");
                peopleStarsQuantity2.setText("39");
                peopleStarsQuantity3.setText("34");
                peopleStarsQuantity4.setText("15");
                peopleStarsQuantity5.setText("9");

                peopleTokensQuantity.setText("7");
                peopleTokensQuantity2.setText("5");
                peopleTokensQuantity3.setText("3");
                peopleTokensQuantity4.setText("1");
                peopleTokensQuantity5.setText("1");

                rankingWeeklyLayout.setBackgroundColor(color);
                rankingWeekly.setTextColor(Color.BLACK);
                rankingMonthlyLayout.setBackgroundResource(R.drawable.operations_clicked);
                rankingMonthly.setTextColor(Color.WHITE);
                seasonLayout.setText("Weekly Season");
                LocalDateTime startTime = LocalDateTime.now();
                LocalDateTime endOfWeek = startTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                endOfWeek = endOfWeek.withHour(0).withMinute(0);
                Duration duration = Duration.between(startTime, endOfWeek);
                long totalMinutes = duration.toMinutes();
                long days = totalMinutes / (60 * 24);
                long hours = (totalMinutes % (60 * 24)) / 60;
                long minutes = totalMinutes % 60;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM. dd. yyyy HH:mm");

                timeLeft = days + " d " + hours + " h " + minutes + " m";

                timeLeftTextView = (TextView) ranking.findViewById(R.id.timeLeftTextView);
                timeLeftTextView.setText("Ends in: " + timeLeft);
//        ----------------------------------------------------------------------------------------------------

                TextView weekDatesTextView = ranking.findViewById(R.id.seasonDate);

                LocalDate now = LocalDate.now();
                LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfTheWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                DateTimeFormatter formattered = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
                String formattedDates = startOfWeek.format(formattered) + " - " + endOfTheWeek.format(formattered);

                weekDatesTextView.setText(formattedDates);

            }
        });

        rankingMonthlyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = "#FF9800";
                int color = Color.parseColor(colorCode);

                peopleName.setText("Pera");
                peopleName2.setText("Beska");
                peopleName3.setText("Milojica");
                peopleName4.setText("Dusan");
                peopleName5.setText("Milan");

                peopleStarsQuantity.setText("227");
                peopleStarsQuantity2.setText("146");
                peopleStarsQuantity3.setText("139");
                peopleStarsQuantity4.setText("67");
                peopleStarsQuantity5.setText("13");

                peopleTokensQuantity.setText("17");
                peopleTokensQuantity2.setText("13");
                peopleTokensQuantity3.setText("9");
                peopleTokensQuantity4.setText("4");
                peopleTokensQuantity5.setText("4");

                rankingMonthlyLayout.setBackgroundColor(color);
                rankingMonthly.setTextColor(Color.BLACK);
                rankingWeeklyLayout.setBackgroundResource(R.drawable.operations_clicked);
                rankingWeekly.setTextColor(Color.WHITE);
                seasonLayout.setText("Monthly Season");
                LocalDateTime startTime = LocalDateTime.now();
                LocalDateTime startOfMonth = startTime.with(TemporalAdjusters.firstDayOfMonth());
                LocalDateTime endOfMonth = startTime.with(TemporalAdjusters.lastDayOfMonth());
                endOfMonth = endOfMonth.withHour(0).withMinute(0);
                Duration duration = Duration.between(startTime, endOfMonth);
                long totalMinutes = duration.toMinutes();
                long days = totalMinutes / (60 * 24);
                long hours = (totalMinutes % (60 * 24)) / 60;
                long minutes = totalMinutes % 60;


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM. dd. yyyy HH:mm");

                timeLeft = days + " d " + hours + " h " + minutes + " m";

                timeLeftTextView = (TextView) ranking.findViewById(R.id.timeLeftTextView);
                timeLeftTextView.setText("Ends in: " + timeLeft);
//        ----------------------------------------------------------------------------------------------------

                TextView monthDatesTextView = ranking.findViewById(R.id.seasonDate);


                DateTimeFormatter formattered = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
                String formattedDates = startOfMonth.format(formattered) + " - " + endOfMonth.format(formattered);
                monthDatesTextView.setText(formattedDates);
            }
        });


    }
    public void statisticsBoxListeners(View view) {
        setUIViews();

        playerStatistics.show();
        Query query =  db.collection("matches").where(Filter.or(
                Filter.equalTo("user2",user.getId().toString()),
                Filter.equalTo("user1",user.getId().toString())));



        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {

                            TextView total = playerStatistics.findViewById(R.id.totalQuantity);
                            total.setText(String.valueOf(task.getResult().size()));
                            BrainsterHome.this.totalnum = Integer.valueOf(task.getResult().size());




                            // dependable on last action
                            Query query1 =  db.collection("matches").whereEqualTo("winner",user.getId().toString());
                            query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document != null) {

                                                TextView totalw = playerStatistics.findViewById(R.id.winsQuantity);
                                                TextView totall = playerStatistics.findViewById(R.id.losesQuantity);
                                                totalw.setText(String.valueOf(task.getResult().size()));
                                                totall.setText(String.valueOf(totalnum-Integer.valueOf(task.getResult().size())));
                                                TextView winsp = playerStatistics.findViewById(R.id.winsPercentage);

                                                Float pera1 =Float.valueOf(Float.valueOf(task.getResult().size())/totalnum);
                                                double example = Math.round((pera1) * 10.00) / 10.00;
                                                winsp.setText(String.valueOf(example*100)+"%");
                                            }


                                        }
                                        if(task.getResult().size() == 0){
                                            TextView totall = playerStatistics.findViewById(R.id.losesQuantity);
                                            totall.setText(String.valueOf(totalnum));
                                        }
                                    }


                                }

                            });
                        }

                    }
                }
            }

        });



        closeButtonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerStatistics.dismiss();
            }
        });

        numberGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstBoxPointer.setVisibility(View.VISIBLE);
                secondBoxPointer.setVisibility(View.INVISIBLE);
                thirdBoxPointer.setVisibility(View.INVISIBLE);
                fourthBoxPointer.setVisibility(View.INVISIBLE);
                fifthBoxPointer.setVisibility(View.INVISIBLE);
                sixthBoxPointer.setVisibility(View.INVISIBLE);
                boxHeader.setText("Number game - distance");
                row1Label.setText("correct number: ");
                row1Value.setText("73%");
                row2Label.setText("1-4: ");
                row2Value.setText("10%");
                row3Label.setText("5-9: ");
                row3Value.setText("2%");
                row4Label.setText("10-19: ");
                row4Value.setText("3%");
                row5Label.setText("20-49: ");
                row5Value.setText("2%");
                row6Label.setText("50-99: ");
                row6Value.setText("2%");
                row7Label.setText("100 and more: ");
                row7Value.setText("9%");
            }
        });

        stepByStepGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstBoxPointer.setVisibility(View.INVISIBLE);
                secondBoxPointer.setVisibility(View.VISIBLE);
                thirdBoxPointer.setVisibility(View.INVISIBLE);
                fourthBoxPointer.setVisibility(View.INVISIBLE);
                fifthBoxPointer.setVisibility(View.INVISIBLE);
                sixthBoxPointer.setVisibility(View.INVISIBLE);

                boxHeader.setText("Word guessing game - steps");
                row1Label.setText("1 step: ");
                row1Value.setText("73%");
                row2Label.setText("2 steps: ");
                row2Value.setText("10%");
                row3Label.setText("3 steps: ");
                row3Value.setText("2%");
                row4Label.setText("4 steps: ");
                row4Value.setText("3%");
                row5Label.setText("5 steps: ");
                row5Value.setText("2%");
                row6Label.setText("6 to 7 steps: ");
                row6Value.setText("2%");
                row7Label.setText("Not guessed word: ");
                row7Value.setText("9%");
            }
        });



        matchingGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstBoxPointer.setVisibility(View.INVISIBLE);
                secondBoxPointer.setVisibility(View.INVISIBLE);
                thirdBoxPointer.setVisibility(View.VISIBLE);
                fourthBoxPointer.setVisibility(View.INVISIBLE);
                fifthBoxPointer.setVisibility(View.INVISIBLE);
                sixthBoxPointer.setVisibility(View.INVISIBLE);

                boxHeader.setText("Matching game - exactness");
                row1Label.setText("matched pairs: ");
                row1Value.setText("73%");
                row2Label.setText("not matched pairs: ");
                row2Value.setText("27%");
                row3Label.setText("");
                row3Value.setText("");
                row4Label.setText("");
                row4Value.setText("");
                row5Label.setText("");
                row5Value.setText("");
                row6Label.setText("");
                row6Value.setText("");
                row7Label.setText("");
                row7Value.setText("");
            }
        });

        combinationsGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstBoxPointer.setVisibility(View.INVISIBLE);
                secondBoxPointer.setVisibility(View.INVISIBLE);
                thirdBoxPointer.setVisibility(View.INVISIBLE);
                fourthBoxPointer.setVisibility(View.VISIBLE);
                fifthBoxPointer.setVisibility(View.INVISIBLE);
                sixthBoxPointer.setVisibility(View.INVISIBLE);

                boxHeader.setText("Combinations game - attempts");
                row1Label.setText("4 and less: ");
                row1Value.setText("20%");
                row2Label.setText("5 attempts: ");
                row2Value.setText("33%");
                row3Label.setText("6 attempts: ");
                row3Value.setText("21%");
                row4Label.setText("Not guessed: ");
                row4Value.setText("25%");
                row5Label.setText("");
                row5Value.setText("");
                row6Label.setText("");
                row6Value.setText("");
                row7Label.setText("");
                row7Value.setText("");
            }
        });

        questionsGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstBoxPointer.setVisibility(View.INVISIBLE);
                secondBoxPointer.setVisibility(View.INVISIBLE);
                thirdBoxPointer.setVisibility(View.INVISIBLE);
                fourthBoxPointer.setVisibility(View.INVISIBLE);
                fifthBoxPointer.setVisibility(View.VISIBLE);
                sixthBoxPointer.setVisibility(View.INVISIBLE);

                boxHeader.setText("Questions game - answer exactness");
                row1Label.setText("correct: ");
                row1Value.setText("36%");
                row2Label.setText("not answered: ");
                row2Value.setText("31%");
                row3Label.setText("wrong: ");
                row3Value.setText("33%");
                row4Label.setText("");
                row4Value.setText("");
                row5Label.setText("");
                row5Value.setText("");
                row6Label.setText("");
                row6Value.setText("");
                row7Label.setText("");
                row7Value.setText("");
            }
        });


        associationsGameProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstBoxPointer.setVisibility(View.INVISIBLE);
                secondBoxPointer.setVisibility(View.INVISIBLE);
                thirdBoxPointer.setVisibility(View.INVISIBLE);
                fourthBoxPointer.setVisibility(View.INVISIBLE);
                fifthBoxPointer.setVisibility(View.INVISIBLE);
                sixthBoxPointer.setVisibility(View.VISIBLE);

                boxHeader.setText("Associations game - columns quantity");
                row1Label.setText("whole association: ");
                row1Value.setText("19%");
                row2Label.setText("4 columns: ");
                row2Value.setText("19%");
                row3Label.setText("3 columns: ");
                row3Value.setText("11%");
                row4Label.setText("2 columns: ");
                row4Value.setText("9%");
                row5Label.setText("1 column: ");
                row5Value.setText("17%");
                row6Label.setText("0 columns: ");
                row6Value.setText("25%");
                row7Label.setText("");
                row7Value.setText("");
            }
        });

    }


    public void placeBasedOnStars(){
        setUIViews();
        ArrayList<String> peopleStarsString = new ArrayList<>(10);
        ArrayList<Integer> peopleStars = new ArrayList<>(10);

//        LinearLayout newPeopleLayout = new LinearLayout(BrainsterHome.this);
//        newPeopleLayout.setLayoutParams(peopleLayout.getLayoutParams());
//        newPeopleLayout.setBackground(peopleLayout.getBackground());
//        newPeopleLayout.setOrientation(peopleLayout.getOrientation());
//        peopleList.addView(newPeopleLayout);

        for (int i = 0; i <= peopleList.getChildCount() - 1; i++) {
            LinearLayout newPeopleLayout = (LinearLayout) peopleList.getChildAt(i);
            LinearLayout newPeoplePointsLayout = (LinearLayout) newPeopleLayout.getChildAt(2);
            LinearLayout newPeopleStarsLayout = (LinearLayout) newPeoplePointsLayout.getChildAt(0);
            LinearLayout newPeopleStarsQuantityLayout = (LinearLayout) newPeopleStarsLayout.getChildAt(1);
            TextView starsQuantity = (TextView) newPeopleStarsQuantityLayout.getChildAt(0);

            String starsQuantityString = starsQuantity.getText().toString();
            peopleStarsString.add(starsQuantityString);

        }

        for(String ps : peopleStarsString) {
            try{
                int stars = Integer.parseInt(ps);
                peopleStars.add(stars);
            } catch (NumberFormatException e){
                Log.e("Parsing Error", "Error parsing text to int: " + e.getMessage());
            }
        }
        peopleStars.sort(Comparator.reverseOrder());
        int p = 0;
        for (int i : peopleStars) {
            p += 1;
            LinearLayout newPeopleLayout = (LinearLayout) peopleList.getChildAt(p-1);
            TextView newPeopleNumber = (TextView) newPeopleLayout.getChildAt(0);
            LinearLayout newPeoplePointsLayout = (LinearLayout) newPeopleLayout.getChildAt(2);
            LinearLayout newPeopleStarsLayout = (LinearLayout) newPeoplePointsLayout.getChildAt(0);
            LinearLayout newPeopleStarsQuantityLayout = (LinearLayout) newPeopleStarsLayout.getChildAt(1);
            TextView newStarsQuantity = (TextView) newPeopleStarsQuantityLayout.getChildAt(0);
            newPeopleNumber.setText(p + ".");
            newStarsQuantity.setText(i);
            peopleStars.remove(0);


        }

    }




    public void ladderMovingFunctionWeekly() {
        setUIViews();
        placeBasedOnStars();

        if(peopleNumber.getText() == "1."){
            peopleTokensQuantity.setText("7");
        }

        else if(peopleNumber.getText() == "2."){
            peopleTokensQuantity.setText("5");
        }

        else if(peopleNumber.getText() == "3.") {
            peopleTokensQuantity.setText("3");
        }
        else if((Integer.valueOf(peopleNumber.getText().charAt(0)) >= 4 && Integer.valueOf(peopleNumber.getText().charAt(0)) <= 9) || peopleNumber.getText() == "10."){
            peopleTokensQuantity.setText("1");
        }
        else {
            peopleTokensQuantity.setText("0");
        }
    }

    public void ladderMovingFunctionMonthly() {
        setUIViews();

        if(peopleNumber.getText() == "1."){
            peopleTokensQuantity.setText("17");
        }

        else if(peopleNumber.getText() == "2."){
            peopleTokensQuantity.setText("13");
        }

        else if(peopleNumber.getText() == "3.") {
            peopleTokensQuantity.setText("9");
        }
        else if((Integer.valueOf(peopleNumber.getText().charAt(0)) >= 4 && Integer.valueOf(peopleNumber.getText().charAt(0)) <= 9) || peopleNumber.getText() == "10."){
            peopleTokensQuantity.setText("4");
        }
        else {
            peopleTokensQuantity.setText("0");
        }
    }

    public void setUIViews(){
        logoutButton = (ImageButton) this.findViewById(R.id.logoutButton);
        userProfile.setContentView(R.layout.my_profile_layout);
        playerStatistics.setContentView(R.layout.statistics_layout);
        notifications.setContentView(R.layout.notifications);
        closeButtonProfile = (RelativeLayout) userProfile.findViewById(R.id.closeButton);
        closeButtonNotifications = (RelativeLayout) notifications.findViewById(R.id.closeButton);
        closeButtonStatistics = (RelativeLayout) playerStatistics.findViewById(R.id.closeButton);
        closeButtonRanking = (RelativeLayout) ranking.findViewById(R.id.closeButton);
        myProfileButton = (ImageButton) this.findViewById(R.id.myProfileButton);
        statisticsButton = (RelativeLayout) this.findViewById(R.id.statistics);
        notificationsBelly = (RelativeLayout) this.findViewById(R.id.notification_belly);
        editProfileButtonClick = (RelativeLayout) userProfile.findViewById(R.id.editProfilePictureLayout);
        saveProfileButton = (RelativeLayout) userProfile.findViewById(R.id.saveProfileButton);
        notificationsList = (LinearLayout) notifications.findViewById(R.id.notificationsList);
        numberGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_number_game);
        stepByStepGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_step_by_step_game);
        matchingGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_matching_game);
        combinationsGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_combinations_game);
        questionsGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_questions_game);
        associationsGameProgressBar = (ProgressBar) playerStatistics.findViewById(R.id.progress_bar_associations_game);
        notificationLayout = (LinearLayout) notifications.findViewById(R.id.notificationLayout);
        firstBoxPointer = (TextView) playerStatistics.findViewById(R.id.firstBoxPointer);
        secondBoxPointer = (TextView) playerStatistics.findViewById(R.id.secondBoxPointer);
        thirdBoxPointer = (TextView) playerStatistics.findViewById(R.id.thirdBoxPointer);
        fourthBoxPointer = (TextView) playerStatistics.findViewById(R.id.fourthBoxPointer);
        fifthBoxPointer = (TextView) playerStatistics.findViewById(R.id.fifthBoxPointer);
        sixthBoxPointer = (TextView) playerStatistics.findViewById(R.id.sixthBoxPointer);
        rankingButton = (RelativeLayout) this.findViewById(R.id.rang_list);
        rankingWeeklyLayout = (RelativeLayout) ranking.findViewById(R.id.rankingWeeklyLayout);
        rankingMonthlyLayout = (RelativeLayout) ranking.findViewById(R.id.rankingMonthlyLayout);
        rankingWeekly = (TextView) ranking.findViewById(R.id.weeklyRank);
        rankingMonthly = (TextView) ranking.findViewById(R.id.monthlyRank);
        seasonLayout = (TextView) ranking.findViewById(R.id.seasonLayout);
        peopleList = (LinearLayout) ranking.findViewById(R.id.peopleList);
        peopleLayout = (LinearLayout) ranking.findViewById(R.id.peopleLayout);
        peoplePointsLayout = (LinearLayout) ranking.findViewById(R.id.peoplePointsLayout);
        peopleNumber = (TextView) ranking.findViewById(R.id.peopleNumber);
        peopleName = (TextView) ranking.findViewById(R.id.peopleName);
        peopleStarsQuantity = (TextView) ranking.findViewById(R.id.peopleStarsQuantity);
        peopleStarsQuantity2 = (TextView) ranking.findViewById(R.id.peopleStarsQuantity2);
        peopleStarsQuantity3 = (TextView) ranking.findViewById(R.id.peopleStarsQuantity3);
        peopleStarsQuantity4 = (TextView) ranking.findViewById(R.id.peopleStarsQuantity4);
        peopleStarsQuantity5 = (TextView) ranking.findViewById(R.id.peopleStarsQuantity5);
        peopleTokensQuantity = (TextView) ranking.findViewById(R.id.peopleTokensQuantity);
        peopleTokensQuantity2 = (TextView) ranking.findViewById(R.id.peopleTokensQuantity2);
        peopleTokensQuantity3 = (TextView) ranking.findViewById(R.id.peopleTokensQuantity3);
        peopleTokensQuantity4 = (TextView) ranking.findViewById(R.id.peopleTokensQuantity4);
        peopleTokensQuantity5 = (TextView) ranking.findViewById(R.id.peopleTokensQuantity5);
        peopleName2 = (TextView) ranking.findViewById(R.id.peopleName2);
        peopleName3 = (TextView) ranking.findViewById(R.id.peopleName3);
        peopleName4 = (TextView) ranking.findViewById(R.id.peopleName4);
        peopleName5 = (TextView) ranking.findViewById(R.id.peopleName5);
        messageIconLayout = (LinearLayout) notifications.findViewById(R.id.messageIconLayout);
        messageIcon = (LinearLayout) notifications.findViewById(R.id.messageIcon);
        notificationTextLayout = (RelativeLayout) notifications.findViewById(R.id.notificationTextLayout);
        notificationText = (TextView) notifications.findViewById(R.id.notificationText);

        boxHeader = (TextView) playerStatistics.findViewById(R.id.boxHeader);

        row1Label = (TextView) playerStatistics.findViewById(R.id.row1Label);
        row1Value = (TextView) playerStatistics.findViewById(R.id.row1Value);
        row2Label = (TextView) playerStatistics.findViewById(R.id.row2Label);
        row2Value = (TextView) playerStatistics.findViewById(R.id.row2Value);
        row3Label = (TextView) playerStatistics.findViewById(R.id.row3Label);
        row3Value = (TextView) playerStatistics.findViewById(R.id.row3Value);
        row4Label = (TextView) playerStatistics.findViewById(R.id.row4Label);
        row4Value = (TextView) playerStatistics.findViewById(R.id.row4Value);
        row5Label = (TextView) playerStatistics.findViewById(R.id.row5Label);
        row5Value = (TextView) playerStatistics.findViewById(R.id.row5Value);
        row6Label = (TextView) playerStatistics.findViewById(R.id.row6Label);
        row6Value = (TextView) playerStatistics.findViewById(R.id.row6Value);
        row7Label = (TextView) playerStatistics.findViewById(R.id.row7Label);
        row7Value = (TextView) playerStatistics.findViewById(R.id.row7Value);

    }

    public void logout(View view) {
        // need alert dialog...
        startActivity(new Intent(BrainsterHome.this, BrainsterHomeUnregistered.class));
    }
}