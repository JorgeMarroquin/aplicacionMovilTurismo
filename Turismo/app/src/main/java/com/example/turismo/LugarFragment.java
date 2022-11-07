package com.example.turismo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.turismo.adapter.LugarAdapter;
import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.FragmentLugarBinding;
import com.example.turismo.interfaces.LugaresAPI;
import com.example.turismo.models.Lugar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LugarFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    private FragmentLugarBinding binding;
    private List<Lugar> mLugares;
    private LugaresAPI mApi;
    private LugarAdapter adapter;
    private String typePlaceQuery;
    private boolean isFavorito = false;
    private SharedPreferences sharedPreferences;
    private int userid;

    public LugarFragment() {}

    public LugarFragment(String type) {
        this.typePlaceQuery = type;
    }

    public LugarFragment(String type, boolean isFavorito) {
        this.typePlaceQuery = type;
        this.isFavorito = isFavorito;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLugarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(getString(R.string.USER_KEY), -1);

        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.sortTypes, android.R.layout.simple_spinner_item);
        binding.sortList.setAdapter(dropdownAdapter);
        mApi = ApiClient.getInstance().create(LugaresAPI.class);

        RecyclerView rvContacts = binding.lugaresList;

        adapter = new LugarAdapter(new ArrayList<>(), isFavorito);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.reloadData(new ArrayList<>());
        loadLugares();

        binding.searchField.setOnQueryTextListener(this);
        binding.sortList.setOnItemSelectedListener(this);

        return view;
    }

    public void goToTop(){
        binding.lugaresList.scrollToPosition(0);
    }

    private void getLugares(Call<List<Lugar>> lugarCall){
        lugarCall.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(@NonNull Call<List<Lugar>> call, @NonNull Response<List<Lugar>> response) {
                mLugares = response.body();
                adapter.reloadData(mLugares);
            }
            @Override
            public void onFailure(@NonNull Call<List<Lugar>> call, @NonNull Throwable t) {
                Toast.makeText(binding.getRoot().getContext(), "Error al obtener los lugares", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLugares(){
        if(isFavorito){
            getLugares(mApi.getUserFavorites(typePlaceQuery, userid));
        }else {
            getLugares(mApi.getLugares(typePlaceQuery, userid));
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        loadLugares();
        binding.sortList.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adapter.queryFilter(newText);
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.sortOptions(i);
        binding.lugaresList.scrollToPosition(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}