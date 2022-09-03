package com.crime.cout.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crime.cout.Helper.Helper;
import com.crime.cout.MainActivity;
import com.crime.cout.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {


    EditText editTextEmail,editTextPassword;
    Button buttonLogin;
    Helper helper;
    TextView textViewForgotPassword;
    ImageView backArrow;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helper=new Helper(this);
        initUI();
        initAuthentication();
    }

    private void initAuthentication() {
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();
    }

    private void initUI() {
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        backArrow=findViewById(R.id.backArrow);
        textViewForgotPassword=findViewById(R.id.textViewForgotPassword);
        textViewLogin=findViewById(R.id.textViewLogin);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();


        if(email.equals("")){
            helper.messageDialog("Error","Email Required");
        }else if(password.equals("")){
            helper.messageDialog("Error","Password Required");
        }else if(!email.contains("@")){
            helper.messageDialog("Error","Invalid Email");
        }else {
            Dialog dialogProgress=helper.openNetLoaderDialog();
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    dialogProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Login Successful ", Toast.LENGTH_SHORT).show();
                    startNewTask();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogProgress.dismiss();
                 helper.messageDialog("Error",e.getMessage()); // show error  message
                }
            });

        }
    }
    private void startNewTask() {
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}