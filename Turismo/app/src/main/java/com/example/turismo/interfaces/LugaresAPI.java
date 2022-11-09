package com.example.turismo.interfaces;

import com.example.turismo.models.Lugar;
import com.example.turismo.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LugaresAPI {
    @GET("getLugares/{tipo}/{user}")
    Call<List<Lugar>> getLugares(@Path("tipo") String tipo, @Path("user") int user);

    @GET("getLugarById/{id}/{user}")
    Call<Lugar> getLugarById(@Path("id") int id, @Path("user") int user);

    @GET("getUserFavorites/{type}/{userid}")
    Call<List<Lugar>> getUserFavorites(@Path("type") String type, @Path("userid") int userId);

    @GET("getLugaresDistancia/{type}/{userid}/{latitud}/{longitud}")
    Call<List<Lugar>> getLugaresDistancia(@Path("type") String type, @Path("userid") int userId, @Path("longitud") double longitud, @Path("latitud") double latitud);

    @POST("saveFav/{id}/{user}")
    Call<Message> saveFav(@Path("id") int id, @Path("user") int user);

    @DELETE("deleteFav/{id}/{user}")
    Call<Message> deleteFav(@Path("id") int id, @Path("user") int user);
}
