package com.example.turismo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.turismo.MainDescripcion;
import com.example.turismo.R;
import com.example.turismo.api.ApiClient;
import com.example.turismo.interfaces.LugaresAPI;
import com.example.turismo.models.Lugar;
import com.example.turismo.models.Message;
import com.example.turismo.tools.CustomDateFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.ViewHolder> {

    private List<Lugar> mLugares;
    private List<Lugar> originalLugares;
    private Context context;
    private LugaresAPI mApi;
    private int userid;
    public boolean isFavorite;

    public LugarAdapter(List<Lugar> mLugares, boolean isFavorite) {
        this.originalLugares = mLugares;
        this.isFavorite = isFavorite;
        mLugares = new ArrayList<>();
        mLugares.addAll(originalLugares);
    }

    public void reloadData(List<Lugar> lugares){
        this.originalLugares = lugares;
        mLugares = new ArrayList<>();
        mLugares.addAll(originalLugares);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LugarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View contactView = inflater.inflate(R.layout.item_place, parent, false);

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_prefs_turismo", Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt("user_key", -1);
        mApi = ApiClient.getInstance().create(LugaresAPI.class);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarAdapter.ViewHolder holder, int position) {
        Lugar lugar = mLugares.get(position);
        CustomDateFormat customDateFormat = new CustomDateFormat();

        holder.setLugarId(lugar.getId());
        if(lugar.getFavorite()){
            holder.mFavorite.setImageResource(R.drawable.ic_baseline_favorite_40);
        }else{
            holder.mFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_40);
        }

        if(lugar.getDistancia() == 0){
            holder.mDistancia.setVisibility(View.INVISIBLE);
        }else{
            holder.mDistancia.setText(new DecimalFormat("#.0#").format(lugar.getDistancia()) + "Km");
        }

        TextView lugarNameTextView = holder.mLugarName;
        lugarNameTextView.setText(lugar.getNombre());
        TextView lugarDepartamento = holder.mLugarDepartamento;
        lugarDepartamento.setText(lugar.getDepartamento());
        RatingBar ratingBar = holder.mRatingBar;
        ratingBar.setRating(lugar.getCalificacion());

        ImageView lugarImagen = holder.mLugarImage;
        Glide.with(this.context).load(lugar.getImagen()).into(lugarImagen);
        holder.mFavorite.setOnClickListener(View -> {
            if (lugar.getFavorite()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                builder.setCancelable(true);
                builder.setTitle("Eliminar de favoritos");
                builder.setMessage("Desea eliminar de favoritos?");
                builder.setPositiveButton("Eliminar",
                        (dialog, which) -> onChangeFav(mApi.deleteFav(lugar.getId(), userid), position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> {});
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                onChangeFav(mApi.saveFav(lugar.getId(), userid), position);
            }

        });
        if (lugar.getVisitaFecha() == null){
            holder.mFecha.setVisibility(View.INVISIBLE);
            holder.mTitleFecha.setVisibility(View.INVISIBLE);
        }else{
            holder.mFecha.setText(customDateFormat.getSimpleDateFormat().format(lugar.getVisitaFecha()));
        }
    }

    @Override
    public int getItemCount() {
        return mLugares.size();
    }

    private void onChangeFav(Call<Message> favoriteCall, int position){
        favoriteCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                if (response.code() == 200){
                    assert response.body() != null;
                    mLugares.get(position).setFavorite(response.body().getResult());
                    if(isFavorite){
                        mLugares.remove(position);
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al modificar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int lugarId;
        private final ImageView mLugarImage;
        private final TextView mLugarName;
        private final TextView mLugarDepartamento;
        private final RatingBar mRatingBar;
        private final ImageView mFavorite;
        private final TextView mFecha;
        private final TextView mTitleFecha;
        private final TextView mDistancia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLugarImage = itemView.findViewById(R.id.lugar_image);
            mLugarName = itemView.findViewById(R.id.lugar_name);
            mLugarDepartamento = itemView.findViewById(R.id.lugar_departamento);
            mRatingBar = itemView.findViewById(R.id.rating_bar);
            mFavorite = itemView.findViewById(R.id.favorite_indicator);
            mFecha = itemView.findViewById(R.id.lugar_fecha_visita);
            mTitleFecha = itemView.findViewById(R.id.lugar_titulo_fecha);
            mDistancia = itemView.findViewById(R.id.lugar_distancia);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MainDescripcion.class);
            intent.putExtra("lugarId", lugarId );
            view.getContext().startActivity(intent);
        }

        public void setLugarId(int id){
            this.lugarId = id;
        }
    }

    public void sortOptions(int i){
        switch (i){
            case 0:
                Collections.sort(mLugares, (l1, l2) -> l1.getNombre().compareTo(l2.getNombre()));
                break;
            case 1:
                Collections.sort(mLugares, (l1, l2) -> l2.getNombre().compareTo(l1.getNombre()));
                break;
            case 2:
                Collections.sort(mLugares, (l1, l2) -> Float.compare(l2.getCalificacion(), l1.getCalificacion()));
                break;
            case 3:
                Collections.sort(mLugares, (l1, l2) -> Float.compare(l1.getCalificacion(), l2.getCalificacion()));
                break;
            case 4:
                Collections.sort(mLugares, (l1, l2) -> l1.getDepartamento().compareTo(l2.getDepartamento()));
                break;
            case 5:
                Collections.sort(mLugares, (l1, l2) -> l2.getDepartamento().compareTo(l1.getDepartamento()));
                break;
            case 6:
                List<Lugar> listNull = new ArrayList<>();
                List<Lugar> listDate = new ArrayList<>();
                for (Lugar lugar : mLugares) {
                    if (lugar.getVisitaFecha() == null) {
                        listNull.add(lugar);
                    }else{
                        listDate.add(lugar);
                    }
                }

                Collections.sort(listDate, (l1, l2) -> l2.getVisitaFecha().compareTo(l1.getVisitaFecha()));
                listDate.addAll(listNull);

                mLugares.clear();
                mLugares.addAll(listDate);
                break;
            case 7:
                List<Lugar> listNull2 = new ArrayList<>();
                List<Lugar> listDate2 = new ArrayList<>();
                for (Lugar lugar : mLugares) {
                    if (lugar.getVisitaFecha() == null) {
                        listNull2.add(lugar);
                    }else{
                        listDate2.add(lugar);
                    }
                }

                Collections.sort(listDate2, (l1, l2) -> l1.getVisitaFecha().compareTo(l2.getVisitaFecha()));
                listDate2.addAll(listNull2);

                mLugares.clear();
                mLugares.addAll(listDate2);
                break;
            }
        notifyDataSetChanged();

    }

    public void distanciaFilter(int distancia){
        if(distancia == 0){
            mLugares.clear();
            mLugares.addAll(originalLugares);
        }else{
            List<Lugar> collection;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                collection = originalLugares.stream()
                        .filter(i -> i.getDistancia() <= distancia)
                        .collect(Collectors.toList());
                mLugares.clear();
                mLugares.addAll(collection);
            }
        }
        notifyDataSetChanged();
    }

    public void queryFilter(String query){
        if(query.matches("")){
            mLugares.clear();
            mLugares.addAll(originalLugares);
        }else{
            List<Lugar> collection;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                collection = mLugares.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
                mLugares.clear();
                mLugares.addAll(collection);
            }

        }
        notifyDataSetChanged();
    }
}
