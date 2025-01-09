package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.adaptor.CommentAdaptor;
import com.sinprl.fetchit.data.Comment;
import com.sinprl.fetchit.data.DataEntry;
import com.sinprl.fetchit.interfaces.OnItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Profile_Details extends AppCompatActivity implements OnItemClickListener {
    FirebaseDatabase database;
    String userID;
    TextView user_name, user_mobile, user_address, user_amount, choice_of_bank, type_of_product, user_status;
    FloatingActionButton add_comment;
    RecyclerView comments_recyclerview;
    List<Comment> all_comments_list;

    @Override
    public void onItemClick(View view, int position) {
    }

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
        comments_recyclerview = findViewById(R.id.list_profile_history);

        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        populate_userinformation();
        populate_comments();

        Button button_close = findViewById(R.id.button_profile_details_close);
        button_close.setOnClickListener(v -> {
            finish();
//            Intent home_screen = new Intent(Profile.this, Home.class);
//            startActivity(home_screen);
        });

        Button button_edit = findViewById(R.id.button_profile_details_edit);
        button_edit.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(Profile_Details.this, Profile_Edit.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        add_comment = findViewById(R.id.button_profile_details_addcomment);
        add_comment.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Add Comment here");
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Add", (dialog, whichButton) -> {
                DatabaseReference databaseReference = database.getReference("Profiles/"+userID+"/Comments/");
                Comment new_comment = new Comment();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                new_comment.setComment_date(dateFormat.format(date));
                new_comment.setComment_text(input.getText().toString());
                databaseReference.push().setValue(new_comment);
            });
            alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            });
            alert.show();
        });
        
    }

    private void populate_comments() {
        final LinearLayoutManager comment_layoutmanager = new LinearLayoutManager(this);
        comments_recyclerview.setLayoutManager(comment_layoutmanager);

        DatabaseReference databaseReference = database.getReference("Profiles/"+userID+"/Comments/");

        all_comments_list = new ArrayList<Comment>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_comments_list.clear();
                for (DataSnapshot s:snapshot.getChildren()) {
                    Comment dataEntry = s.getValue(Comment.class);
                    all_comments_list.add(dataEntry);
                }
                CommentAdaptor commentAdaptor = new CommentAdaptor(Profile_Details.this, all_comments_list, Profile_Details.this);
                comments_recyclerview.setAdapter(commentAdaptor);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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