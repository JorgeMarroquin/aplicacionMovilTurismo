package com.example.turismo;

import android.os.Build;
import android.os.Bundle;

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

    private static final String ARG_PARAM1 = "param1";
    private String typePlaceQuery;

    public LugarFragment() {}

    public LugarFragment(String type) {
        this.typePlaceQuery = type;
    }

    public static LugarFragment newInstance(String param1) {
        LugarFragment fragment = new LugarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typePlaceQuery = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLugarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.sortTypes, android.R.layout.simple_spinner_item);
        binding.sortList.setAdapter(dropdownAdapter);
        mApi = ApiClient.getInstance().create(LugaresAPI.class);

        RecyclerView rvContacts = binding.lugaresList;

        adapter = new LugarAdapter(new ArrayList<>());
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.reloadData(new ArrayList<>());
        Call<List<Lugar>> lugarCall = mApi.getLugares(typePlaceQuery);
        lugarCall.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                mLugares = response.body();
                adapter.reloadData(mLugares);
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error al obtener los restaurantes", Toast.LENGTH_SHORT).show();
            }
        });

        binding.searchField.setOnQueryTextListener(this);
        binding.sortList.setOnItemSelectedListener(this);

        return view;
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}