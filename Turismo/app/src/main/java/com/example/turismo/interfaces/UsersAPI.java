package com.example.turismo.interfaces;

import com.example.turismo.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersAPI {
    @POST("createUser")
    Call<Usuario> createUser(@Body Usuario usuario);

    @GET("login/{email}/{password}")
    Call<Usuario> requestLogin(@Path("email") String email, @Path("password") String password);
}