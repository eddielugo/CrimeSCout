package com.crime.cout.Account;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crime.cout.Helper.Helper;
import com.crime.cout.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ResetPasswordActivity extends AppCompatActivity {
     //UI widgets variables
     ImageView backArrow;
     EditText edEmail;
     Button buttonGetResetLink;
     Helper helper; // helper class

     //Firebase authentication objects
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        helper=new Helper(this);// initialize helper class
        initAuth(); // call auth function
        initUI();// call connect UI function
    }

    // initialize firebase authentication object
    private void initAuth() {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    private void initUI() {

        // Connect UI
        backArrow=findViewById(R.id.backArrow);
        edEmail=findViewById(R.id.edEmail);
        buttonGetResetLink=findViewById(R.id.buttonGetResetLink);

        // when user click on back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // finish this activity
            }
        });

        // when user click on rest  link
        buttonGetResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(); // call restPassword function
            }
        });

    }

    private void resetPassword() {
        String email=edEmail.getText().toString();// get email from user

        // validate email
        if(email.equals("")){
            helper.messageDialog(getResources().getString(R.string.error),"Email required");
        }else  if(!email.contains("@")){
            helper.messageDialog(getResources().getString(R.string.error),"Invalid email");
        }else {

            // open progress dialog
            Dialog dialogProgress=helper.openNetLoaderDialog();

            // sent rest link to user user email
          auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.resetLink), Toast.LENGTH_LONG).show();
                   dialogProgress.dismiss(); // finish dialog
                   finish();// finish this activity
              }
          }).addOnFailureListener(new OnFailureListener() {// handle Failure
              @Override
              public void onFailure(@NonNull  Exception e) {
                dialogProgress.dismiss();// finish dialog
                  helper.messageDialog("Error",e.getMessage()); // display error
              }
          });
        }



    }
}