package com.example.bai2.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bai2.models.Customer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private static final String PREF_NAME = "Customers";
    private static final String KEY_CUSTOMERS = "customerList";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CustomerRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public List<Customer> getCustomers(){
        String json = sharedPreferences.getString(KEY_CUSTOMERS, null);
        Type type = new TypeToken<List<Customer>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }


    public void saveCustomers(List<Customer> customers){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(customers);
        editor.putString(KEY_CUSTOMERS, json);
        editor.apply();
    }

    public void clearCustomers(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CUSTOMERS);
        editor.apply();
    }


}
