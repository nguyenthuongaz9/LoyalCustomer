package com.example.bai2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bai2.adapters.CustomerAdapter;
import com.example.bai2.databinding.ActivityListCustomerBinding;
import com.example.bai2.models.Customer;
import com.example.bai2.models.User;
import com.example.bai2.repository.UserRepository;
import com.example.bai2.services.CustomerService;

import java.io.File;
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
        String filePath = customerService.exportToXml(this);

        if (filePath != null) {
            File file = new File(filePath);
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Exported Customer Data");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find attached the exported XML file.");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
            }
        }
    }

    private void onClickImportButton(){
        customerService.importFromXml(this);
        recreate();
    }

    private void onClickResetButton(){
        customerService.deleteAllCustomers();
    }

}