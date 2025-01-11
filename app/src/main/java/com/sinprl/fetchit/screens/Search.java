package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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