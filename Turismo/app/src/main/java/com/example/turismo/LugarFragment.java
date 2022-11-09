package com.example.turismo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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
    private int configuracion = 0; //0 normal, 1 favoritos, 2 cerca de mi
    private Location location;
    private SharedPreferences sharedPreferences;
    private FusedLocationProviderClient fusedLocationClient;
    private int userid;

    public LugarFragment() {
    }

    public LugarFragment(String type) {
        this.typePlaceQuery = type;
    }

    public LugarFragment(String type, int configuracion) {
        this.typePlaceQuery = type;
        this.configuracion = configuracion;
    }

    public LugarFragment(String type, int configuracion, boolean isFavorito) {
        this.typePlaceQuery = type;
        this.isFavorito = isFavorito;
        this.configuracion = configuracion;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLugarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(getString(R.string.USER_KEY), -1);

        mApi = ApiClient.getInstance().create(LugaresAPI.class);

        RecyclerView rvContacts = binding.lugaresList;
        adapter = new LugarAdapter(new ArrayList<>(), isFavorito);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.reloadData(new ArrayList<>());

        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(view.getContext(), (configuracion == 2) ? R.array.sortDistance : R.array.sortTypes, android.R.layout.simple_spinner_item);
        binding.sortList.setAdapter(dropdownAdapter);

        binding.searchField.setOnQueryTextListener(this);
        binding.sortList.setOnItemSelectedListener(this);

        loadLugares();

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
        switch (configuracion){
            case 1:
                getLugares(mApi.getUserFavorites(typePlaceQuery, userid));
                break;
            case 2:
                getLocation();
                if(location != null){
                    getLugares(mApi.getLugaresDistancia(typePlaceQuery, userid, location.getLatitude(), location.getLongitude()));
                }
                break;
            default:
                getLugares(mApi.getLugares(typePlaceQuery, userid));
                break;
        }
    }

    private void getLocation(){
        List<Location> result = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.getRoot().getContext());
        if (ActivityCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    2
            );
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location lastLocation) {
                        location = lastLocation;
                    }
                });

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
        if(configuracion == 2){
            adapter.sortOptions(i);
        }else{
            adapter.sortOptions(i);
            binding.lugaresList.scrollToPosition(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}