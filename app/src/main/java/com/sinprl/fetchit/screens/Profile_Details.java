package com.sinprl.fetchit.screens;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sinprl.fetchit.data.Profile;
import com.sinprl.fetchit.interfaces.OnItemClickListener;
import com.sinprl.fetchit.utils.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Profile_Details extends AppCompatActivity implements OnItemClickListener {
    FirebaseDatabase database;
    String userID;
    TextView user_name, user_mobile, user_address, user_amount, choice_of_bank, type_of_product, user_status, user_code;
    FloatingActionButton add_comment;
    RecyclerView comments_recyclerview;
    List<Comment> all_comments_list;
    String report_status, base_date;
    Integer period;

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, Profile_Display.class);
        intent.putExtra("report_status", "ALL");
        startActivity(intent);
    }

    public void sendSMS(String phoneNumber, String message) {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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

        Bundle b = getIntent().getExtras();
        report_status = b.getString("report_status","ALL");
        base_date = b.getString("base_date","");
        period = b.getInt("period",-1);
        userID = b.getString("userID","");
        
        user_name = findViewById(R.id.text_profile_details_name);
        user_mobile = findViewById(R.id.text_profile_details_mobile);
        user_address = findViewById(R.id.text_profile_details_address);
        user_amount = findViewById(R.id.text_profile_details_amount);
        choice_of_bank = findViewById(R.id.text_profile_details_choice_of_bank);
        type_of_product = findViewById(R.id.text_profile_details_type_of_product);
        user_code = findViewById(R.id.text_profile_details_code);
        comments_recyclerview = findViewById(R.id.list_profile_history);

        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        populate_userinformation();
        populate_comments();

        Button button_close = findViewById(R.id.button_profile_details_close);
        button_close.setOnClickListener(v -> {
            finish();
            Intent profileDisplay = new Intent(Profile_Details.this, Profile_Display.class);
            Bundle extras = new Bundle();
            extras.putString("report_status", report_status);
            extras.putString("base_date", base_date);
            extras.putInt("period", -1);
            profileDisplay.putExtras(extras);
            startActivity(profileDisplay);
        });

        Button button_edit = findViewById(R.id.button_profile_details_edit);
        button_edit.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(Profile_Details.this, Profile_Edit.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        user_mobile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+91" + user_mobile.getText().toString()));
            startActivity(intent);
        });

        TextView send_sms = findViewById(R.id.text_profile_details_send_sms);
        send_sms.setOnClickListener(v -> {
            sendSMS(user_mobile.getText().toString(), "Sample SMS");
        });

        TextView send_whatsapp = findViewById(R.id.text_profile_details_send_whatsapp);
        send_whatsapp.setOnClickListener(v -> {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + user_mobile.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error While opening Whatsapp", Toast.LENGTH_LONG).show();
            }
        });

        add_comment = findViewById(R.id.button_profile_details_addcomment);
        add_comment.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Add Comment here");
            final View customLayout = getLayoutInflater().inflate(R.layout.add_comment, null);
            CheckBox important = customLayout.findViewById(R.id.checkbox_comment_important);
            EditText comment_text = customLayout.findViewById(R.id.text_comment_text);
            alert.setView(customLayout);
            alert.setPositiveButton("Add", (dialog, whichButton) -> {
                if(!comment_text.getText().toString().isEmpty()) {
                    DatabaseReference databaseReference = database.getReference("Profiles/" + userID + "/Comments/");
                    Comment new_comment = new Comment();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    new_comment.setComment_date(dateFormat.format(date));
                    new_comment.setComment_text(comment_text.getText().toString());
                    if (important.isChecked())
                        new_comment.setImportant(Boolean.TRUE);
                    else
                        new_comment.setImportant(Boolean.FALSE);
                    databaseReference.push().setValue(new_comment);
                }
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

        all_comments_list = new ArrayList<>();

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
                Profile profile = snapshot.getValue(Profile.class);
                profile.setId(snapshot.getKey());
                user_name.setText(StringUtils.toCamelCase(profile.getName()));
                user_mobile.setText(profile.getMobile());
                user_address.setText(StringUtils.toCamelCase( profile.getAddress()));
                user_code.setText(profile.getCode());
                DecimalFormat df = new DecimalFormat("##,##,##,##,##,###");
                user_amount.setText(getResources().getString(R.string.rupee) + "" + df.format(Integer.parseInt(profile.getAmount())));
                choice_of_bank.setText(profile.getChoiceofbank());
                type_of_product.setText(profile.getTypeofproduct());
                update_status(profile.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update_status(String status) {
        TextView circle1 = findViewById(R.id.circle_1);
        TextView circle2 = findViewById(R.id.circle_2);
        TextView circle3 = findViewById(R.id.circle_3);
        TextView circle4 = findViewById(R.id.circle_4);
        TextView circle5 = findViewById(R.id.circle_5);

        circle1.setBackground(getResources().getDrawable(R.drawable.circle_gray));
        circle2.setBackground(getResources().getDrawable(R.drawable.circle_gray));
        circle3.setBackground(getResources().getDrawable(R.drawable.circle_gray));
        circle4.setBackground(getResources().getDrawable(R.drawable.circle_gray));
        circle5.setBackground(getResources().getDrawable(R.drawable.circle_gray));

        switch (status){
            case "NEW":
                circle1.setBackground(getResources().getDrawable(R.drawable.circle_new));
                break;
            case "KYC":
                circle1.setBackground(getResources().getDrawable(R.drawable.circle_new));
                circle2.setBackground(getResources().getDrawable(R.drawable.circle_kyc));
                break;
            case "LOG":
                circle1.setBackground(getResources().getDrawable(R.drawable.circle_new));
                circle2.setBackground(getResources().getDrawable(R.drawable.circle_kyc));
                circle3.setBackground(getResources().getDrawable(R.drawable.circle_log));
                break;
            case "SAN":
                circle1.setBackground(getResources().getDrawable(R.drawable.circle_new));
                circle2.setBackground(getResources().getDrawable(R.drawable.circle_kyc));
                circle3.setBackground(getResources().getDrawable(R.drawable.circle_log));
                circle4.setBackground(getResources().getDrawable(R.drawable.circle_san));
                break;
            case "DIS":
                circle1.setBackground(getResources().getDrawable(R.drawable.circle_new));
                circle2.setBackground(getResources().getDrawable(R.drawable.circle_kyc));
                circle3.setBackground(getResources().getDrawable(R.drawable.circle_log));
                circle4.setBackground(getResources().getDrawable(R.drawable.circle_san));
                circle5.setBackground(getResources().getDrawable(R.drawable.circle_dis));
                break;
        }
    }
}