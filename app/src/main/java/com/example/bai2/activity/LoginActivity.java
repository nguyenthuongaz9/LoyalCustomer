package com.example.bai2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai2.databinding.ActivityLoginBinding;
import com.example.bai2.services.UserService;

public class LoginActivity extends AppCompatActivity {

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserService(this);
        userService.initializeDefaultUser();

        if (userService.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
        }

        binding.loginBtn.setOnClickListener(v -> {
            String username = binding.usernameTxt.getText().toString();
            String password = binding.passwordTxt.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userService.authenticate(username, password)) {
                userService.setLoggedIn(true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
