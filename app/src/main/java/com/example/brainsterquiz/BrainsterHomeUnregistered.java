package com.example.brainsterquiz;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

public class BrainsterHomeUnregistered extends AppCompatActivity {
    Dialog loginDialog;
    Dialog registerDialog;
    ImageView closeButton;
    RelativeLayout loginButton;
    TextView signInLink;
    TextView signUpLink;
    ImageView closeButtonReg;
    RelativeLayout signUpButton;
    FirebaseFirestore db;
    private  ChatApplication app;
    private Socket mSocket;
    TextView play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

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

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog.show();
                loginDialog.dismiss();
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

        String finalIdd = "Konacno";
        Query query = db.collection("users").whereEqualTo("name", usernameTxt.getText().toString()).whereEqualTo("password", passwordTxt.getText().toString());


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {
                            app =new ChatApplication();
                            mSocket = app.getSocket();
                            Konekcija appb = (Konekcija)BrainsterHomeUnregistered.this.getApplication();
                            Socket socket = appb.setSocket(mSocket);
                            appb.setUser(document);
                            Intent intent = new Intent(BrainsterHomeUnregistered.this, BrainsterHome.class);
                            startActivity(intent);
                            Toast.makeText(BrainsterHomeUnregistered.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(BrainsterHomeUnregistered.this, "Login failed!", Toast.LENGTH_SHORT).show();
                            usernameTxt.setText("");
                            passwordTxt.setText("");
                        }
                    }
                } else{
                    Toast.makeText(BrainsterHomeUnregistered.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    usernameTxt.setText("");
                    passwordTxt.setText("");
                }
            }

        });


           Toast.makeText(BrainsterHomeUnregistered.this, "Login failed!", Toast.LENGTH_SHORT).show();
           usernameTxt.setText("");
           passwordTxt.setText("");

    }
    public void register(View v) {
        EditText usernameTxt = (EditText) registerDialog.findViewById(R.id.usernameTxt);
        EditText passwordTxt = (EditText) registerDialog.findViewById(R.id.passwordTxt);
        EditText passwordRepeatTxt = (EditText) registerDialog.findViewById(R.id.confirmPasswordTxt);
        EditText emailTxt = (EditText) registerDialog.findViewById(R.id.emailTxt);

        if(passwordTxt.getText().equals(passwordRepeatTxt.getText()))
        {
            if(emailTxt.getText().toString().length() > 3)
            {
                if(usernameTxt.getText().toString().length() > 3)
                {
                    String finalIdd = "Konacno";
                    Query query = db.collection("users").whereEqualTo("name", usernameTxt.getText().toString()).whereEqualTo("password", passwordTxt.getText().toString());


                    Map<String, Object> docData = new HashMap<>();
                    docData.put("name", usernameTxt.getText().toString());
                    docData.put("password",passwordTxt.getText().toString() );
                    docData.put("email", emailTxt.getText().toString());
                    db.collection("users").document(String.valueOf(Math.random())).set(docData);

                }
            }
        }


    }

}