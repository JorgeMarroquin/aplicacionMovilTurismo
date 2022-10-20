package com.example.turismo.interfaces;

import com.example.turismo.models.Comment;
import com.example.turismo.models.Score;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScoresAPI {

    @GET("getScoreInPlace/{user}/{place}")
    Call<Score> getScoreInPlace(@Path("user") int user, @Path("place") int place);

    @POST("changeScore")
    Call<Score> changeScore(@Body Score score);
}
