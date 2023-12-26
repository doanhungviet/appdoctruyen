package com.example.ph26437_doctruyen_app_assignment_mob403.Home.Model;

import java.util.List;

public class Book {
    private String _id;
    private String bookname;
    private String description;
    private String author;
    private int publishyear;
    private String imgbook;
    private List<String> contentbook;

    public Book() {
    }

    public Book(String _id, String bookname, String description, String author, int publishyear, String imgbook) {
        this._id = _id;
        this.bookname = bookname;
        this.description = description;
        this.author = author;
        this.publishyear = publishyear;
        this.imgbook = imgbook;
    }

    public Book(String bookname, String description, String author, int publishyear, String imgbook, List<String> contentbook) {
        this.bookname = bookname;
        this.description = description;
        this.author = author;
        this.publishyear = publishyear;
        this.imgbook = imgbook;
        this.contentbook = contentbook;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishyear() {
        return publishyear;
    }

    public void setPublishyear(int publishyear) {
        this.publishyear = publishyear;
    }

    public String getImgbook() {
        return imgbook;
    }

    public void setImgbook(String imgbook) {
        this.imgbook = imgbook;
    }
    public List<String> getContentbook() {
        return contentbook;
    }

    public void setContentbook(List<String> contentbook) {
        this.contentbook = contentbook;
    }
}

