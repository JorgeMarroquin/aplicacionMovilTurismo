package com.example.turismo.mvpPresenter;

import android.content.Context;

import com.example.turismo.models.Usuario;

import java.util.List;

public interface IPerfilPresenter {

    void getUser(int userid);
    void onUserSuccess(Usuario usuario);
    void onUserError(String msg);

    void saveUser(Usuario usuario);
    void onSaveUserSuccess(Usuario usuario);
    void onSaveUserError(String msg);
}
