package com.example.turismo.interfaces;

import com.example.turismo.models.Message;
import com.example.turismo.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsersAPI {
    @POST("createUser")
    Call<Usuario> createUser(@Body Usuario usuario);

    @GET("login/{email}/{password}")
    Call<Usuario> requestLogin(@Path("email") String email, @Path("password") String password);

    @GET("getUserById/{userId}")
    Call<Usuario> getUserById(@Path("userId") int userId);

    @PUT("updateUser")
    Call<Usuario> updateUser(@Body Usuario usuario);

    @PUT("changePassword/{userId}/{password}")
    Call<Message> changePassword(@Path("userId") int userId, @Path("password") String password);
}
