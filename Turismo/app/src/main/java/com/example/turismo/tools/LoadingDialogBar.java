package com.example.turismo.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.turismo.R;

public class LoadingDialogBar {

    Context context;
    Dialog dialog;

    public LoadingDialogBar(Context context) {
        this.context = context;
    }

    public void showDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView progressText = dialog.findViewById(R.id.progress_text);
        progressText.setText(title);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
