package com.example.bai2.services;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import com.example.bai2.R;
import com.example.bai2.models.Customer;
import com.example.bai2.repository.CustomerRepository;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(Context context){
        this.customerRepository = new CustomerRepository(context);
    }

    public List<Customer> getCustomers(){
        return customerRepository.getCustomers();
    }

    public void addCustomer(String phoneNumber, int point, String note){
        List<Customer> customers = customerRepository.getCustomers();
        Customer newCustomer = new Customer(phoneNumber, point, note ,new Date(), new Date());
        customers.add(newCustomer);
        customerRepository.saveCustomers(customers);
    }

    public void updateCustomer(String phoneNumber, int newPoint , String newNote){
        List<Customer> customers = customerRepository.getCustomers();
        for (Customer c : customers){
            if(c.getPhoneNumber().equals(phoneNumber)){
                c.setPoint(newPoint);
                c.setNote(newNote);
                c.setUpdatedAt(new Date());
                break;
            }
        }
        customerRepository.saveCustomers(customers);
    }

    public List<Customer> deleteCustomerByPhoneNumber(String phoneNumber) {
        List<Customer> customers = customerRepository.getCustomers();
        List<Customer> result = customers;
        for (Customer customer : customers) {

            if (customer.getPhoneNumber().contains(phoneNumber)) {
                result.remove(customer);
            }
        }
        customerRepository.saveCustomers(result);
        return result;
    }

    public List<Customer> findCustomersByPhoneNumber(String phoneNumber) {
        List<Customer> customers = customerRepository.getCustomers();
        List<Customer> result = new ArrayList<>();

        for (Customer customer : customers) {

            if (customer.getPhoneNumber().contains(phoneNumber)) {
                result.add(customer);
            }
        }
        return result;
    }
    public Customer findCustomerByPhoneNumber(String phoneNumber) {
        List<Customer> customers = customerRepository.getCustomers();

        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }

    public Boolean validateValue(String phoneNumber, String note, Context context ){
        if (phoneNumber.isEmpty()) {
            return false;
        }
        if (phoneNumber.length() < 10) {
            Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (note.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập ghi chú", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void deleteAllCustomers(){
        customerRepository.clearCustomers();
    }


    public void exportToXml(Context context) {
        List<Customer> customers = customerRepository.getCustomers();
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag("", "Customers");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            for (Customer customer : customers) {
                xmlSerializer.startTag("", "Customer");
                xmlSerializer.startTag("", "PhoneNumber");
                xmlSerializer.text(customer.getPhoneNumber());
                xmlSerializer.endTag("", "PhoneNumber");

                xmlSerializer.startTag("", "Point");
                xmlSerializer.text(String.valueOf(customer.getPoint()));
                xmlSerializer.endTag("", "Point");

                xmlSerializer.startTag("", "Note");
                xmlSerializer.text(customer.getNote());
                xmlSerializer.endTag("", "Note");

                xmlSerializer.startTag("", "CreatedAt");
                xmlSerializer.text(dateFormat.format(customer.getCreatedAt()));
                xmlSerializer.endTag("", "CreatedAt");

                xmlSerializer.startTag("", "UpdatedAt");
                xmlSerializer.text(dateFormat.format(customer.getUpdatedAt()));
                xmlSerializer.endTag("", "UpdatedAt");

                xmlSerializer.endTag("", "Customer");
            }

            xmlSerializer.endTag("", "Customers");
            xmlSerializer.endDocument();


            File directory = new File(context.getFilesDir(), "customers_data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    Log.d("CustomerService", "Directory created successfully.");
                } else {
                    Log.d("CustomerService", "Failed to create directory.");
                    Toast.makeText(context, "Failed to create directory", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            File file = new File(directory, "customersoutput.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(writer.toString().getBytes());
            fileOutputStream.close();

            Toast.makeText(context, "Exported to XML successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CustomerService", "Error writing to file: " + e.getMessage());
            Toast.makeText(context, "Failed to export XML", Toast.LENGTH_SHORT).show();
        }
    }


    public void importFromXml(Context context) {
        List<Customer> customers = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            // Đọc tệp từ thư mục raw (dựng thành tệp này trước khi biên dịch ứng dụng)
            InputStream inputStream = context.getResources().openRawResource(R.raw.customersinput);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            String tag = null;
            String phoneNumber = null;
            int point = 0;
            String note = null;
            Date createdAt = null;
            Date updatedAt = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                tag = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Customer".equals(tag)) {
                            phoneNumber = null;
                            point = 0;
                            note = null;
                            createdAt = null;
                            updatedAt = null;
                        } else if ("PhoneNumber".equals(tag)) {
                            phoneNumber = parser.nextText();
                        } else if ("Point".equals(tag)) {
                            point = Integer.parseInt(parser.nextText());
                        } else if ("Note".equals(tag)) {
                            note = parser.nextText();
                        } else if ("CreatedAt".equals(tag)) {
                            createdAt = dateFormat.parse(parser.nextText());
                        } else if ("UpdatedAt".equals(tag)) {
                            updatedAt = dateFormat.parse(parser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("Customer".equals(tag) && phoneNumber != null && createdAt != null && updatedAt != null) {
                            Customer customer = new Customer(phoneNumber, point, note, createdAt, updatedAt);
                            customers.add(customer);
                        }
                        break;
                }
                eventType = parser.next();
            }

            inputStream.close();
            customerRepository.saveCustomers(customers);
            Toast.makeText(context, "Imported from XML successfully!", Toast.LENGTH_SHORT).show();

        } catch (XmlPullParserException | IOException | ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to import XML", Toast.LENGTH_SHORT).show();
        }
    }



}
