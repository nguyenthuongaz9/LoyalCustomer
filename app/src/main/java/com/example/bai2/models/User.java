package com.example.bai2.models;

public class User {
    private String username;
    private String password;
    private Boolean isFirstLaunch = true;
    private Boolean isLoggedIn = false;

    public User(String username, String password, Boolean isFirstLaunch, Boolean isLoggedIn) {
        this.username = username;
        this.password = password;
        this.isFirstLaunch = isFirstLaunch;
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(Boolean firstLaunch) {
        isFirstLaunch = firstLaunch;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
