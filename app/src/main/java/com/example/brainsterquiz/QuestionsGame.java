package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class QuestionsGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_game);
        getSupportActionBar().hide();
    }
}