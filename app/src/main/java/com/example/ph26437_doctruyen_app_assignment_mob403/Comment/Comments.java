package com.example.ph26437_doctruyen_app_assignment_mob403.Comment;

import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;

public class Comments {
    private User id_user;
    private String content;
    private String commenttime;

    public Comments() {
    }

    public Comments(User id_user, String content, String commenttime) {
        this.id_user = id_user;
        this.content = content;
        this.commenttime = commenttime;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }
}
