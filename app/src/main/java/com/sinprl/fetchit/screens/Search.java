package com.sinprl.fetchit.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.adaptor.DataListAdaptor;
import com.sinprl.fetchit.data.Porfile;
import com.sinprl.fetchit.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search extends AppCompatActivity implements OnItemClickListener {
    FirebaseDatabase database;
    List<Porfile> all_data_entries;
    RecyclerView data_recycle_view;
    EditText search_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button_home = findViewById(R.id.button_search_home);
        button_home.setOnClickListener(v -> {
            finish();
            Intent search_screen = new Intent(Search.this, Home.class);
            startActivity(search_screen);
        });

        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        data_recycle_view = findViewById(R.id.list_data);
        populate_data();
        search_user = findViewById(R.id.text_search_user_name);

        search_user.setOnTouchListener((v, event) -> {
            if (search_user.getText().length() > 0){
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (search_user.getRight() - search_user.getCompoundDrawables()[2].getBounds().width())) {
                        search_user.setText("");
                        search_user.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        return true;
                    }
                }}
            return false;
        });

        search_user.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                search_user.clearFocus();
                return true;
            }
            return false;
        });

        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_user.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0);
                List<Porfile> tempList = new ArrayList<>();
                if (search_user.getText().length() == 0)
                    tempList = all_data_entries;
                else {
                    tempList.clear();
                    for (Porfile profile : all_data_entries) {
                        try {
                            if (profile.getName().toUpperCase().contains(search_user.getText().toString().trim().toUpperCase()))
                                tempList.add(profile);
                        }catch (Exception e) {
                            Log.d("searchProfile", e.getMessage());
                        }
                    }
                }
                DataListAdaptor dataListAdaptor = new DataListAdaptor(Search.this, tempList, Search.this);
                data_recycle_view.setAdapter(dataListAdaptor);
            }
            @Override
            public void afterTextChanged(Editable s) {           }
        });






    }

    private void populate_data() {

        final LinearLayoutManager data_entry_layoutmanager = new LinearLayoutManager(this);
        data_recycle_view.setLayoutManager(data_entry_layoutmanager);

        DatabaseReference databaseReference = database.getReference("Profiles/");
        all_data_entries = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_data_entries.clear();
                for (DataSnapshot s:snapshot.getChildren()) {
                    Porfile profile = s.getValue(Porfile.class);
                    profile.setId(s.getKey());
                    all_data_entries.add(profile);
                }
                Collections.reverse(all_data_entries);
                DataListAdaptor dataListAdaptor = new DataListAdaptor(Search.this, all_data_entries, Search.this);
                data_recycle_view.setAdapter(dataListAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), Profile_Details.class);
        intent.putExtra("userID", all_data_entries.get(position).getId());
        startActivity(intent);
    }
}