package com.example.turismo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismo.CommentFragment;
import com.example.turismo.R;
import com.example.turismo.api.ApiClient;
import com.example.turismo.interfaces.CommentsAPI;
import com.example.turismo.interfaces.LugaresAPI;
import com.example.turismo.models.Comment;
import com.example.turismo.models.Lugar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private List<Comment> mComments;
    private int lugarId;
    private CommentsAPI mApi;
    private Context context;

    public CommentAdapter(List<Comment> mComments, int lugarId) {
        this.lugarId = lugarId;
        this.mComments = mComments;
    }

    public void reloadData(List<Comment> mComments){
        this.mComments = mComments;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment){
        this.mComments.add(comment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View contactView = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        mApi = ApiClient.getInstance().create(CommentsAPI.class);
        Comment comment = mComments.get(position);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        holder.mName.setText(comment.getUsuario().getNombre() + " " + comment.getUsuario().getApellido());
        holder.mDate.setText(dateFormat.format(comment.getFecha()));
        holder.mComment.setText(comment.getComentario());
        if(!comment.getIsUser()){
            holder.mDelete.setVisibility(View.INVISIBLE);
        }
        holder.mDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setCancelable(true);
            builder.setTitle("Eliminar comentario");
            builder.setMessage("Deseas eliminar este comentario?");
            builder.setPositiveButton("Eliminar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteComment(comment.getId(), position);
                        }
                    });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void deleteComment(int commentId, int position){
        Call<Comment> call = mApi.deleteComment(commentId);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.code() == 200){
                    mComments.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(context, "Fallo al eliminar comentario", Toast.LENGTH_SHORT);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDate;
        private TextView mComment;
        private ImageView mDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.comment_name);
            mDate = (TextView) itemView.findViewById(R.id.comment_date);
            mComment = (TextView) itemView.findViewById(R.id.comment_text);
            mDelete = (ImageView) itemView.findViewById(R.id.comment_delete);

        }

    }
}
