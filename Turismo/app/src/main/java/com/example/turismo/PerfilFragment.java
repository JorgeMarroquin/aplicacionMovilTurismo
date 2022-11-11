package com.example.turismo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.turismo.adapter.ChangePasswordAdapter;
import com.example.turismo.databinding.FragmentLugarBinding;
import com.example.turismo.databinding.FragmentPerfilBinding;
import com.example.turismo.models.Usuario;
import com.example.turismo.mvpPresenter.IPerfilPresenter;
import com.example.turismo.mvpPresenter.PerfilPresenter;
import com.example.turismo.mvpView.IPerfilView;
import com.example.turismo.tools.LoadingDialogBar;
import com.example.turismo.tools.MessageDialog;

public class PerfilFragment extends Fragment implements IPerfilView {

    private FragmentPerfilBinding binding;
    private IPerfilPresenter presenter;
    private SharedPreferences sharedPreferences;
    private LoadingDialogBar loadingDialogBar;
    private MessageDialog messageDialog;
    private int userid;
    private ChangePasswordAdapter changePasswordAdapter;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(getString(R.string.USER_KEY), -1);

        this.messageDialog = new MessageDialog(view.getContext());
        this.loadingDialogBar = new LoadingDialogBar(view.getContext());

        binding.perfilButtonExit.setOnClickListener(v -> closeSession());
        this.presenter = new PerfilPresenter(this);
        loadingDialogBar.showDialog("Cargando usuario");
        this.presenter.getUser(userid);
        binding.perfilButtonSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setCancelable(true);
            builder.setTitle("Guardar cambios");
            builder.setMessage("Desea guardar los cambios?");
            builder.setPositiveButton("Guardar",
                    (dialog, which) -> saveUser());
            builder.setNegativeButton("Cancelar", (dialog, which) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        binding.perfilButtonChangePassword.setOnClickListener(v -> {
            changePasswordAdapter.showDialog();
        });
        return view;
    }

    private void setUserInformation(Usuario usuario){
        binding.perfilApellido.getEditText().setText(usuario.getApellido());
        binding.perfilCorreo.getEditText().setText(usuario.getCorreo());
        binding.perfilCountry.setCountryForNameCode(usuario.getNacionalidad());
        binding.perfilNombre.getEditText().setText(usuario.getNombre());
        binding.perfilPhone.getEditText().setText(usuario.getTelefono());
        changePasswordAdapter = new ChangePasswordAdapter(binding.getRoot().getContext(), usuario.getPassword(), usuario.getId());
    }

    private void closeSession(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(binding.getRoot().getContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    private void saveUser(){
        Usuario editUser = new Usuario();
        editUser.setId(userid);
        editUser.setApellido(binding.perfilApellido.getEditText().getText().toString());
        editUser.setCorreo(binding.perfilCorreo.getEditText().getText().toString());
        editUser.setNacionalidad(binding.perfilCountry.getSelectedCountryNameCode());
        editUser.setNombre(binding.perfilNombre.getEditText().getText().toString());
        editUser.setTelefono(binding.perfilPhone.getEditText().getText().toString());
        loadingDialogBar.showDialog("Actualizando usuario");
        presenter.saveUser(editUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onUserSuccess(Usuario usuario) {
        setUserInformation(usuario);
        loadingDialogBar.hideDialog();

    }

    @Override
    public void onUserError(String msg) {
        loadingDialogBar.hideDialog();
        messageDialog.showDialog(msg);

    }

    @Override
    public void onSaveUserSuccess(Usuario usuario) {
        loadingDialogBar.hideDialog();
        messageDialog.showDialog("Usuario actualizado");
    }

    @Override
    public void onSaveUserError(String msg) {
        loadingDialogBar.hideDialog();
        messageDialog.showDialog(msg);
    }
}