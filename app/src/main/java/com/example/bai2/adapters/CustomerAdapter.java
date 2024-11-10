package com.example.bai2.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai2.databinding.CustomerItemBinding;
import com.example.bai2.models.Customer;
import com.example.bai2.services.CustomerService;

import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customerList;
    private CustomerService customerService;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CustomerItemBinding binding = CustomerItemBinding.inflate(inflater, parent, false);
        customerService = new CustomerService(parent.getContext());
        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer, position);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    private void removeCustomerAt(int position) {
        customerList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, customerList.size());
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final CustomerItemBinding binding;

        public CustomerViewHolder(CustomerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Customer customer, int position) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            binding.phoneNumber.setText(customer.getPhoneNumber());
            binding.point.setText(String.valueOf(customer.getPoint()));
            binding.note.setText(customer.getNote());
            binding.createdAt.setText(dateFormat.format(customer.getCreatedAt()));
            binding.updatedAt.setText(dateFormat.format(customer.getUpdatedAt()));

            // Set delete button click listener
            binding.deleteBtn.setOnClickListener(v -> {
                customerService.deleteCustomerByPhoneNumber(customer.getPhoneNumber());
                removeCustomerAt(position);
            });
        }
    }
}
