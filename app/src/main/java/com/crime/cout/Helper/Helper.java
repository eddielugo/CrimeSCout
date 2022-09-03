package com.crime.cout.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;


import com.crime.cout.R;



public class Helper {
    Context context;
    public Helper(Context context) {
        this.context = context;
    }
    public Dialog openNetLoaderDialog() {
       Dialog dialogP=new Dialog(context);
        dialogP.setContentView(R.layout.dialog_loading);
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.setCancelable(false);
        dialogP.show();
        return dialogP;
    }
    public void messageDialog(String title,String message) {

        Dialog dialogP=new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogP.setContentView(R.layout.toast_dialog);
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.show();
        TextView textViewTopMessage,textViewErrorMessage,buttonOkay;
        textViewTopMessage=dialogP.findViewById(R.id.textViewTopMessage);
        textViewErrorMessage=dialogP.findViewById(R.id.textViewErrorMessage);
        buttonOkay=dialogP.findViewById(R.id.buttonOkay);


        textViewTopMessage.setText(title);
        textViewErrorMessage.setText(message);
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });
        dialogP.setCanceledOnTouchOutside(true);

    }



}
