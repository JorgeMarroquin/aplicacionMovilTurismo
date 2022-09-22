package com.example.turismo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.ActivityMainBinding;
import com.example.turismo.databinding.ActivityRegistroBinding;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Usuario;
import com.example.turismo.tools.AESCrypt;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private Usuario nuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonRegister.setOnClickListener(v -> {

            String hashPassword = null; try {
                hashPassword = AESCrypt.encrypt(binding.password.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String finalHashPassword = hashPassword;

            nuevoUsuario = new Usuario(
                    binding.name.getText().toString(),
                    binding.lastName.getText().toString(),
                    binding.email.getText().toString(),
                    finalHashPassword,
                    binding.country.getSelectedCountryName(),
                    binding.phone.getText().toString());

            if(nuevoUsuario.emptyFields()){
                Toast.makeText(Registro.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
            }else{
                if(binding.verify.getText().toString().matches(binding.password.getText().toString())){
                    register(nuevoUsuario);
                }else{
                    Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void register(Usuario usuario){

        UsersAPI mApi = ApiClient.getInstance().create(UsersAPI.class);
        Call<Usuario> call = mApi.createUser(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                try {
                    nuevoUsuario = response.body();
                    Intent i = new Intent(Registro.this, Application.class);
                    startActivity(i);

                }catch (Exception ex){
                    Toast.makeText(Registro.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(Registro.this, "FAILURE", Toast.LENGTH_SHORT).show();
            }
        });
    }
}