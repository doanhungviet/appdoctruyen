package com.example.ph26437_doctruyen_app_assignment_mob403.Comment;

public class PostComments {
    private String id_book;
    private String id_user;
    private String content;
    private String commenttime;

    public PostComments() {
    }

    public PostComments(String id_book, String id_user, String content, String commenttime) {
        this.id_book = id_book;
        this.id_user = id_user;
        this.content = content;
        this.commenttime = commenttime;
    }

    public String getId_book() {
        return id_book;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
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
