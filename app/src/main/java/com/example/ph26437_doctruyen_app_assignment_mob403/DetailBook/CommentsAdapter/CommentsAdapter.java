package com.example.ph26437_doctruyen_app_assignment_mob403.DetailBook.CommentsAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ph26437_doctruyen_app_assignment_mob403.Comment.Comments;
import com.example.ph26437_doctruyen_app_assignment_mob403.R;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{
    private Context context;
    private List<Comments> commentsList;
    public CommentsAdapter(Context context) {
        this.context = context;
    }

    public void setDataComments(List<Comments> commentsList) {
        this.commentsList = commentsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comments comments = commentsList.get(position);
        if (comments != null) {
            User userId = comments.getId_user();

            if (userId != null) {
                String fullname = userId.getFullname();

                if (fullname != null) {
                    holder.tv_namecomment.setText(fullname);
                } else {
                    holder.tv_namecomment.setText("Unknown User");
                }
            } else {
                holder.tv_namecomment.setText("Unknown User");
            }
        }
        holder.tv_contentcomment.setText(comments.getContent());
        holder.tv_commenttimecomment.setText(comments.getCommenttime());
    }

    @Override
    public int getItemCount() {
        return commentsList == null ? 0 : commentsList.size();
    }


    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_namecomment, tv_contentcomment, tv_commenttimecomment;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namecomment = itemView.findViewById(R.id.tv_namecomment);
            tv_contentcomment = itemView.findViewById(R.id.tv_contentcomment);
            tv_commenttimecomment = itemView.findViewById(R.id.tv_commenttimecomment);
        }
    }
}
