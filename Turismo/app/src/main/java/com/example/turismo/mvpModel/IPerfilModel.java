package com.example.turismo.mvpModel;

import com.example.turismo.models.Usuario;

public interface IPerfilModel {
    void getUser(int userid);
    void saveUser(Usuario usuario);
}
