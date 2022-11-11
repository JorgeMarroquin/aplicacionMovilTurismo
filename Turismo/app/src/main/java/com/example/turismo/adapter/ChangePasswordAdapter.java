package com.example.turismo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.turismo.R;
import com.example.turismo.api.ApiClient;
import com.example.turismo.interfaces.LugaresAPI;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Message;
import com.example.turismo.tools.AESCrypt;
import com.example.turismo.tools.MessageDialog;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordAdapter {

    Context context;
    Dialog dialog;
    private UsersAPI mApi;
    private String password;
    private int usuarioId;

    public ChangePasswordAdapter(Context context, String password, int usuarioId) {
        this.context = context;
        this.password = password;
        this.usuarioId = usuarioId;
        this.mApi = ApiClient.getInstance().create(UsersAPI.class);
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password);
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout textOld =  dialog.findViewById(R.id.password_actual);
        TextInputLayout textNew = dialog.findViewById(R.id.password_new);
        TextInputLayout textConfirm = dialog.findViewById(R.id.password_confirmation);
        Button closeButton = dialog.findViewById(R.id.password_button_cancel);
        Button confirmButton = dialog.findViewById(R.id.password_button_confirm);

        closeButton.setOnClickListener(view -> hideDialog());
        confirmButton.setOnClickListener(view -> {
            textOld.setError("");
            textNew.setError("");
            textConfirm.setError("");
            String oldPassword  = textOld.getEditText().getText().toString();
            String newPassword = textNew.getEditText().getText().toString();
            String confirmPassword = textConfirm.getEditText().getText().toString();
            if(oldPassword.matches("")){
                textOld.setError("Debe llenar este campo");
                return;
            }
            if(newPassword.matches("")){
                textNew.setError("Debe llenar este campo");
                return;
            }
            if(confirmPassword.matches("")){
                textConfirm.setError("Debe llenar este campo");
                return;
            }
            if(!newPassword.matches(confirmPassword)){
                textConfirm.setError("Las contraseñas no coinciden");
                return;
            }
            String hashOldPassword = null;
            try {
                hashOldPassword = AESCrypt.encrypt(oldPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!hashOldPassword.matches(password)){
                textOld.setError("La contraseña actual no coincide");
                return;
            }
            String hashNewPassword = null;
            try {
                hashNewPassword = AESCrypt.encrypt(newPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            changePassword(hashNewPassword);
        });
        dialog.create();
        dialog.show();
    }

    private void changePassword(String newPassword){
        MessageDialog messageDialog = new MessageDialog(context);
        Call<Message> messageCall = mApi.changePassword(usuarioId, newPassword);
        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    messageDialog.showDialog("Contraseña cambiada");
                }else{
                    messageDialog.showDialog("Error al actualizar contraseña");
                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                messageDialog.showDialog("Error al actualizar contraseña");
            }
        });


    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
