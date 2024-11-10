// repository/UserRepository.java
package com.example.bai2.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bai2.models.User;

public class UserRepository {
    private static final String PREF_NAME = "User";
    private SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public User getUser() {
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (username != null && password != null) {
            return new User(username, password, isFirstLaunch, isLoggedIn);
        }
        return null;
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putBoolean("isFirstLaunch", user.getFirstLaunch());
        editor.putBoolean("isLoggedIn", user.getLoggedIn());
        editor.apply();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", isFirstLaunch);
        editor.apply();
    }
}
