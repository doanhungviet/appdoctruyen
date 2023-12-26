package com.example.ph26437_doctruyen_app_assignment_mob403.User.Model;

public class User {
    private String _id;
    private String username;
    private String passwd;
    private String email;
    private String fullname;
    private String err;

    public User() {
    }

    public User(String username, String passwd, String email, String fullname) {
        this.username = username;
        this.passwd = passwd;
        this.email = email;
        this.fullname = fullname;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}

