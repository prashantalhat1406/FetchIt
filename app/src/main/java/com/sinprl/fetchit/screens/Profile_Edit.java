package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.adaptor.SpinnerAdaptor;
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.data.Profile;
import com.sinprl.fetchit.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Profile_Edit extends AppCompatActivity {
    FirebaseDatabase database;
    String userID, old_status;
    EditText user_name, user_mobile, user_address, user_amount, user_code, user_bank, user_bankmanager, user_reference;
    Spinner type_of_product, user_status;
    ArrayAdapter<CharSequence> product_adaptor, bank_adaptor, status_adaptor;
    HashMap<String, String> statusMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");

        userID = getIntent().getExtras().getString("userID","");
        user_name = findViewById(R.id.text_edit_user_name);
        user_mobile = findViewById(R.id.text_edit_user_mobile);
        user_address = findViewById(R.id.text_edit_user_address);
        user_amount = findViewById(R.id.text_edit_user_amount);
        user_code = findViewById(R.id.text_edit_user_code);
        user_bank = findViewById(R.id.text_edit_user_bank);
        user_bankmanager = findViewById(R.id.text_edit_user_bank_manager);
        user_reference = findViewById(R.id.text_edit_user_reference);


//        statusMapping = new HashMap<>();
//        statusMapping.put("New Proposal", "NEW");
//        statusMapping.put("Follow Up", "FOW");
//        statusMapping.put("Log Proposal", "LOG");
//        statusMapping.put("Sanction", "SAN");
//        statusMapping.put("Disbursement", "DIS");
//        statusMapping.put("OTC - PDD", "OTC");
//        statusMapping.put("Payment Done", "PAY");

        product_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.type_of_products,
                android.R.layout.simple_spinner_item
        );
        status_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.status,
                android.R.layout.simple_spinner_item
        );

        type_of_product = findViewById(R.id.spinner_edit_type_of_product);
        String[] productsArray = getResources().getStringArray(R.array.type_of_products);
        List<String> productArrayList = new ArrayList<>(Arrays.asList(productsArray));
        SpinnerAdaptor product_spn_adaptor = new SpinnerAdaptor(this, R.layout.spinner_value, productArrayList);
        type_of_product.setAdapter(product_spn_adaptor);

        user_status = findViewById(R.id.spinner_edit_user_status);
        String[] statusArray = getResources().getStringArray(R.array.status);
        List<String> statusArrayList = new ArrayList<>(Arrays.asList(statusArray));
        SpinnerAdaptor status_adaptor = new SpinnerAdaptor(this, R.layout.spinner_value, statusArrayList);
        user_status.setAdapter(status_adaptor);

        populate_userinformation();

        Button button_save = findViewById(R.id.button_profile_edit_save);
        button_save.setOnClickListener(v -> {
            if (save_profile_data_to_database()) {
                goto_profile_details();
            }
        });

        Button button_cancel = findViewById(R.id.button_profile_edit_cancel);
        button_cancel.setOnClickListener(v -> {
            goto_profile_details();
        });

    }

    private void goto_profile_details() {
        finish();
        Intent intent = new Intent(Profile_Edit.this, Profile_Details.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private boolean save_profile_data_to_database() {
        if(valid_input()) {
            DatabaseReference userreference = database.getReference("Profiles/"+userID+"/");
            userreference.child("name").setValue(user_name.getText().toString().trim());
            userreference.child("address").setValue(user_address.getText().toString().trim());
            userreference.child("mobile").setValue(StringUtils.getValidMobile(user_mobile.getText().toString().trim()));
            userreference.child("amount").setValue(user_amount.getText().toString().trim());
            userreference.child("choiceofbank").setValue(user_bank.getText().toString().trim());
            userreference.child("typeofproduct").setValue(type_of_product.getSelectedItem().toString());
            userreference.child("status").setValue(user_status.getSelectedItem().toString());
            userreference.child("code").setValue(user_code.getText().toString().trim());
            userreference.child("reference").setValue(user_reference.getText().toString().trim());
            userreference.child("bankmanager").setValue(user_bankmanager.getText().toString().trim());

            String new_status = user_status.getSelectedItem().toString();

            if (new_status.equals("DIS")) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = new Date();
                userreference.child("dis_date").setValue(dateFormat.format(date));
            }

            if (!new_status.equals(old_status)){
                DatabaseReference comment_ref = database.getReference("Profiles/"+userID+"/Comments/");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                Comment new_comment = new Comment();
                new_comment.setComment_date(dateFormat.format(date));
                new_comment.setComment_text("Status changed to: " + new_status);
                new_comment.setImportant(Boolean.FALSE);
                comment_ref.push().setValue(new_comment);
            }

            return true;
        }
        else
            return false;
    }

    private boolean valid_input() {
        if (user_name.getText().toString().isEmpty()){
            user_name.setError("Name is mandatory");
            return false;
        }
        if (user_mobile.getText().toString().isEmpty()){
            user_mobile.setError("Mobile is mandatory");
            return false;
        }
//        if (user_mobile.getText().toString().length() != 10){
//            user_mobile.setError("Mobile must be 10 digits");
//            return false;
//        }
        if (user_address.getText().toString().isEmpty()){
            user_address.setError("Address is mandatory");
            return false;
        }
//        if (type_of_product.getSelectedItem().toString().equals("Type Of Products")){
//            ((TextView) type_of_product.getSelectedView()).setError("Select Type Of Products");
//            return false;
//        }
        if (user_bank.getText().toString().isEmpty()){
            user_bank.setError("Bank is mandatory");
            return false;
        }
        if (user_amount.getText().toString().isEmpty()){
            user_amount.setError("Amount is mandatory");
            return false;
        }
        if (user_code.getText().toString().isEmpty()){
            user_code.setError("Code is mandatory");
            return false;
        }


        return true;
    }

    private void populate_userinformation() {
        DatabaseReference userreference = database.getReference("Profiles/"+userID+"/");
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                profile.setId(snapshot.getKey());
                user_name.setText(profile.getName());
                user_mobile.setText(profile.getMobile());
                user_address.setText(profile.getAddress());
                user_amount.setText(profile.getAmount());
                user_code.setText(profile.getCode());
                user_bank.setText(profile.getChoiceofbank());
                type_of_product.setSelection(product_adaptor.getPosition(profile.getTypeofproduct()));
                old_status = profile.getStatus();
                user_status.setSelection(status_adaptor.getPosition(old_status));
                user_reference.setText(profile.getReference());
                user_bankmanager.setText(profile.getBankmanager());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}