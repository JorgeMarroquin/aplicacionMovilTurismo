package com.example.turismo.mvpPresenter;

import android.content.Context;

import com.example.turismo.models.Usuario;
import com.example.turismo.mvpModel.IPerfilModel;
import com.example.turismo.mvpModel.PerfilModel;
import com.example.turismo.mvpView.IPerfilView;

import java.util.List;

public class PerfilPresenter implements IPerfilPresenter{
    private IPerfilView view;
    private IPerfilModel model;

    public PerfilPresenter(IPerfilView pView) {
        this.view = pView;
        this.model = new PerfilModel(this);
    }

    @Override
    public void getUser(int userid) {
        this.model.getUser(userid);
    }

    @Override
    public void onUserSuccess(Usuario user) {
        if (view == null)
            return;

        this.view.onUserSuccess(user);
    }

    @Override
    public void onUserError(String msg) {
        if (view == null)
            return;

        this.view.onUserError(msg);
    }

}
