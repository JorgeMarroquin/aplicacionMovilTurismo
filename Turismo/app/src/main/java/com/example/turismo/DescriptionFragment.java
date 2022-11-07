package com.example.turismo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.FragmentDescriptionBinding;
import com.example.turismo.interfaces.LugaresAPI;
import com.example.turismo.interfaces.VisitasAPI;
import com.example.turismo.models.Lugar;
import com.example.turismo.models.Message;
import com.example.turismo.models.Visita;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionFragment extends Fragment {

    private FragmentDescriptionBinding binding;
    private int lugarId;
    private LugaresAPI mApi;
    private VisitasAPI mVisitaApi;
    private Lugar lugar;
    private int userid;
    private DatePickerDialog datePickerDialog;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DescriptionFragment() {}

    public DescriptionFragment(int lugarId) {
        this.lugarId = lugarId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(getString(R.string.USER_KEY), -1);

        CollapsingToolbarLayout toolbarLayout = binding.collapsingToolbar;
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        binding.datePickerButton.setOnClickListener(this::OpenDatePicker);
        initDatePiker();

        mApi = ApiClient.getInstance().create(LugaresAPI.class);
        mVisitaApi = ApiClient.getInstance().create(VisitasAPI.class);
        Call<Lugar> lugarCall = mApi.getLugarById(lugarId, userid);
        lugarCall.enqueue(new Callback<Lugar>() {
            @Override
            public void onResponse(@NonNull Call<Lugar> call, @NonNull Response<Lugar> response) {
                lugar = response.body();
                toolbarLayout.setTitle(lugar.getNombre());
                Glide.with(view).load(lugar.getImagen()).into(binding.lugarDescriptionImage);
                binding.ratingDescriptionBar.setRating(lugar.getCalificacion());
                binding.lugarDescriptionDescription.setText(lugar.getDescripcion());
                binding.imagePlaceName.setText(lugar.getNombre());
                binding.imagePlaceMuni.setText("Departamento" + lugar.getDepartamento());
                if(lugar.getFavorite()){
                    binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_60);
                }
                if(lugar.getVisitaFecha() != null){
                    binding.datePickerButton.setText(dateFormat.format(lugar.getVisitaFecha()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Lugar> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });

        binding.waysButton.setOnClickListener(v -> {
            //waze://ul?ll=
            //https://www.waze.com/ul?ll=
            goToUrl("https://www.waze.com/ul?ll=" + lugar.getLatitud().toString() + "," + lugar.getLongitud().toString() + "&navigate=yes");
        });

        binding.mapsButton.setOnClickListener(v -> goToUrl("https://www.google.com/maps/search/?api=1&query="  + lugar.getLatitud().toString() + "%2C" + lugar.getLongitud().toString()));

        binding.favoriteButton.setOnClickListener(v -> {

            if(lugar.getFavorite()){
                onDeleteFav(mApi.deleteFav(lugarId, userid));
            }else{
                onSaveFav(mApi.saveFav(lugarId, userid));
            }
        });

        return view;
    }

    private void goToUrl(String s){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    private void initDatePiker(){
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            String date = makeDateString(day, month+1, year);
            try {
                setVisitaDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        Calendar cal = Calendar.getInstance();
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(binding.getRoot().getContext(), style, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    private void setVisitaDate(String date) throws ParseException {
        Date newDate = dateFormat.parse(date);
        Call<Visita> visitaCall = mVisitaApi.changeDate(new Visita(lugarId, userid, newDate));
        visitaCall.enqueue(new Callback<Visita>() {
            @Override
            public void onResponse(@NonNull Call<Visita> call, @NonNull Response<Visita> response) {
                if (response.isSuccessful()){
                    binding.datePickerButton.setText(date);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Visita> call, @NonNull Throwable t) {
                Toast.makeText(binding.getRoot().getContext(), "Error al cambiar fecha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDeleteFav(Call<Message> favoriteCall){
        favoriteCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                lugar.setFavorite(false);
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_60);
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error al quitar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSaveFav(Call<Message> favoriteCall){
        favoriteCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                lugar.setFavorite(true);
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_60);
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error al agregar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void OpenDatePicker(View view) {
        datePickerDialog.show();
    }
}