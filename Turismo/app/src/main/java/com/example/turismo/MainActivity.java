package com.example.turismo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.ActivityMainBinding;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Usuario;
import com.example.turismo.tools.AESCrypt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.registro.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Registro.class);
            startActivity(i);
        });

        binding.loginButton.setOnClickListener(v -> {
            String hashPassword = null; try {
                hashPassword = AESCrypt.encrypt(binding.password.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            login(binding.email.getText().toString(), hashPassword);
        });
    }

    public void login(String email, String password){

        UsersAPI mApi = ApiClient.getInstance().create(UsersAPI.class);
        Call<Usuario> call = mApi.requestLogin(email, password);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.code() == 200) {
                    usuario = response.body();
                    Intent i = new Intent(MainActivity.this, Application.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
            }
        });
    }
}