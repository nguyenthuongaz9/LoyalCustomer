package com.example.bai2.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bai2.adapters.CustomerAdapter;
import com.example.bai2.databinding.ActivityListCustomerBinding;
import com.example.bai2.models.Customer;
import com.example.bai2.models.User;
import com.example.bai2.repository.UserRepository;
import com.example.bai2.services.CustomerService;

import java.util.List;

public class ListCustomerActivity extends AppCompatActivity {

    private ActivityListCustomerBinding binding;
    private UserRepository userRepository;
    private CustomerService customerService;
    private User currentUser;
    private CustomerAdapter customerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCustomerBinding.inflate(getLayoutInflater());

        userRepository = new UserRepository(this);
        customerService = new CustomerService(this);
        currentUser = userRepository.getUser();
        if (currentUser == null || !currentUser.getLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(binding.getRoot());

        List<Customer> customerList = customerService.getCustomers();
        if(customerList != null){
            customerAdapter = new CustomerAdapter(customerList);
            binding.customerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.customerRecyclerView.setAdapter(customerAdapter);
        }
        binding.inputBtn.setOnClickListener(v -> onClickInputButton());
        binding.useBtn.setOnClickListener(v -> onClickUseButton());
        binding.exportBtn.setOnClickListener(v -> onClickExportButton());
        binding.importBtn.setOnClickListener(v -> onClickImportButton());
        binding.resetBtn.setOnClickListener(v -> onClickResetButton());

    }


    private void onClickInputButton(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickUseButton(){
        Intent intent = new Intent(this, UsePointActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickExportButton(){
        customerService.exportToXml(this);
    }
    private void onClickImportButton(){
        customerService.importFromXml(this);
        recreate();
    }

    private void onClickResetButton(){
        customerService.deleteAllCustomers();
    }

}