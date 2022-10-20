package com.example.turismo.interfaces;

import com.example.turismo.models.Score;
import com.example.turismo.models.Visita;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VisitasAPI {

    @POST("changevisitaDate")
    Call<Visita> changeDate(@Body Visita visita);

}
