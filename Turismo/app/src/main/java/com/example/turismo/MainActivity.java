package com.example.turismo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.ActivityMainBinding;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Usuario;
import com.example.turismo.tools.AESCrypt;
import com.example.turismo.tools.LoadingDialogBar;
import com.example.turismo.tools.MessageDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private String mEmail;
    private int userId;
    private MessageDialog messageDialog;

    private ActivityMainBinding binding;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedpreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        mEmail = sharedpreferences.getString(getString(R.string.EMAIL_KEY), null);
        userId = sharedpreferences.getInt(getString(R.string.USER_KEY), -1);
        messageDialog = new MessageDialog(this);

        goToApp();
        binding.registro.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Registro.class);
            startActivity(i);
        });

        binding.loginButton.setOnClickListener(v -> {
            if(binding.password.getText().toString().equals("") || binding.email.getText().toString().equals("")){
                messageDialog.showDialog("Llena todos los campos");
            }else{
                String hashPassword = null;
                try {
                    hashPassword = AESCrypt.encrypt(binding.password.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                login(binding.email.getText().toString(), hashPassword);
            }
        });
    }

    public void login(String email, String password){
        LoadingDialogBar loadingDialogBar = new LoadingDialogBar(this);
        MessageDialog messageDialog = new MessageDialog(this);
        loadingDialogBar.showDialog("Cargando");
        UsersAPI mApi = ApiClient.getInstance().create(UsersAPI.class);
        Call<Usuario> call = mApi.requestLogin(email, password);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                loadingDialogBar.hideDialog();
                if (response.isSuccessful()) {
                    usuario = response.body();
                    userId = usuario.getId();
                    mEmail = usuario.getCorreo();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(getString(R.string.EMAIL_KEY), email);
                    editor.putInt(getString(R.string.USER_KEY), usuario.getId());
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, Application.class);
                    startActivity(i);
                } else {
                    messageDialog.showDialog("Correo o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                loadingDialogBar.hideDialog();
                messageDialog.showDialog("Error al acceder");
            }
        });
    }

    private void goToApp(){
        if (mEmail != null && userId != -1) {
            Intent i = new Intent(MainActivity.this, Application.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        goToApp();
    }
}