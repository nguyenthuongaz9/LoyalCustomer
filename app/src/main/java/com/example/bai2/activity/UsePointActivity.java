package com.example.bai2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai2.databinding.ActivityUsePointBinding;
import com.example.bai2.models.Customer;
import com.example.bai2.models.User;
import com.example.bai2.repository.UserRepository;
import com.example.bai2.services.CustomerService;

public class UsePointActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private User currentUser;
    private CustomerService customerService;
    private ActivityUsePointBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsePointBinding.inflate(getLayoutInflater());

        userRepository = new UserRepository(this);
        customerService = new CustomerService(this);

        currentUser = userRepository.getUser();
        if (currentUser == null || !currentUser.getLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        setContentView(binding.getRoot());
        binding.listBtn.setOnClickListener(v -> onClickListButton());
        binding.inputBtn.setOnClickListener(v -> onClickInputButton());
        binding.saveBtn.setOnClickListener(v -> onClickSaveButton());
        binding.saveAndNext.setOnClickListener(v -> onClickSaveAndNextButton());

        binding.numberPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handlePhoneNumberChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void onClickListButton(){
        Intent intent = new Intent(this, ListCustomerActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickInputButton(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void onClickSaveButton(){
        String phoneNumber = binding.numberPhone.getText().toString();

        int currentPoint = binding.currentPoint.getText().toString().isEmpty()
                ? 0
                : Integer.parseInt(binding.currentPoint.getText().toString());

        int newPoint = binding.newPoint.getText().toString().isEmpty()
                ? 0
                : Integer.parseInt(binding.newPoint.getText().toString());


        String note = binding.note.getText().toString();
        if(customerService.findCustomerByPhoneNumber(phoneNumber) != null && customerService.validateValue(phoneNumber, note, this) != false){
            int totalPoint = currentPoint - newPoint >= 0 ? currentPoint - newPoint : 0 ;
            customerService.updateCustomer(phoneNumber, totalPoint, note);

            binding.currentPoint.setText(String.valueOf(totalPoint));
        }else{
            Toast.makeText(this, "Số điện thoại trên Không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickSaveAndNextButton(){
        String phoneNumber = binding.numberPhone.getText().toString();

        int currentPoint = binding.currentPoint.getText().toString().isEmpty()
                ? 0
                : Integer.parseInt(binding.currentPoint.getText().toString());

        int newPoint = binding.newPoint.getText().toString().isEmpty()
                ? 0
                : Integer.parseInt(binding.newPoint.getText().toString());


        String note = binding.note.getText().toString();
        if(customerService.findCustomerByPhoneNumber(phoneNumber) != null && customerService.validateValue(phoneNumber,note, this) != false){
            int totalPoint = currentPoint - newPoint >= 0 ? currentPoint - newPoint : 0 ;
            Intent intent = new Intent(this, ListCustomerActivity.class);
            customerService.updateCustomer(phoneNumber, totalPoint, note);
            Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            binding.currentPoint.setText(String.valueOf(totalPoint));
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Số điện thoại trên Không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }



    private void handlePhoneNumberChange(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            Customer customer = customerService.findCustomerByPhoneNumber(phoneNumber);
            if (customer != null) {
                binding.currentPoint.setText(String.valueOf(customer.getPoint()));
                binding.note.setText(customer.getNote());
            } else {
                binding.currentPoint.setText("0");
                binding.note.setText("");
            }
        }
    }


}