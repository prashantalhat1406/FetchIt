package com.sinprl.fetchit.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.interfaces.OnItemClickListener;

import java.util.List;

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Comment> comments;
    private final OnItemClickListener mOnItemClickListener;

    public CommentAdaptor(Context mContext, List<Comment> comments, OnItemClickListener mOnItemClickListener){
        this.context = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.comments = comments;
        this.mOnItemClickListener = mOnItemClickListener;
    }



    @NonNull
    @Override
    public CommentAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_profile_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdaptor.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.comment_text.setText(comment.getComment_text());
        holder.comment_date.setText(comment.getComment_date());
        holder.itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

public static class ViewHolder extends RecyclerView.ViewHolder{

    public final TextView comment_text;
    public final TextView comment_date;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_text =  itemView.findViewById(R.id.text_item_profile_history_text);
        comment_date =  itemView.findViewById(R.id.text_item_profile_history_date);
    }
}
}
