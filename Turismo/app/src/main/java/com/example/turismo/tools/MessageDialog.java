package com.example.turismo.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.example.turismo.R;

public class MessageDialog {

    Context context;
    Dialog dialog;

    public MessageDialog(Context context) {
        this.context = context;
    }

    public void showDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView progressText = dialog.findViewById(R.id.dialog_text);
        progressText.setText(title);
        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
