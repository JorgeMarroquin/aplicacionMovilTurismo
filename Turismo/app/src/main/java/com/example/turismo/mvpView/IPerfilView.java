package com.example.turismo.mvpView;

import com.example.turismo.models.Usuario;

import java.util.List;

public interface IPerfilView {

    void onUserSuccess(Usuario usuario);
    void onUserError(String msg);
}
