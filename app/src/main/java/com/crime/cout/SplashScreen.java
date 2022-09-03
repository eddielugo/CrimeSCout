package com.crime.cout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.crime.cout.Account.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initAuthentication();
        stayForWhile();
    }
    private void stayForWhile() {

        Thread stayForWhileThread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    if(isLogin()){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    }else {

                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        stayForWhileThread.start();
    }

    private boolean isLogin() {
        return firebaseUser != null;

    }
    private void initAuthentication() {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    }
}