package com.example.turismo.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.turismo.R;
import com.example.turismo.models.Lugar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.ViewHolder> {

    private List<Lugar> mLugares;
    private List<Lugar> originalLugares;
    private Context context;
    private View contactView;

    public LugarAdapter(List<Lugar> mLugares) {
        this.originalLugares = mLugares;
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

        // Inflate the custom layout
        contactView = inflater.inflate(R.layout.item_place, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Lugar lugar = mLugares.get(position);

        // Set item views based on your views and data model
        TextView lugarNameTextView = holder.mLugarName;
        lugarNameTextView.setText(lugar.getNombre());
        TextView lugarDepartamento = holder.mLugarDepartamento;
        lugarDepartamento.setText(lugar.getDepartamento());
        RatingBar ratingBar = holder.mRatingBar;
        ratingBar.setRating(lugar.getCalificacion());

        ImageView lugarImagen = holder.mLugarImage;


        Glide.with(this.context).load(lugar.getImagen()).into(lugarImagen);
    }

    @Override
    public int getItemCount() {
        return mLugares.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mLugarImage;
        private TextView mLugarName;
        private TextView mLugarDepartamento;
        private RatingBar mRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLugarImage = (ImageView) itemView.findViewById(R.id.lugar_image);
            mLugarName = (TextView) itemView.findViewById(R.id.lugar_name);
            mLugarDepartamento = (TextView) itemView.findViewById(R.id.lugar_departamento);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
        }
    }

    public void sortOptions(int i){
        Log.d("STATE", String.valueOf(i));
        switch (i){
            case 0:
                Collections.sort(mLugares, Lugar.nameAZComparator);
                break;
            case 1:
                Collections.sort(mLugares, Lugar.nameZAComparator);
                break;
            case 2:
                Collections.sort(mLugares, Lugar.mayorPopularidad);
                break;
            case 3:
                Collections.sort(mLugares, Lugar.menorPopularidad);
                break;
            case 4:
                Collections.sort(mLugares, Lugar.departamentoAZComparator);
                break;
            case 5:
                Collections.sort(mLugares, Lugar.departamentoZAComparator);
                break;
            }
        notifyDataSetChanged();

    }

    public void queryFilter(String query){
        if(query.matches("")){
            mLugares.clear();
            mLugares.addAll(originalLugares);
        }else{
            List<Lugar> collection = null;
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
