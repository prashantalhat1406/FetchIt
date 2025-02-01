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
import com.sinprl.fetchit.adaptor.SpinnerAdaptor;
import com.sinprl.fetchit.constants.App_Constants;
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.data.Profile;
import com.sinprl.fetchit.utils.CommonUtils;
import com.sinprl.fetchit.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Profile_Add extends AppCompatActivity {

    FirebaseDatabase database;
    EditText user_name, user_address, user_mobile, user_amount, user_code, user_bank, user_reference, user_bank_manager,user_bank_manager_mobile;
    Spinner type_of_product;

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


//        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        database = FirebaseDatabase.getInstance(App_Constants.FIREBASE_DATABASE);
        user_name = findViewById(R.id.text_user_name);
        user_address = findViewById(R.id.text_user_address);
        user_mobile = findViewById(R.id.text_user_mobile);
        user_amount = findViewById(R.id.text_user_amount);
        user_code = findViewById(R.id.text_user_code);
        user_bank = findViewById(R.id.text_user_choice_of_bank);
        user_reference = findViewById(R.id.text_user_reference);
        user_bank_manager = findViewById(R.id.text_user_bank_manager);
        user_bank_manager_mobile = findViewById(R.id.text_user_bank_manager_mobile);

        type_of_product = findViewById(R.id.spinner_type_of_product);
        String[] stringArray = getResources().getStringArray(R.array.type_of_products);
        List<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
        SpinnerAdaptor product_adaptor = new SpinnerAdaptor(this, R.layout.spinner_value, arrayList);
        type_of_product.setAdapter(product_adaptor);



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
            Profile new_profile = new Profile();
            new_profile.setId(uniqueKey);
            new_profile.setName(user_name.getText().toString().trim());
            new_profile.setAddress(user_address.getText().toString().trim());
            new_profile.setMobile(StringUtils.getValidMobile((user_mobile.getText().toString().trim())));
            new_profile.setTypeofproduct(type_of_product.getSelectedItem().toString());
            new_profile.setChoiceofbank(user_bank.getText().toString().trim());
            new_profile.setAmount(user_amount.getText().toString().trim());
            new_profile.setCode(user_code.getText().toString().trim());
            new_profile.setReference(user_reference.getText().toString().trim());
            new_profile.setBankmanager(user_bank_manager.getText().toString().trim());
            new_profile.setManagermobile(StringUtils.getValidMobile((user_bank_manager_mobile.getText().toString().trim())));
            new_profile.setStatus("NEW");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            new_profile.setEntry_date(dateFormat.format(date));
            new_profile.setDis_date("");
            databaseReference.child(uniqueKey).setValue(new_profile);


            DatabaseReference comment_ref = database.getReference("Profiles/"+uniqueKey+"/Comments/");
            Comment new_comment = new Comment();
            new_comment.setComment_date(dateFormat.format(date));
            new_comment.setComment_text("Status changed to: INQ");
            new_comment.setImportant(Boolean.FALSE);
            comment_ref.push().setValue(new_comment);

            return true;
        }
        else
            return false;
    }

//    private String getValidMobile(String raw_mobile) {
//        String clean_mobile = raw_mobile;
//
//        if (raw_mobile.length() > 10)
//        {
//            clean_mobile = raw_mobile.replaceAll("\\s+", "");
//            clean_mobile = clean_mobile.substring(clean_mobile.length() - 10);
//        }
//
//        return clean_mobile;
//    }

    private boolean valid_input() {
        if (user_name.getText().toString().isEmpty()){
            user_name.setError("Name is mandatory");
            return false;
        }
        if (user_mobile.getText().toString().isEmpty()){
            user_mobile.setError("Mobile is mandatory");
            return false;
        }

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
        if (user_code.getText().toString().isEmpty()){
            user_code.setError("Code is mandatory");
            return false;
        }
        if (user_amount.getText().toString().isEmpty()){
            user_amount.setError("Amount is mandatory");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}