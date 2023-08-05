package com.example.brainsterquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class BrainsterHomeUnregistered extends AppCompatActivity {
    Dialog loginDialog;
    Dialog registerDialog;
    ImageView closeButton;
    RelativeLayout loginButton;
    TextView signInLink;
    TextView signUpLink;
    ImageView closeButtonReg;
    RelativeLayout signUpButton;
    TextView play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brainster_home_unregistered);
        getSupportActionBar().hide();
        loginDialog = new Dialog(this);
        registerDialog = new Dialog(this);
        this.play = (TextView) findViewById(R.id.startGameText);

    }

    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AssociationsGame.class);


            intent.putExtra("solo", 1);
            intent.putExtra("round", 0);
        intent.putExtra("rScore", "0");

        startActivity(intent);
    }
    public void loginAndRegisterDialog(View v) {

        setUIViews();
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog.dismiss();
                loginDialog.show();
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
            }
        });
        closeButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog.dismiss();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
        loginDialog.show();

    }
    private void setUIViews() {
        loginDialog.setContentView(R.layout.activity_login);
        registerDialog.setContentView(R.layout.activity_registration);
        closeButton = (ImageView) loginDialog.findViewById(R.id.closeButtonLogo);
        loginButton = (RelativeLayout) loginDialog.findViewById(R.id.loginButton);
        signInLink = (TextView) registerDialog.findViewById(R.id.signIn);
        signUpLink = (TextView) loginDialog.findViewById(R.id.signUp);
        closeButtonReg = (ImageView) registerDialog.findViewById(R.id.closeButton);
        signUpButton = (RelativeLayout) registerDialog.findViewById(R.id.signUpButton);
    }

    public void login(View v) {
        EditText usernameTxt = (EditText) loginDialog.findViewById(R.id.usernameTxt);
        EditText passwordTxt = (EditText) loginDialog.findViewById(R.id.passwordTxt);

        if ((usernameTxt.getText().toString().equals("admin")) && (passwordTxt.getText().toString().equals("admin"))){
            startActivity(new Intent(BrainsterHomeUnregistered.this, BrainsterHome.class));
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            usernameTxt.setText("");
            passwordTxt.setText("");
        }
    }
}