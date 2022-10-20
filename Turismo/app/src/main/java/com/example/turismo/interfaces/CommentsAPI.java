package com.example.turismo.interfaces;

import com.example.turismo.models.Comment;
import com.example.turismo.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentsAPI {

    @GET("getCommentsById/{lugarId}/{usuarioId}")
    Call<List<Comment>> getCommentsById(@Path("lugarId") int lugarId, @Path("usuarioId") int usuarioId);

    @POST("createComment/")
    Call<Comment> createComment(@Body Comment comment);

    @DELETE("deleteComment/{commentId}")
    Call<Comment> deleteComment(@Path("commentId") int commentId);
}
