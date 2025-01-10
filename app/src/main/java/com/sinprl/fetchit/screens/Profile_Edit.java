package com.sinprl.fetchit.screens;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.data.Porfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile_Edit extends AppCompatActivity {
    FirebaseDatabase database;
    String userID, old_status;
    TextView user_name, user_mobile, user_address, user_amount;
    Spinner choice_of_bank, type_of_product, user_status;
    ArrayAdapter<CharSequence> product_adaptor, bank_adaptor, status_adaptor;

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
        choice_of_bank = findViewById(R.id.spinner_edit_choice_of_bank);
        bank_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.choice_of_bank,
                android.R.layout.simple_spinner_item
        );
        bank_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice_of_bank.setAdapter(bank_adaptor);

        type_of_product = findViewById(R.id.spinner_edit_type_of_product);
        product_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.type_of_products,
                android.R.layout.simple_spinner_item
        );
        product_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_of_product.setAdapter(product_adaptor);

        user_status = findViewById(R.id.spinner_edit_user_status);
        status_adaptor = ArrayAdapter.createFromResource(
                this,
                R.array.status,
                android.R.layout.simple_spinner_item
        );
        status_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_status.setAdapter(status_adaptor);

        populate_userinformation();

        Button button_save = findViewById(R.id.button_profile_edit_save);
        button_save.setOnClickListener(v -> {
            if (save_profile_data_to_database()) {
                finish();
            }
        });

        Button button_cancel = findViewById(R.id.button_profile_edit_cancel);
        button_cancel.setOnClickListener(v -> {
            finish();
        });

    }

    private boolean save_profile_data_to_database() {
        if(valid_input()) {
            DatabaseReference userreference = database.getReference("Profiles/"+userID+"/");
            userreference.child("name").setValue(user_name.getText().toString());
            userreference.child("address").setValue(user_address.getText().toString());
            userreference.child("mobile").setValue(user_mobile.getText().toString());
            userreference.child("amount").setValue(user_amount.getText().toString());
            userreference.child("choiceofbank").setValue(choice_of_bank.getSelectedItem().toString());
            userreference.child("typeofproduct").setValue(type_of_product.getSelectedItem().toString());
            userreference.child("status").setValue(user_status.getSelectedItem().toString());

            String new_status = user_status.getSelectedItem().toString();
            if (!new_status.equals(old_status)){
                DatabaseReference comment_ref = database.getReference("Profiles/"+userID+"/Comments/");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                Comment new_comment = new Comment();
                new_comment.setComment_date(dateFormat.format(date));
                new_comment.setComment_text("Status changed to: " + new_status);
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

    private void populate_userinformation() {
        DatabaseReference userreference = database.getReference("Profiles/"+userID+"/");
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Porfile profile = snapshot.getValue(Porfile.class);
                profile.setId(snapshot.getKey());
                user_name.setText(profile.getName());
                user_mobile.setText(profile.getMobile());
                user_address.setText(profile.getAddress());
                user_amount.setText(profile.getAmount());
                choice_of_bank.setSelection(bank_adaptor.getPosition(profile.getChoiceofbank()));
                type_of_product.setSelection(product_adaptor.getPosition(profile.getTypeofproduct()));
                old_status = profile.getStatus();
                user_status.setSelection(status_adaptor.getPosition(old_status));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}