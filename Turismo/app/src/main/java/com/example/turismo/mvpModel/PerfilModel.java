package com.example.turismo.mvpModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.turismo.api.ApiClient;
import com.example.turismo.interfaces.UsersAPI;
import com.example.turismo.models.Usuario;
import com.example.turismo.mvpPresenter.IPerfilPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilModel implements IPerfilModel{

    private IPerfilPresenter presenter;
    private UsersAPI api;
    private Usuario temp;

    public PerfilModel(IPerfilPresenter presenter) {
        this.presenter = presenter;
        api = ApiClient.getInstance().create(UsersAPI.class);
    }

    @Override
    public void getUser(int userid) {
        Call<Usuario> usuarioCall = api.getUserById(userid);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                presenter.onUserSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                presenter.onUserError("Usuario no encontrado");

            }
        });
    }
}
