package com.example.turismo.interfaces;

import com.example.turismo.models.Lugar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LugaresAPI {
    @GET("getLugares/{tipo}")
    Call<List<Lugar>> getLugares(@Path("tipo") String tipo);
}
