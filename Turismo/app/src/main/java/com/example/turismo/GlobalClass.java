package com.example.turismo;

import com.example.turismo.models.Usuario;
import android.app.Application;
public class GlobalClass extends Application{
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
