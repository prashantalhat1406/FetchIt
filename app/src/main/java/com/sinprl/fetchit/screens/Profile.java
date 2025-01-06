package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.DataEntry;

public class Profile extends AppCompatActivity {

    FirebaseDatabase database;
    EditText user_name, user_address, user_mobile, type_of_product, choice_of_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        user_name = findViewById(R.id.text_user_name);
        user_address = findViewById(R.id.text_user_address);
        user_mobile = findViewById(R.id.text_user_mobile);
        type_of_product = findViewById(R.id.text_type_of_product);
        choice_of_bank = findViewById(R.id.text_choice_of_bank);

        Button button_save = findViewById(R.id.button_profile_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_profile_data_to_database();
                finish();
                Intent home_screen = new Intent(Profile.this, Home.class);
                startActivity(home_screen);
            }
        });

        Button button_cancel = findViewById(R.id.button_profile_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent home_screen = new Intent(Profile.this, Home.class);
                startActivity(home_screen);
            }
        });

    }

    private void save_profile_data_to_database() {
        DatabaseReference databaseReference = database.getReference("Profiles/");

        DataEntry new_data_entry = new DataEntry();
        new_data_entry.setName(user_name.getText().toString());
        new_data_entry.setAddress(user_address.getText().toString());
        new_data_entry.setMobile(user_mobile.getText().toString());
        new_data_entry.setTypeofproduct(type_of_product.getText().toString());
        new_data_entry.setChoiceofbank(choice_of_bank.getText().toString());

        databaseReference.push().setValue(new_data_entry);
    }
}