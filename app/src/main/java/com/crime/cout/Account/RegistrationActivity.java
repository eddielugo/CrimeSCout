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
import com.crime.cout.Models.UserModel;
import com.crime.cout.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPassword;
    Button buttonRegister;
    ImageView backArrow;
    Helper helper;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TextView textViewRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        helper=new Helper(this);
        initUI();
        initAuthentication();
    }

    private void initAuthentication() {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users");
    }

    private void initUI() {
        editTextName=findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextConfirmPassword=findViewById(R.id.editTextConfirmPassword);
        buttonRegister=findViewById(R.id.buttonRegister);
        backArrow=findViewById(R.id.backArrow);
        textViewRegistration=findViewById(R.id.textViewRegistration);
        textViewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // finish this activity
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegistration();
            }
        });

    }

    private void doRegistration() {
        UserModel userModel=new UserModel();
        userModel.setUserEmail(editTextEmail.getText().toString());
        userModel.setUserName(editTextName.getText().toString());
        String password=editTextPassword.getText().toString();
        String confirmPassword=editTextConfirmPassword.getText().toString();

        if(userModel.getUserEmail().equals("")){
            helper.messageDialog("Error","Email required");
        }else if(userModel.getUserName().equals("")){
            helper.messageDialog("Error","Name required");
        }else if(!userModel.getUserEmail().contains("@")){
            helper.messageDialog("Error","Invalid Email");
        }else if(password.equals("")){
            helper.messageDialog("Error","Password required");
        }else if(!password.equals(confirmPassword)) {
            helper.messageDialog("Error","Password and confirm password not matched");
        }else {

            Dialog dialogProgress=helper.openNetLoaderDialog();
            firebaseAuth.createUserWithEmailAndPassword(userModel.getUserEmail(),password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) { // if success

                    Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                    firebaseUser=firebaseAuth.getCurrentUser();
                    reference.child(firebaseUser.getUid()).child("profile").setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialogProgress.dismiss();
                            startNewTask();
                        }
                    }).addOnFailureListener(new OnFailureListener() { // handle if data not uploaded
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            helper.messageDialog("Error",e.getMessage()); // display message using helper
                            dialogProgress.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogProgress.dismiss();
                 helper.messageDialog("Error",e.getMessage());
                }
            });
        }
    }
    private void startNewTask() {
        Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}