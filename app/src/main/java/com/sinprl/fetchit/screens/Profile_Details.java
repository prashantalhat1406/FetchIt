package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.DataEntry;

public class Profile_Details extends AppCompatActivity {
    FirebaseDatabase database;
    String userID;
    TextView user_name, user_mobile, user_address, user_amount, choice_of_bank, type_of_product, user_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userID = getIntent().getExtras().getString("userID","");
        
        user_name = findViewById(R.id.text_profile_details_name);
        user_mobile = findViewById(R.id.text_profile_details_mobile);
        user_address = findViewById(R.id.text_profile_details_address);
        user_amount = findViewById(R.id.text_profile_details_amount);
        choice_of_bank = findViewById(R.id.text_profile_details_choice_of_bank);
        type_of_product = findViewById(R.id.text_profile_details_type_of_product);
        user_status = findViewById(R.id.text_profile_details_status);

        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        populate_userinformation();

        Button button_close = findViewById(R.id.button_profile_details_close);
        button_close.setOnClickListener(v -> {
            finish();
//            Intent home_screen = new Intent(Profile.this, Home.class);
//            startActivity(home_screen);
        });
        
    }

    private void populate_userinformation() {
        DatabaseReference userreference = database.getReference("Profiles/"+userID+"/");
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataEntry dataEntry = snapshot.getValue(DataEntry.class);
                dataEntry.setId(snapshot.getKey());
                user_name.setText(dataEntry.getName());
                user_mobile.setText(dataEntry.getMobile());
                user_address.setText(dataEntry.getAddress());
                user_amount.setText(dataEntry.getAmount());
                choice_of_bank.setText(dataEntry.getChoiceofbank());
                type_of_product.setText(dataEntry.getTypeofproduct());
                update_status(dataEntry.getStatus());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update_status(String status) {
        user_status.setText(status);
        switch (status){
            case "NEW": user_status.setBackground(getResources().getDrawable(R.color.status_new));
                user_status.setTextColor(getResources().getColor(R.color.white));
                break;
            case "KYC": user_status.setBackground(getDrawable(R.color.status_kyc));
                user_status.setTextColor(getResources().getColor(R.color.black));
                break;
            case "LOG": user_status.setBackground(getDrawable(R.color.status_log));
                user_status.setTextColor(getResources().getColor(R.color.black));
                break;
            case "SAN": user_status.setBackground(getDrawable(R.color.status_san));
                user_status.setTextColor(getResources().getColor(R.color.black));
                break;
            case "DIS": user_status.setBackground(getDrawable(R.color.status_dis));
                user_status.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}