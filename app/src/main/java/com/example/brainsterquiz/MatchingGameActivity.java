package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MatchingGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);
        getSupportActionBar().hide();
    }
}