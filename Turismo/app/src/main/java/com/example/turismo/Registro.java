package com.example.turismo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.ActivityRegistroBinding;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Usuario;
import com.example.turismo.tools.AESCrypt;
import com.example.turismo.tools.LoadingDialogBar;
import com.example.turismo.tools.MessageDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private Usuario nuevoUsuario;
    private MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        messageDialog = new MessageDialog(this);

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
                messageDialog.showDialog("Llena todos los campos");
            }else{
                if(binding.verify.getText().toString().matches(binding.password.getText().toString())){
                    register(nuevoUsuario);
                }else{
                    messageDialog.showDialog("Las contraseñas no coinciden");
                }
            }

        });
    }

    public void register(Usuario usuario){
        LoadingDialogBar loading = new LoadingDialogBar(Registro.this);
        loading.showDialog("Cargando");
        UsersAPI mApi = ApiClient.getInstance().create(UsersAPI.class);
        Call<Usuario> call = mApi.createUser(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                loading.hideDialog();
                if (response.isSuccessful()){
                    messageDialog.showDialog("Usuario creado");
                    Intent i = new Intent(Registro.this, MainActivity.class);
                    startActivity(i);
                }
                if (response.code() == 409){
                    messageDialog.showDialog("El correo ya fue registrado");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                messageDialog.showDialog("Error al crear el usuario");
                loading.hideDialog();
            }
        });

    }
}