// service/UserService.java
package com.example.bai2.services;

import android.content.Context;

import com.example.bai2.models.User;
import com.example.bai2.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public UserService(Context context) {
        this.userRepository = new UserRepository(context);
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.getUser();
        return user != null && user.getUsername().equals(username) && user.getPassword().equals(password);
    }

    public void initializeDefaultUser() {
        User user = userRepository.getUser();
        if (user == null || user.getFirstLaunch()) {
            userRepository.saveUser(new User("test1", "123", false, false));
            userRepository.setFirstLaunch(false);
        }
    }

    public boolean isLoggedIn() {
        User user = userRepository.getUser();
        return user != null && user.getLoggedIn();
    }

    public void setLoggedIn(boolean loggedIn) {
        User user = userRepository.getUser();
        if (user != null) {
            user.setLoggedIn(loggedIn);
            userRepository.saveUser(user);
        }
    }
}
