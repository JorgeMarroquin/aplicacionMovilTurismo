package com.example.turismo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.turismo.adapter.CommentAdapter;
import com.example.turismo.api.ApiClient;
import com.example.turismo.databinding.FragmentCommentBinding;
import com.example.turismo.interfaces.CommentsAPI;
import com.example.turismo.interfaces.ScoresAPI;
import com.example.turismo.models.Comment;
import com.example.turismo.models.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    private CommentsAPI mApi;
    private ScoresAPI mApiScore;
    private CommentAdapter adapter;
    private int userid;
    private int lugarId;

    public CommentFragment() {}

    public CommentFragment(int lugarId){
        this.lugarId = lugarId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS_TURISMO), Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(getString(R.string.USER_KEY), -1);

        mApi = ApiClient.getInstance().create(CommentsAPI.class);
        mApiScore = ApiClient.getInstance().create(ScoresAPI.class);

        RecyclerView rvComments = binding.commentList;
        adapter = new CommentAdapter(new ArrayList<>(), lugarId);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getComments();
        getScore();

        binding.commentList.scrollToPosition(adapter.getItemCount()-1);
        binding.inputComment.setEndIconOnClickListener(v -> {
            Comment comment = new Comment(
                    userid,
                    lugarId,
                    new Date(),
                    binding.inputComment.getEditText().getText().toString()
            );
            postComment(comment);
            binding.inputComment.getEditText().setText("");
        });

        return view;
    }

    private void getComments(){
        Call<List<Comment>> commentsCall = mApi.getCommentsById(lugarId, userid);
        commentsCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                adapter.reloadData(response.body());
                binding.commentList.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al obtener los comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setScore(int placeId, int userid, float score){
        Score newScore = new Score(placeId, userid, score);
        Call<Score> scoreCall = mApiScore.changeScore(newScore);
        scoreCall.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(@NonNull Call<Score> call, @NonNull Response<Score> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Puntuación registrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Score> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al enviar puntuación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getScore(){
        Call<Score> scoreCall = mApiScore.getScoreInPlace(userid, lugarId);
        scoreCall.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(@NonNull Call<Score> call, @NonNull Response<Score> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        binding.ratingPlace.setRating(response.body().getScore());
                        binding.ratingPlace.setOnRatingBarChangeListener((ratingBar, v, b) -> setScore(lugarId, userid, v));
                    }else{
                        binding.ratingPlace.setRating(0);
                        binding.ratingPlace.setOnRatingBarChangeListener((ratingBar, v, b) -> setScore(lugarId, userid, v));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Score> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al obtener score", Toast.LENGTH_SHORT).show();
                binding.ratingPlace.setOnRatingBarChangeListener((ratingBar, v, b) -> setScore(lugarId, userid, v));
            }
        });

    }

    private void postComment(Comment comment){
        Call<Comment> call = mApi.createComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful()){
                    adapter.addComment(response.body());
                    binding.commentList.scrollToPosition(adapter.getItemCount() - 1 );
                }

            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al enviar comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}