package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.data.Porfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile_Add extends AppCompatActivity {

    FirebaseDatabase database;
    EditText user_name, user_address, user_mobile, user_amount;
    Spinner type_of_product, choice_of_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        user_name = findViewById(R.id.text_user_name);
        user_address = findViewById(R.id.text_user_address);
        user_mobile = findViewById(R.id.text_user_mobile);
        user_amount = findViewById(R.id.text_user_amount);

        type_of_product = findViewById(R.id.spinner_type_of_product);
        ArrayAdapter<CharSequence> product_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.type_of_products,
                android.R.layout.simple_spinner_item
        );
        product_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_of_product.setAdapter(product_adaptor);

        choice_of_bank = findViewById(R.id.spinner_choice_of_bank);
        ArrayAdapter<CharSequence> bank_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.choice_of_bank,
                android.R.layout.simple_spinner_item
        );
        bank_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice_of_bank.setAdapter(bank_adaptor);

        Button button_save = findViewById(R.id.button_profile_save);
        button_save.setOnClickListener(v -> {
            if (save_profile_data_to_database()) {
                finish();
                Intent home_screen = new Intent(Profile_Add.this, Home.class);
                startActivity(home_screen);
            }
        });

        Button button_cancel = findViewById(R.id.button_profile_cancel);
        button_cancel.setOnClickListener(v -> {
            finish();
            Intent home_screen = new Intent(Profile_Add.this, Home.class);
            startActivity(home_screen);
        });

    }

    private boolean save_profile_data_to_database() {
        if(valid_input()) {
            DatabaseReference rootRef = database.getReference();
            String uniqueKey = rootRef.child("Profiles").push().getKey();
            DatabaseReference databaseReference = database.getReference("Profiles/");
            Porfile new_profile = new Porfile();
            new_profile.setName(user_name.getText().toString());
            new_profile.setAddress(user_address.getText().toString());
            new_profile.setMobile(user_mobile.getText().toString());
            new_profile.setTypeofproduct(type_of_product.getSelectedItem().toString());
            new_profile.setChoiceofbank(choice_of_bank.getSelectedItem().toString());
            new_profile.setAmount(user_amount.getText().toString());
            new_profile.setStatus("NEW");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            new_profile.setEntry_date(dateFormat.format(date));
            databaseReference.child(uniqueKey).setValue(new_profile);

            DatabaseReference comment_ref = database.getReference("Profiles/"+uniqueKey+"/Comments/");
            Comment new_comment = new Comment();
            new_comment.setComment_date(dateFormat.format(date));
            new_comment.setComment_text("NEW entry created");
            comment_ref.push().setValue(new_comment);

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
        if (user_mobile.getText().toString().length() != 10){
            user_mobile.setError("Mobile must be 10 digits");
            return false;
        }
        if (user_address.getText().toString().isEmpty()){
            user_address.setError("Address is mandatory");
            return false;
        }
        if (type_of_product.getSelectedItem().toString().equals("Type Of Products")){
            ((TextView) type_of_product.getSelectedView()).setError("Select Type Of Products");
            return false;
        }
        if (choice_of_bank.getSelectedItem().toString().equals("Select Bank")){
            ((TextView) choice_of_bank.getSelectedView()).setError("Select Bank Name");
            return false;
        }
        if (user_amount.getText().toString().isEmpty()){
            user_amount.setError("Amount is mandatory");
            return false;
        }

        return true;
    }
}